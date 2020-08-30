package com.example.disasterfasoulisproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class FireActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, LocationListener {

    private Uri filePath;
    private static final int REQ_CODE = 10;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    TextView txtfire;
    Button btnFireMessage,btnUploadDisasterImage,btnChooseImage;
    ImageView imageView;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseAuth myauth;
    FirebaseDatabase database;

    Location loc;
    double latitude;
    double longtitude;


    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);
        btnFireMessage = findViewById(R.id.btnSentFireMessage);
        btnUploadDisasterImage = findViewById(R.id.btnUploadDisasterImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        txtfire = findViewById(R.id.txtFire);
        imageView = findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        myauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = myauth.getCurrentUser();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);


        runpermision();
        
        btnUploadDisasterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }



    private void uploadImage() {

       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (filePath!=null){
            StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(FireActivity.this, "Image Umploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + progres + "%");
                }
            });
        }*/

        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomkey);

        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                       Snackbar.make(findViewById(android.R.id.content),"Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(FireActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + progres + "%");
            }
        });

    }


    public void btn_SendAlertSMS(View view) {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (loc!=null){
            latitude = loc.getLatitude();
            longtitude = loc.getLongitude();

            String message = "Βρίσκομαι στην τοποθεσία με γεωγραφικό μήκος " + String.valueOf(latitude) + " και γεωγραφικό πλάτος "  + String.valueOf(longtitude) +
                    " και παρατηρώ μια πυρκαϊά";
            String tel = "6984498150";

            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},11);
            }else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(tel,null,message,sentPI,deliveredPI);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();



            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && requestCode == RESULT_OK && data != null && data.getData()!=null){
            filePath = data.getData();
            //Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
            imageView.setImageURI(filePath);
        }
    }

    //----------------------------- Methods for permission-------------------------------------------------------//
    public void runpermision(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Toast.makeText(this, "Location already granted", Toast.LENGTH_SHORT).show();
        }
    }


    //Αν πάρεις το lOcation (αποτέλεσμα παραθύρου)κανε αυτα που σου λεω αλλιώς τόνισε οτι το χρειάζεσαι
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQ_CODE && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }else {
            Toast.makeText(this, "I need it", Toast.LENGTH_SHORT).show();
        }
    }

    //Αυτές οι μέθοδοι γίνονται αναγκαστικά ovveride γιατί πρέπει να γίνουν implemented απο το interface LocationListener το οποίο δηλώνω στην αρχή
    @Override
    public void onLocationChanged(Location location) {

        if (location==null){
            txtfire.setText("Unable to Load Location. Please try again later");
        }
        else {
            txtfire.setText("Βρίσκομαι στην τοποθεσία με γεωγραφικό μήκος : " + location.getLatitude() + "και γεωγραφικό πλάτος :  " + location.getLatitude() + " και παρατηρώ μια πυρκαϊά");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
//--------------------------------------------------------------------------------------------------------------------------------//




}
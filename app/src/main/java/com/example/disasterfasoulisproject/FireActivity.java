package com.example.disasterfasoulisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FireActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener,LocationListener {

    private static  final int REQ_CODE = 10;
    Button btnFireMessage;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseAuth myauth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);
        btnFireMessage = findViewById(R.id.btnSentFireMessage);

        myauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = myauth.getCurrentUser();
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);


        runpermision();

    }



    //----------------------------- Methods for permission-------------------------------------------------------//
    public void runpermision(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
            Toast.makeText(this, "Location already granted", Toast.LENGTH_SHORT).show();
        }
    }


    //Αν πάρεις το lOcation (αποτέλεσμα παραθύρου)κανε αυτα που σου λεω αλλιώς τόνισε οτι το χρειάζεσαι
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQ_CODE && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
            }
        }else {
            Toast.makeText(this, "I need it", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        
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
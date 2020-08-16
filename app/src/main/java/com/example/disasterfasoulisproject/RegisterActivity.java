package com.example.disasterfasoulisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register Activity";
    TextView textView2;

    EditText editEmail,editPass,editConfPass;
    Button btnRegister;
    ProgressBar progressBar;

    FirebaseAuth myauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myauth = FirebaseAuth.getInstance();
        textView2 = findViewById(R.id.textView2);


        editEmail = findViewById(R.id.edtemail);
        editPass = findViewById(R.id.edtpass);
        editConfPass = findViewById(R.id.edtconfirmpass);
        btnRegister = findViewById(R.id.btnRegister);
        //progressBar = findViewById(R.id.progressBar2);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editEmail.getText().toString().isEmpty() && !editPass.getText().toString().isEmpty() && !editConfPass.getText().toString().isEmpty()){
                    String password = editPass.getText().toString();
                    if (password.equals(editConfPass.getText().toString())){
                        register(editEmail.getText().toString(),editPass.getText().toString());
                    }else{
                        Toast.makeText(RegisterActivity.this, "Passwords doesnt Match", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill All fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }


    //------------------------------------------register--------------------------------------------------------------------------------//
    private void register(String email, String password){
        myauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG,"On Complete: " + task.isSuccessful());
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"On Complete :  Auth : " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sendverificationmail();
                    // GO TO ANOTHER ACTIVITY
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    //myauth.signOut();

                }else {
                    Toast.makeText(RegisterActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //-----------------------------Send Users Data-------------------------------------------//

    private  void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(myauth.getUid());
        //myRef.setValue()
    }

    //-------------------------------------------------------------------------------------//
    private void showdialog(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidedialog(){
        if (progressBar.getVisibility()==(View.VISIBLE)){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void sendverificationmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Please verify t oyour mail", Toast.LENGTH_SHORT).show();
                        myauth.signOut();
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this, "An Error ocured"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
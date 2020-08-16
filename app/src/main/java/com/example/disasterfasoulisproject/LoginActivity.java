package com.example.disasterfasoulisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editmail,editpass;
    TextView textView;
    Button btnLog;
    FirebaseAuth myauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
        editmail = findViewById(R.id.edit_email);
        editpass = findViewById(R.id.edit_pass);
        btnLog = findViewById(R.id.btn_login);

        myauth = FirebaseAuth.getInstance();

        FirebaseUser user = myauth.getCurrentUser();

        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            user = myauth.getCurrentUser();
            String uid = user.getUid().toString();
            intent.putExtra("usersId",uid);
            startActivity(intent);
            finish();

        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });



        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editmail.getText().toString()) && !TextUtils.isEmpty(editpass.getText().toString())) {

                    myauth.signInWithEmailAndPassword(editmail.getText().toString(), editpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (myauth.getCurrentUser().isEmailVerified() && myauth.getCurrentUser()!=null){
                                    Toast.makeText(LoginActivity.this, "LogedIn Succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    //FirebaseUser user = myauth.getCurrentUser();
                                    //String uid = user.getUid().toString();
                                    //intent.putExtra("usersId",uid);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();                                  }

                            } else {
                                Toast.makeText(LoginActivity.this, "Errror" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Please fill All fields", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
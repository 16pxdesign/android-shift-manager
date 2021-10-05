package com.ruszala.fueltrack.auth.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.auth.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    FirebaseAuth authInstance;
    EditText emailText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register");

        //refs
        authInstance = FirebaseAuth.getInstance();
        registerBtn = (Button) findViewById(R.id.register_btn);
        emailText = (EditText) findViewById(R.id.emailInput);
        passwordText = (EditText) findViewById(R.id.passwordInput);

        //set listener for register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                //check for auth to firebase
                authInstance.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Finish and back
                            Toast.makeText(getApplicationContext(), R.string.registrationCompliteMsg  , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            //Error
                            Toast.makeText(getApplicationContext(), R.string.registerFailMsg , Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    //bind back-button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

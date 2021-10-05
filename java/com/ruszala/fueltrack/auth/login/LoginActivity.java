package com.ruszala.fueltrack.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.auth.register.RegisterActivity;

/**
 * Startup activity, allows the user to log in to application or go to the registration window.
 */
public class LoginActivity extends AppCompatActivity {

    FirebaseAuth authInstance;
    FirebaseAuth.AuthStateListener authStateListener;

    EditText emailText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.welcome));

        //refs
        Button login_btn = (Button) findViewById(R.id.login_btn);
        Button join_btn = (Button) findViewById(R.id.join);
        emailText = (EditText) findViewById(R.id.emailInput);
        passwordText = (EditText) findViewById(R.id.passwordInput);


        //Firebase auth
        authInstance = FirebaseAuth.getInstance();
        //authInstance.signOut();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //listener for login button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                //check for auth to firebase
                authInstance.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            //show error
                            Toast.makeText(getApplicationContext(), R.string.LoginFailMsg , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //listener to register button
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open register activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //add listeners
        authInstance.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //remove listeners
        authInstance.removeAuthStateListener(authStateListener);

    }
}

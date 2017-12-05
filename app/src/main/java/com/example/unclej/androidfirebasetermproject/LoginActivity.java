package com.example.unclej.androidfirebasetermproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    String TAG = "PH";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String email;
        String password;
        login = (Button)findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonLogin(v);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() { // 인증 상태 리스너
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("PH", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("PH", "onAuthStateChanged:signed_out");
                }
            }
        };


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();

            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }


    }

    public void onButtonLogin(View v) {
        String email = ((EditText)findViewById(R.id.id)).getText().toString();
        String password = ((EditText)findViewById(R.id.pw)).getText().toString();
        mAuth.signInWithEmailAndPassword(email, password) // Task 객체 리턴
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("PH", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) { // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로긴!",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

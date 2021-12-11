package com.myapp.tremplist_update;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent mainIntent;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // user is logged in
            if ( user != null ){
                mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
            }
            // if the user is not logged in
            else{
                mainIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
            }

            SplashScreenActivity.this.startActivity(mainIntent);
            finish(); // so you cant return back from MainActivity to SplashScreen
        }, SPLASH_DISPLAY_LENGTH);

    }
}

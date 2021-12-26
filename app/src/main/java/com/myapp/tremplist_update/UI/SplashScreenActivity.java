package com.myapp.tremplist_update.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.tremplist_update.R;

// In this class we implemtnt the splashScreen that shows when you open the app.

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500; // how much time the screen will appear
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // which layout will show

        new Handler().postDelayed(() -> {
            Intent mainIntent;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // user is logged in taking him to the Main page in the app.
            if ( user != null ){
                mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
            }
            // if the user is not logged in take him to the Login screen.
            else{
                mainIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
            }

            SplashScreenActivity.this.startActivity(mainIntent);
            finish(); // so you cant return back from MainActivity to SplashScreen
        }, SPLASH_DISPLAY_LENGTH);

    }
}

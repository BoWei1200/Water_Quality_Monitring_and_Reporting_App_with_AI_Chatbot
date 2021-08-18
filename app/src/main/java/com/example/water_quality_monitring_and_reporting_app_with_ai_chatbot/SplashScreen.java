package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity{

    ImageView logo;
    TextView appname;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        appname=findViewById(R.id.appname);
        lottieAnimationView=findViewById(R.id.lottie);

        //duration 1000=1sec
        logo.animate().setDuration(4000);
        appname.animate().setDuration(4000);
        lottieAnimationView.animate().setDuration(4000);

        Runnable r = new Runnable() {

            @Override
            public void run() {
                // if you are redirecting from a fragment then use getActivity() as the context.
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();//user cannot switch back to splash screen
            }
        };

        Handler h = new Handler();
        // The Runnable will be executed after the given delay time
        h.postDelayed(r, 4000); // delay 4 sec
    }

    /**
     *
     * Enable Full Screen Mode
     */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
package com.example.internshipproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    Animation topanim, bottomanim;
    private static int SPLASH_SCREEN = 3000;
    ImageView imageView;
    TextView logo, slogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_splash);

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        imageView = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogo = findViewById(R.id.textView2);

        imageView.setAnimation(topanim);
        logo.setAnimation(bottomanim);
        slogo.setAnimation(bottomanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this,Reg_Activity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);
    }

}

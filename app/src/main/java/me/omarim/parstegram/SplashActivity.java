package me.omarim.parstegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                goToLogin();
            }
        };

        handler.postDelayed(r, 2000);


    }

    void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}

package com.avtdev.jokeworld.activities;

import android.content.Intent;
import android.os.Bundle;

import com.avtdev.jokeworld.R;

public class SplashActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

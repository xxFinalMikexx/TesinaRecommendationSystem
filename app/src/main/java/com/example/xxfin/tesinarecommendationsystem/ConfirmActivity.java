package com.example.xxfin.tesinarecommendationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
    }

    public void okConfirmar(View v) {
        Intent intentMain = new Intent(ConfirmActivity.this, MainActivity.class);
        this.startActivity(intentMain);
        this.finish();
    }
}

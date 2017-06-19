package com.example.xxfin.tesinarecommendationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

public class MainActivity extends AppCompatActivity {

    /*Variables para la actividad*/
    private String email = "";
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");

    }

    public void enviarAporte(View v) {
        Intent aporteIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        aporteIntent.putExtra("email", this.email);
        aporteIntent.putExtra("userId", this.userId);
        this.startActivity(aporteIntent);
    }

    public void buscarRecomendacion(View v) {
        Intent recomendacionIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        recomendacionIntent.putExtra("email", this.email);
        recomendacionIntent.putExtra("userId", this.userId);
        this.startActivity(recomendacionIntent);
    }
}

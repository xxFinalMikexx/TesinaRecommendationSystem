package com.example.xxfin.tesinarecommendationsystem;

import android.app.ProgressDialog;
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
    private int userType = 0;
    private String genero = "";
    private int edad = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");
        this.userType = (int) extras.get("userType");
        this.genero = (String) extras.get("genero");
        this.edad = (int) extras.get("edad");

    }

    public void enviarAporte(View v) {
        Intent aporteIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        aporteIntent.putExtra("email", this.email);
        aporteIntent.putExtra("userId", this.userId);
        aporteIntent.putExtra("userType", this.userType);
        this.startActivity(aporteIntent);
        this.finish();
    }

    public void buscarRecomendacion(View v) {
        Intent recomendacionIntent = new Intent(MainActivity.this, RecomendacionActivity.class);
        recomendacionIntent.putExtra("email", this.email);
        recomendacionIntent.putExtra("userId", this.userId);
        recomendacionIntent.putExtra("userType", this.userType);
        recomendacionIntent.putExtra("genero", this.genero);
        recomendacionIntent.putExtra("edad", this.edad);
        this.startActivity(recomendacionIntent);
        this.finish();
    }
}

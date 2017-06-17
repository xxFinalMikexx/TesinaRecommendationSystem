package com.example.xxfin.tesinarecommendationsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void enviarAporte(View v) {
        Intent aporteIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        this.startActivity(aporteIntent);
    }

    public void buscarRecomendacion(View v) {
        Intent recomendacionIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        this.startActivity(recomendacionIntent);
    }
}

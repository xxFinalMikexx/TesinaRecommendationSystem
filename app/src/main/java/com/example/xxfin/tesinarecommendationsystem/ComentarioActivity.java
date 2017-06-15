package com.example.xxfin.tesinarecommendationsystem;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

public class ComentarioActivity extends AppCompatActivity {

    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
    }

    public void tomarFoto(View v) {

    }

    public void obtenerCoordenadas() {
        Location coordenadas = null;

        this.latitud = coordenadas.getLatitude();
        this.longitud = coordenadas.getLongitude();
    }
}

package com.example.xxfin.tesinarecommendationsystem;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class RecomendacionActivity extends AppCompatActivity {

    /*Local Variables*/
    private String email = "";
    private String userId = "";
    private String tipoLista[] = {"Hospital", "Library", "Bar", "Cafe", "Museo", "Escuela", "Banco", "Restaurante", "Tienda", "Alojamiento"};
    private String rangoLista[] = {"0.5km", "1km", "1.5kms", "2kms", "2.5kms", "3kms", "3.5kms", "4kms", "4.5kms", "5kms"};

    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;

    private DatabaseReference mDatabase;
    private DatabaseReference mFirebaseDatabaseReference;

    /*Static code of result*/
    private static final String API_KEY = "AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion);

        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");

        /*Crea cliente de para la localización*/
        crearClienteLocalizacion();

        Spinner generoSpin = (Spinner) findViewById(R.id.genero);
        Spinner edadesSpin = (Spinner) findViewById(R.id.rangoEdad);

        ArrayAdapter<String> generoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.tipoLista);
        ArrayAdapter<String> edadedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.rangoLista);
        generoSpin.setAdapter(generoAdapter);
        edadesSpin.setAdapter(edadedAdapter);
    }

    public synchronized void crearClienteLocalizacion() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        } catch (NullPointerException npe) {
            throw new NullPointerException();
        } catch (IllegalStateException ise) {
            throw new IllegalStateException();
        }
    }

    
}

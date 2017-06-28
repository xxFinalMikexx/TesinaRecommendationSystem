package com.example.xxfin.tesinarecommendationsystem;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xxfin.tesinarecommendationsystem.Objects.InfoPlace;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetallesActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*Static Final Variables*/
    private static final String API_KEY = "AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1002;

    /*Firebase Variables*/
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    /*Google variables*/
    public GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    private Marker user_marker;

    /*Activity Variables*/
    private boolean mLocationPermissionGranted = false;
    private double latitud = 0;
    private double longitud = 0;
    private String placeId;

    /*Views and Buttons*/
    private TextView nombreLugar;
    private TextView direccionLugar;
    private TextView ratingLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        this.mAuth = FirebaseAuth.getInstance();
        this.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.user = mAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        this.placeId = (String) extras.get("placeId");

        this.nombreLugar = (TextView) findViewById(R.id.nombreLugar);
        this.direccionLugar = (TextView) findViewById(R.id.direccionLugar);
        this.ratingLugar = (TextView) findViewById(R.id.ratingLugar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect(); // Conecta con el servicio de ubicación de Google
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detallesMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Comentarios Acitividad", "No se pudieron obtener las coordenadas");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("Comentarios Actividad", "Conexión suspendida");
        //mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {
            mMap = map;
            Log.e("Map", "Map Ready!!");
            getDeviceLocation();
            if (!this.placeId.equals("")) {
                obtenPlaceInfo();
            }
        } catch(Exception e) {
            Log.e("Error Map", "-"+e.getMessage());
        }
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mLastLocation != null) {
            this.latitud = mLastLocation.getLatitude();
            this.longitud = mLastLocation.getLongitude();
            LatLng selected_point = new LatLng(this.latitud, this.longitud);
            user_marker = mMap.addMarker(new MarkerOptions()
                    .position(selected_point)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            new MarkerOptions();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selected_point, 12.0f));

        } else {
            Log.d("NULL current", "Current location is null. Using defaults.");
        }
    }

    public void obtenPlaceInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("placeid=").append(placeId);
            googlePlacesUrl.append("&key=").append(API_KEY);
            Log.e("Detalles de lugar", googlePlacesUrl.toString());
            final JsonObjectRequest placeRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            obtenerInfoLugar(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(RecomendacionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(placeRequest);
        } catch(Exception e) {
            Log.e("DETALLES LUGAR", e.getMessage());
        }
    }

    public void obtenerInfoLugar(JSONObject result) {
        try {
            JSONObject jsonObj = result.getJSONObject("result");

            this.nombreLugar.setText(jsonObj.getString("name"));
            this.ratingLugar.setText("Calificación otorgada por usuarios: "+jsonObj.getDouble("rating"));
            this.direccionLugar.setText(jsonObj.getString("formatted_address"));

        } catch(Exception e) {
            Log.e("InfoLugar", e.getMessage());
            //e.printStackTrace();
            return;
        }
    }
}

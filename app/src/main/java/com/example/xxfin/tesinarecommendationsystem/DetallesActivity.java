package com.example.xxfin.tesinarecommendationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xxfin.tesinarecommendationsystem.Objects.Asociaciones;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

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
    private LinkedList listaDestinos;
    private LinkedList asociacionesDestinos;
    private ProgressDialog prDialog;
    private JSONArray reviews;
    private boolean comentarios = false;

    /*Views and Buttons*/
    private TextView nombreLugar;
    private TextView direccionLugar;
    private TextView ratingLugar;
    private TextView mostrarComentarios;
    private ImageView asocImage1; private TextView asocNombre1; private TextView asocDir1;
    private ImageView asocImage2; private TextView asocNombre2; private TextView asocDir2;
    private ImageView asocImage3; private TextView asocNombre3; private TextView asocDir3;
    private ImageView asocImage4; private TextView asocNombre4; private TextView asocDir4;
    private ImageView asocImage5; private TextView asocNombre5; private TextView asocDir5;
    private GridLayout asocGrid1;
    private GridLayout asocGrid2;
    private GridLayout asocGrid3;
    private GridLayout asocGrid4;
    private GridLayout asocGrid5;

    private GridLayout reviewGrid;
    private TextView author1; private TextView hace1; private TextView calif1; private TextView comentario1;
    private TextView author2; private TextView hace2; private TextView calif2; private TextView comentario2;
    private TextView author3; private TextView hace3; private TextView calif3; private TextView comentario3;
    private TextView author4; private TextView hace4; private TextView calif4; private TextView comentario4;
    private TextView author5; private TextView hace5; private TextView calif5; private TextView comentario5;

    public String actualPlace1;
    public String actualPlace2;
    public String actualPlace3;
    public String actualPlace4;
    public String actualPlace5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        this.mAuth = FirebaseAuth.getInstance();
        this.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.user = mAuth.getCurrentUser();

        this.listaDestinos = new LinkedList();
        this.asociacionesDestinos = new LinkedList();

        Bundle extras = getIntent().getExtras();
        this.placeId = (String) extras.get("placeId");
        if(this.placeId == null) {
            Log.e("Place id", "Nulo");
        }

        this.nombreLugar = (TextView) findViewById(R.id.nombreLugar);
        this.direccionLugar = (TextView) findViewById(R.id.direccionLugar);
        this.ratingLugar = (TextView) findViewById(R.id.ratingLugar);
        this.mostrarComentarios = (TextView) findViewById(R.id.mostrarComentarios);

        /*************************/
        this.asocImage1 = (ImageView)findViewById(R.id.imagen1);
        this.asocImage2 = (ImageView)findViewById(R.id.imagen2);
        this.asocImage3 = (ImageView)findViewById(R.id.imagen3);
        this.asocImage4 = (ImageView)findViewById(R.id.imagen4);
        this.asocImage5 = (ImageView)findViewById(R.id.imagen5);

        this.asocNombre1 = (TextView)findViewById(R.id.nombre1);
        this.asocNombre2 = (TextView)findViewById(R.id.nombre2);
        this.asocNombre3 = (TextView)findViewById(R.id.nombre3);
        this.asocNombre4 = (TextView)findViewById(R.id.nombre4);
        this.asocNombre5 = (TextView)findViewById(R.id.nombre5);

        this.asocDir1 = (TextView)findViewById(R.id.direccion1);
        this.asocDir2 = (TextView)findViewById(R.id.direccion2);
        this.asocDir3 = (TextView)findViewById(R.id.direccion3);
        this.asocDir4 = (TextView)findViewById(R.id.direccion4);
        this.asocDir5 = (TextView)findViewById(R.id.direccion5);

        this.asocGrid1 = (GridLayout)findViewById(R.id.propuesta1);
        this.asocGrid2 = (GridLayout)findViewById(R.id.propuesta2);
        this.asocGrid3 = (GridLayout)findViewById(R.id.propuesta3);
        this.asocGrid4 = (GridLayout)findViewById(R.id.propuesta4);
        this.asocGrid5 = (GridLayout)findViewById(R.id.propuesta5);

        this.reviewGrid = (GridLayout)findViewById(R.id.reviewsGrid);
        this.reviewGrid.setVisibility(View.GONE);

        this.author1 = (TextView) findViewById(R.id.author1);
        this.author2 = (TextView) findViewById(R.id.author2);
        this.author3 = (TextView) findViewById(R.id.author3);
        this.author4 = (TextView) findViewById(R.id.author4);
        this.author5 = (TextView) findViewById(R.id.author5);

        this.hace1 = (TextView) findViewById(R.id.hace1);
        this.hace2 = (TextView) findViewById(R.id.hace2);
        this.hace3 = (TextView) findViewById(R.id.hace3);
        this.hace4 = (TextView) findViewById(R.id.hace4);
        this.hace5 = (TextView) findViewById(R.id.hace5);

        this.calif1 = (TextView) findViewById(R.id.calif1);
        this.calif2 = (TextView) findViewById(R.id.calif2);
        this.calif3 = (TextView) findViewById(R.id.calif3);
        this.calif4 = (TextView) findViewById(R.id.calif4);
        this.calif5 = (TextView) findViewById(R.id.calif5);

        this.comentario1 = (TextView) findViewById(R.id.comentario1);
        this.comentario2 = (TextView) findViewById(R.id.comentario2);
        this.comentario3 = (TextView) findViewById(R.id.comentario3);
        this.comentario4 = (TextView) findViewById(R.id.comentario4);
        this.comentario5 = (TextView) findViewById(R.id.comentario5);


        /*************************/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();


        mFirebaseDatabaseReference.child("Asociaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot asociacionSnap : dataSnapshot.getChildren()) {
                    try {
                        Asociaciones actual = asociacionSnap.getValue(Asociaciones.class);
                        Log.e("Actual", actual.getPlaceIdOrigen());
                        if (placeId.equals(actual.getPlaceIdOrigen())) {
                            listaDestinos.addLast(actual);
                        }
                    }catch(Exception e) {
                        Log.e("Error Asocioaciones", "-"+e.getMessage());
                        e.printStackTrace();
                    }
                }
                revisarConfianza(listaDestinos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Se ha interrumpido la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        prDialog = new ProgressDialog(this); // Crear un dialogo para mostrar progreso
        prDialog.setCancelable(false);
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

    public void mostrarComentarios(View v) {
        if(comentarios) {
            this.reviewGrid.setVisibility(View.GONE);
            this.comentarios = false;
        } else {
            this.reviewGrid.setVisibility(View.VISIBLE);
            this.comentarios = true;
        }
    }

    public void revisarConfianza(LinkedList destinos) {
        double promedio = 0;
        for(int i = 0; i < destinos.size(); i++) {
            Asociaciones actual = (Asociaciones)destinos.get(i);
            promedio += Double.parseDouble(actual.getConfianza());
        }
        promedio = promedio / destinos.size();

        for(int i = 0; i < destinos.size(); i++) {
            Asociaciones actual = (Asociaciones)destinos.get(i);
            if(Double.parseDouble(actual.getConfianza()) > promedio) {
                obtenDetallesLugar(actual.getPlaceIdDestino());
            }
        }

        prDialog.setMessage("Obteniendo lista de recomendaciones..."); // Asignar mensaje al dialogo de progreso
        prDialog.show(); // Mostrar dialogo de progreso
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                mostarAsociaciones();
                prDialog.hide();
            }
        }, 5000);

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

    public void obtenDetallesLugar(String place) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("placeid=").append(place);
            googlePlacesUrl.append("&key=").append(API_KEY);
            Log.e("Detalles Asociaciones", googlePlacesUrl.toString());
            final JsonObjectRequest placeRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            obtenerAsociaciones(response);
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

    public void obtenPlaceInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("placeid=").append(placeId);
            googlePlacesUrl.append("&key=").append(API_KEY);
            Log.e("Detalles lugar", googlePlacesUrl.toString());
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

    public void obtenerAsociaciones(JSONObject result) {
        try {
            InfoPlace info = new InfoPlace();
            JSONObject jsonObj = result.getJSONObject("result");

            info.setName(jsonObj.getString("name"));

            info.setDireccion(jsonObj.getString("formatted_address"));

            JSONArray photos = jsonObj.getJSONArray("photos");
            JSONObject photoRef = photos.getJSONObject(0);
            String ref = photoRef.getString("photo_reference");
            info.setPhoto(ref);

            info.setPlaceId(jsonObj.getString("place_id"));

            this.asociacionesDestinos.addLast(info);
        } catch(Exception e) {
            Log.e("Asociaciones Lugar", e.getMessage());
            //e.printStackTrace();
            return;
        }
    }

    public void obtenerInfoLugar(JSONObject result) {
        try {
            JSONObject jsonObj = result.getJSONObject("result");

            try {
                this.nombreLugar.setText(jsonObj.getString("name"));
            } catch(Exception e) {
                this.nombreLugar.setText("Nombre no encontrado");
            }
            try {
                this.ratingLugar.setText("Calificación otorgada por usuarios: "+jsonObj.getDouble("rating"));
            } catch(Exception e) {
                this.ratingLugar.setText("No se tienen datos de rating");
            }
            try {
                this.direccionLugar.setText(jsonObj.getString("formatted_address"));
            } catch(Exception e) {
                this.direccionLugar.setText("No se ha especificado una dirección válida");
            }

            try {
                this.reviews = jsonObj.getJSONArray("reviews");
                JSONObject actual = null;

                actual = this.reviews.getJSONObject(0);
                this.author1.setText(actual.getString("author_name"));
                this.hace1.setText(actual.getString("relative_time_description"));
                this.calif1.setText("Calificación otorgada: "+actual.getString("rating"));
                this.comentario1.setText(actual.getString("text"));

                actual = this.reviews.getJSONObject(1);
                this.author2.setText(actual.getString("author_name"));
                this.hace2.setText(actual.getString("relative_time_description"));
                this.calif2.setText("Calificación otorgada: "+actual.getString("rating"));
                this.comentario2.setText(actual.getString("text"));

                actual = this.reviews.getJSONObject(2);
                this.author3.setText(actual.getString("author_name"));
                this.hace3.setText(actual.getString("relative_time_description"));
                this.calif3.setText("Calificación otorgada: "+actual.getString("rating"));
                this.comentario3.setText(actual.getString("text"));

                actual = this.reviews.getJSONObject(3);
                this.author4.setText(actual.getString("author_name"));
                this.hace4.setText(actual.getString("relative_time_description"));
                this.calif4.setText("Calificación otorgada: "+actual.getString("rating"));
                this.comentario4.setText(actual.getString("text"));

                actual = this.reviews.getJSONObject(4);
                this.author5.setText(actual.getString("author_name"));
                this.hace5.setText(actual.getString("relative_time_description"));
                this.calif5.setText("Calificación otorgada: "+actual.getString("rating"));
                this.comentario5.setText(actual.getString("text"));

            } catch(Exception e) {
                Log.e("Comentarios", "-"+e.getMessage());
            }


        } catch(Exception e) {
            Log.e("InfoLugar", e.getMessage());
            //e.printStackTrace();
            return;
        }
    }

    public synchronized void mostarAsociaciones() {
        LinkedList finalRecomendaciones = null;
        try {
            finalRecomendaciones = sortLinkedList(this.asociacionesDestinos);
            if(finalRecomendaciones != null) {
                InfoPlace actual = new InfoPlace();

                actual = (InfoPlace) finalRecomendaciones.get(0);
                String photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
                Picasso.with(getApplicationContext()).load(photoRef).into(this.asocImage1);
                this.actualPlace1 = actual.getPlaceId();
                this.asocNombre1.setText(actual.getName());
                this.asocDir1.setText(actual.getDireccion().toString());
                this.asocGrid1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        iniciarDetalles(actualPlace1);
                    }
                });

                actual = (InfoPlace) finalRecomendaciones.get(1);
                photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
                Picasso.with(getApplicationContext()).load(photoRef).into(this.asocImage2);
                this.actualPlace2 = actual.getPlaceId();
                this.asocNombre2.setText(actual.getName());
                this.asocDir2.setText(actual.getDireccion().toString());
                this.asocGrid2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        iniciarDetalles(actualPlace2);
                    }
                });

                actual = (InfoPlace) finalRecomendaciones.get(2);
                photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
                Picasso.with(getApplicationContext()).load(photoRef).into(this.asocImage3);
                this.actualPlace3 = actual.getPlaceId();
                this.asocNombre3.setText(actual.getName());
                this.asocDir3.setText(actual.getDireccion().toString());
                this.asocGrid3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        iniciarDetalles(actualPlace3);
                    }
                });

                actual = (InfoPlace) finalRecomendaciones.get(3);
                photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
                Picasso.with(getApplicationContext()).load(photoRef).into(this.asocImage4);
                this.actualPlace4 = actual.getPlaceId();
                this.asocNombre4.setText(actual.getName());
                this.asocDir4.setText(actual.getDireccion().toString());
                this.asocGrid4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        iniciarDetalles(actualPlace4);
                    }
                });

                actual = (InfoPlace) finalRecomendaciones.get(4);
                photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
                Picasso.with(getApplicationContext()).load(photoRef).into(this.asocImage5);
                this.actualPlace5 = actual.getPlaceId();
                this.asocNombre5.setText(actual.getName());
                this.asocDir5.setText(actual.getDireccion().toString());
                this.asocGrid5.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        iniciarDetalles(actualPlace5);
                    }
                });
            }
        } catch(Exception e) {

        }
    }

    public void iniciarDetalles(String placeId) {
        Intent detallesIntent = new Intent(DetallesActivity.this, DetallesActivity.class);
        detallesIntent.putExtra("placeId", placeId);
        Log.e("Pasando Place", placeId);
        startActivity(detallesIntent);
    }

    public LinkedList sortLinkedList(LinkedList lista) {
        try {
            LinkedList returnLista = new LinkedList();

            for (int i = 0; i < lista.size(); i++) {
                InfoPlace info = (InfoPlace) lista.get(i);
                if (returnLista.isEmpty()) {
                    returnLista.addFirst(info);
                } else {
                    for (int j = 0; j < returnLista.size(); j++) {
                        InfoPlace actual = (InfoPlace) returnLista.get(j);
                        if (j == returnLista.size() - 1) {
                            returnLista.addLast(info);
                            break;
                        }
                        if (actual.getRating() <= info.getRating()) {
                            returnLista.add(j, info);
                            break;
                        }
                    }
                }
            }

            return returnLista;
        } catch(Exception e) {
            return null;
        }
    }
}

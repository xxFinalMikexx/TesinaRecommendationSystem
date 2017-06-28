package com.example.xxfin.tesinarecommendationsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.xxfin.tesinarecommendationsystem.Objects.Comments;
import com.example.xxfin.tesinarecommendationsystem.Objects.InfoPlace;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.example.xxfin.tesinarecommendationsystem.Objects.Users;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RecomendacionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*Local Variables*/
    private String email = "";
    private String userId = "";
    private String tipoLista[] = {"Hospital", "Librería", "Bar", "Café", "Museo", "Escuela", "Banco", "Restaurante", "Tienda", "Alojamiento"};
    private String rangoLista[] = {"0.5km", "1km", "1.5kms", "2kms", "2.5kms", "3kms", "3.5kms", "4kms", "4.5kms", "5kms"};
    private double latitud = 0; // Variable para guardar latitud
    private double longitud = 0; // Variable para guardar longitud
    private Users userInfo;
    private LinkedList listaCercanos = new LinkedList();
    private LinkedList listaDB = new LinkedList();
    private Spinner tipoSpin;
    private Spinner rangoSpin;
    private CheckBox generoBox;
    private CheckBox edadBox;
    private GridLayout gridMain;
    private GridView gridPopulate;
    private String tipoLugar = "";
    private String rangoBusqueda = "";
    public String tipoSeleccionado = "";
    public String rangoSeleccionado = "";
    HashMap placesHash = new HashMap();
    public boolean generoCheck = false;
    public boolean edadCheck = false;
    public boolean flag = false;
    private ProgressDialog prDialog;

    /*Views para recomendaciones*/
    private ImageView imagen1; private TextView nombre1; private TextView direccion1; private TextView rating1;
    private ImageView imagen2; private TextView nombre2; private TextView direccion2; private TextView rating2;
    private ImageView imagen3; private TextView nombre3; private TextView direccion3; private TextView rating3;
    private ImageView imagen4; private TextView nombre4; private TextView direccion4; private TextView rating4;
    private ImageView imagen5; private TextView nombre5; private TextView direccion5; private TextView rating5;
    private ImageView imagen6; private TextView nombre6; private TextView direccion6; private TextView rating6;
    private ImageView imagen7; private TextView nombre7; private TextView direccion7; private TextView rating7;
    private ImageView imagen8; private TextView nombre8; private TextView direccion8; private TextView rating8;
    private ImageView imagen9; private TextView nombre9; private TextView direccion9; private TextView rating9;
    private ImageView imagen10; private TextView nombre10; private TextView direccion10; private TextView rating10;

    private GridLayout recomend1;   private GridLayout gridGoogle1;
    private GridLayout recomend2;   private GridLayout gridGoogle2;
    private GridLayout recomend3;   private GridLayout gridGoogle3;
    private GridLayout recomend4;   private GridLayout gridGoogle4;
    private GridLayout recomend5;   private GridLayout gridGoogle5;

    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;

    /*Firebase references*/
    private DatabaseReference mDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    /*Static code of result*/
    private static final String API_KEY = "AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE";
    private static final int REQUEST_ACCESS_FINE_LOCATION = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion);

        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");

        /*Crea cliente de para la localización*/
        crearClienteLocalizacion();

        if (!gpsEstaActivado()) {
            solicitarActivacionGPS();
        }

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getApplicationContext(), "User DataSnapshot", Toast.LENGTH_LONG).show();
                try {
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                        Users userObj = userSnap.getValue(Users.class);
                        Log.e("User Snapshot: ", userId + "\n" + userObj.getUserId());
                        if (userObj.getUserId().equals(userId)) {
                            String userId = userObj.getUserId();
                            String userNombre = userObj.getNombre();
                            String userGenero = userObj.getGenero();
                            int userRangoEdad = userObj.getRangoEdad();
                            int userTipo = userObj.getTipo();
                            String userEmail = userObj.getEmail();

                            fillUserInformation(userId, userNombre, userGenero, userRangoEdad, userTipo, userEmail);

                            break;
                        }
                    }

                } catch (Exception ex) {
                    Log.e("UserSnapshot", "Error snapshot " + ex.getMessage());
                    //Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.tipoSpin = (Spinner) findViewById(R.id.spinFiltro);
        this.rangoSpin = (Spinner) findViewById(R.id.spinRango);
        this.generoBox = (CheckBox) findViewById(R.id.checkGenero);
        this.edadBox = (CheckBox) findViewById(R.id.checkEdad);
        this.gridMain = (GridLayout) findViewById(R.id.gridRecomendaciones);

        ArrayAdapter<String> generoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.tipoLista);
        ArrayAdapter<String> edadedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.rangoLista);
        this.tipoSpin.setAdapter(generoAdapter);
        this.rangoSpin.setAdapter(edadedAdapter);

        this.tipoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoSeleccionado = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.rangoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rangoSeleccionado = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*Inicialización para Recomendaciones*/
        this.imagen1 = (ImageView)findViewById(R.id.imagen1); this.nombre1 = (TextView) findViewById(R.id.nombre1);
        this.direccion1 = (TextView) findViewById(R.id.direccion1); this.rating1 = (TextView) findViewById(R.id.rating1);

        this.imagen2 = (ImageView)findViewById(R.id.imagen2); this.nombre2 = (TextView) findViewById(R.id.nombre2);
        this.direccion2 = (TextView) findViewById(R.id.direccion2); this.rating2 = (TextView) findViewById(R.id.rating2);

        this.imagen3 = (ImageView)findViewById(R.id.imagen3); this.nombre3 = (TextView) findViewById(R.id.nombre3);
        this.direccion3 = (TextView) findViewById(R.id.direccion3); this.rating3 = (TextView) findViewById(R.id.rating3);

        this.imagen4 = (ImageView)findViewById(R.id.imagen4); this.nombre4 = (TextView) findViewById(R.id.nombre4);
        this.direccion4 = (TextView) findViewById(R.id.direccion4); this.rating4 = (TextView) findViewById(R.id.rating4);

        this.imagen5 = (ImageView)findViewById(R.id.imagen5); this.nombre5 = (TextView) findViewById(R.id.nombre5);
        this.direccion5 = (TextView) findViewById(R.id.direccion5); this.rating5 = (TextView) findViewById(R.id.rating5);

        this.imagen6 = (ImageView)findViewById(R.id.imagen6); this.nombre6 = (TextView) findViewById(R.id.nombreGoogle1);
        this.direccion6 = (TextView) findViewById(R.id.direccionGoogle1); this.rating6 = (TextView) findViewById(R.id.ratingGoogle1);

        this.imagen7 = (ImageView)findViewById(R.id.imagen7); this.nombre7 = (TextView) findViewById(R.id.nombreGoogle2);
        this.direccion7 = (TextView) findViewById(R.id.direccionGoogle2); this.rating7 = (TextView) findViewById(R.id.ratingGoogle2);

        this.imagen8 = (ImageView)findViewById(R.id.imagen8); this.nombre8 = (TextView) findViewById(R.id.nombreGoogle3);
        this.direccion8 = (TextView) findViewById(R.id.direccionGoogle3); this.rating8 = (TextView) findViewById(R.id.ratingGoogle3);

        this.imagen9 = (ImageView)findViewById(R.id.imagen9); this.nombre9 = (TextView) findViewById(R.id.nombreGoogle4);
        this.direccion9 = (TextView) findViewById(R.id.direccionGoogle4); this.rating9 = (TextView) findViewById(R.id.ratingGoogle4);

        this.imagen10 = (ImageView)findViewById(R.id.imagen10); this.nombre10 = (TextView) findViewById(R.id.nombreGoogle5);
        this.direccion10 = (TextView) findViewById(R.id.direccionGoogle5); this.rating10 = (TextView) findViewById(R.id.ratingGoogle5);

        this.gridGoogle1 = (GridLayout) findViewById(R.id.google1); this.gridGoogle1.setVisibility(View.INVISIBLE);
        this.gridGoogle2 = (GridLayout) findViewById(R.id.google2); this.gridGoogle2.setVisibility(View.INVISIBLE);
        this.gridGoogle3 = (GridLayout) findViewById(R.id.google3); this.gridGoogle3.setVisibility(View.INVISIBLE);
        this.gridGoogle4 = (GridLayout) findViewById(R.id.google4); this.gridGoogle4.setVisibility(View.INVISIBLE);
        this.gridGoogle5 = (GridLayout) findViewById(R.id.google5); this.gridGoogle5.setVisibility(View.INVISIBLE);

        this.recomend1 = (GridLayout) findViewById(R.id.resultado1);
        this.recomend2 = (GridLayout) findViewById(R.id.resultado2);
        this.recomend3 = (GridLayout) findViewById(R.id.resultado3);
        this.recomend4 = (GridLayout) findViewById(R.id.resultado4);
        this.recomend5 = (GridLayout) findViewById(R.id.resultado5);

        prDialog = new ProgressDialog(this); // Crear un dialogo para mostrar progreso
        prDialog.setCancelable(false);
    }

    /**
     * Metodo que verifica si el servicio de gps se encuentra activado en el telefono
     *
     * @return true si el gps esta activado, false en caso contrario
     */
    private boolean gpsEstaActivado() {
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context); // Obtener el servicio de localizacion

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Verifica si el servicio de localizacion esa disponible
    }

    public void fillUserInformation(String userId, String nombre, String genero, int rangoEdad, int tipo, String email) {
        Toast.makeText(getApplicationContext(), "Fill user info not null", Toast.LENGTH_LONG).show();
        this.userInfo = new Users(userId, nombre, genero, rangoEdad, tipo, email);
        Log.e("User info", "User info not null");
    }

    /**
     * Metodo que se llama cada vez que la vista se hace visible para el usuario
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (gpsEstaActivado()) // Comprueba si el gps del dispositivo se encuentra activado
            mGoogleApiClient.connect(); // Conecta con el servicio de ubicación de Google
    }

    /**
     * Metodo que se llama al cambiar a una nueva vista o al cerrar la aplicación
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect(); // Detiene la conexión con el servicio de Google
        }
    }

    /**
     * Una vez que se conectó con los servicios de ubicación de google, obtiene las coordenadas de la posición actual
     *
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); // Obtiene la última localización conocida del dispositivo
        if (mLastLocation != null) {
            this.latitud = mLastLocation.getLatitude(); // Asignar latitud a variable de clase
            this.longitud = mLastLocation.getLongitude(); // Asignar longitud a variable de clase
        }
    }
    
    /**
     * Mensaje de error en caso de que la conexión falle
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(getApplicationContext(), "No se pudieron obtener las coordenadas", Toast.LENGTH_LONG).show();
        //Log.d("ActividadPrincipal", "No se pudieron obtener las coordenadas");
    }

    /**
     * Mensaje de alerta en caso de que la conexión se haya suspendido
     *
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        //Log.d("ActividadPrincipal", "Conexión suspendida");
        //mGoogleApiClient.connect();
    }
    
    /**
     * Metodo que muestra una alerta solicitando la activacion del servicio de ubicación
     */
    public boolean solicitarActivacionGPS() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Construye la alerta. Se coloca mensaje, botón para confirmación, y cancelación.
            builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create(); // Crear alerta
            alert.show(); // Mostrar alerta
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public void buscarRecomendacion(View v) {
        //this.gridMain.setVisibility(View.INVISIBLE);
        flag = false;
        this.placesHash = new HashMap();
        //Toast.makeText(getApplicationContext(), "Entrando a tipo y rango...", Toast.LENGTH_LONG).show();
        this.tipoLugar = getTipoLugar(this.tipoSeleccionado);
        this.rangoBusqueda = getRangoBusqueda(this.rangoSeleccionado);

        obtenerResultadosSimilares(this.tipoLugar, this.rangoBusqueda);

        prDialog.setMessage("Obteniendo lista de recomendaciones..."); // Asignar mensaje al dialogo de progreso
        prDialog.show(); // Mostrar dialogo de progreso
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                mostrarRecomendaciones();
                prDialog.hide();
            }
        }, 15000);
    }
    
    public synchronized void obtenerDetallesLugar(String placeId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("placeid=").append(placeId);
            googlePlacesUrl.append("&key=").append(API_KEY);
            //Log.e("Detalles de lugar", googlePlacesUrl.toString());
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
    
     public synchronized void obtenerResultadosSimilares(String tipo, String rango) {
        RequestQueue queue = Volley.newRequestQueue(this);
        this.listaCercanos = new LinkedList();
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            //location=51.503186,-0.126446&radius=5000&types=hospital&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("location=").append(this.latitud).append(",").append(this.longitud);
            googlePlacesUrl.append("&radius=").append(rango);
            googlePlacesUrl.append("&type=").append(tipo);
            googlePlacesUrl.append("&key=").append(API_KEY);
            Log.e("Cercanos Tipo: ", googlePlacesUrl.toString());
            final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("ResponseSimilares", "...");
                            obtenerListaCercanos(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(RecomendacionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(detailsRequest);

        } catch(Exception e) {
            Log.e("NearbySearch: ", e.getMessage());
            //Toast.makeText(RecomendacionActivity.this, "Error en string NearbySearch", Toast.LENGTH_LONG).show();
        }
    }
    
    public void obtenerInfoLugar(JSONObject result) {
        InfoPlace lugarActual = new InfoPlace();
        JSONObject place = null;
        try {
            JSONObject jsonObj = result.getJSONObject("result");

            lugarActual.setName(jsonObj.getString("name"));
            lugarActual.setPlaceId(jsonObj.getString("place_id"));

            JSONObject geometry = jsonObj.getJSONObject("geometry").getJSONObject("location");
            LatLng coordinates = new LatLng(geometry.getDouble("lat"), geometry.getDouble("lng"));
            lugarActual.setLatlng(coordinates);

            lugarActual.setPlaceTypes(jsonObj.getJSONArray("types"));

            boolean flagType = false;
            for(int i = 0; i < lugarActual.getPlaceTypes().length(); i++) {
                if (lugarActual.getPlaceTypes().get(i).equals(this.tipoLugar)) {
                    flagType = true;
                }
            }
            if(!flagType) {
                return;
            }
            lugarActual.setRating(jsonObj.getDouble("rating"));

            JSONArray photos = jsonObj.getJSONArray("photos");
            JSONObject photoRef = photos.getJSONObject(0);
            String ref = photoRef.getString("photo_reference");

            lugarActual.setDireccion(jsonObj.getString("formatted_address"));

            lugarActual.setPhoto(ref);
            this.listaDB.addLast(lugarActual);
            Log.e("Place added", lugarActual.getName()+" - "+lugarActual.getRating());
        } catch(Exception e) {
            Log.e("InfoLugar", e.getMessage());
            //e.printStackTrace();
            return;
        }
    }
    
    public void obtenerListaCercanos(JSONObject result) {
        try {
            JSONArray jsonArray = result.getJSONArray("results");  
            for(int i = 0; i < jsonArray.length(); i++) {
                try {
                    InfoPlace lugarActual = new InfoPlace();

                    JSONObject place = jsonArray.getJSONObject(i);
                    lugarActual.setName(place.getString("name"));

                    JSONObject geometry = place.getJSONObject("geometry").getJSONObject("location");
                    LatLng coordenadas = new LatLng(geometry.getDouble("lat"), geometry.getDouble("lng"));
                    lugarActual.setLatlng(coordenadas);

                    lugarActual.setRating(place.getDouble("rating"));

                    JSONArray photos = place.getJSONArray("photos");
                    JSONObject photoRef = photos.getJSONObject(0);
                    String ref = photoRef.getString("photo_reference");

                    lugarActual.setDireccion(place.getString("vicinity"));
                    lugarActual.setPlaceId(place.getString("place_id"));
                    lugarActual.setPhoto(ref);

                    this.listaCercanos.addLast(lugarActual);
                    Log.e("Cercanos added", lugarActual.getName() + " - " + lugarActual.getRating());
                } catch(Exception e) {
                    Log.e("Lista Cercanos", "Error " + e.getMessage());
                }
            }
                                                
            obtenerRecomendacionesDB();
        } catch(Exception e) {
            Log.e("Lista Cercanos", "Error " + e.getMessage());
            //Toast.makeText(RecomendacionActivity.this, "Error al obtener información de lugares cercanos", Toast.LENGTH_LONG).show();
        }
    }
                                                
    public synchronized void obtenerRecomendacionesDB() {
        this.listaDB = new LinkedList();
        mFirebaseDatabaseReference.child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comments infoComentario = new Comments();
                if(dataSnapshot != null) {
                    for(DataSnapshot comentarioSnap : dataSnapshot.getChildren()) {
                        try {
                            infoComentario = comentarioSnap.getValue(Comments.class);
                        } catch(Exception e) {
                            //Toast.makeText(RecomendacionActivity.this, "Comments null", Toast.LENGTH_LONG).show();
                        }
                        try {
                            String actualPlaceId = "";
                            if(generoCheck) {
                                if(edadCheck) {
                                    if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3
                                            && infoComentario.getTipoUsuario().equals(userInfo.getTipo())) {
                                        if (!infoComentario.getUserId().equals(user.getUid())) {
                                            actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                            if(actualPlaceId == null) {
                                                placesHash.put(infoComentario.getPlaceId()+"", infoComentario.getPlaceId()+"");
                                                obtenerDetallesLugar(infoComentario.getPlaceId());
                                            }
                                        }
                                    }
                                } else {
                                    switch (infoComentario.getTipoUsuario()) {
                                        case "1":
                                        case "2":
                                        case "3":
                                        case "4":
                                            if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 && (infoComentario.getTipoUsuario().equals("4")
                                                    || infoComentario.getTipoUsuario().equals("3")) || infoComentario.getTipoUsuario().equals("2")
                                                    || infoComentario.getTipoUsuario().equals("1")) {
                                                if (!infoComentario.getUserId().equals(user.getUid())) {
                                                    actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                    if(actualPlaceId == null) {
                                                        placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                        obtenerDetallesLugar(infoComentario.getPlaceId());
                                                    }
                                                }
                                            }
                                            break;
                                        case "5":
                                        case "6":
                                        case "7":
                                        case "8":
                                            if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 && (infoComentario.getTipoUsuario().equals("5")
                                                    || infoComentario.getTipoUsuario().equals("6")) || infoComentario.getTipoUsuario().equals("7")
                                                    || infoComentario.getTipoUsuario().equals("8")) {
                                                if (!infoComentario.getUserId().equals(user.getUid())) {
                                                    actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                    if(actualPlaceId == null) {
                                                        placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                        obtenerDetallesLugar(infoComentario.getPlaceId());
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                }
                            } else if(edadCheck) {
                                switch (infoComentario.getTipoUsuario()) {
                                    case "1":
                                    case "5":
                                        if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 &&
                                                (infoComentario.getTipoUsuario().equals("1")) || infoComentario.getTipoUsuario().equals("5")) {
                                            if (!infoComentario.getUserId().equals(user.getUid())) {
                                                actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                if(actualPlaceId == null) {
                                                    placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                    obtenerDetallesLugar(infoComentario.getPlaceId());
                                                }
                                            }
                                        }
                                        break;
                                    case "2":
                                    case "6":
                                        if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 &&
                                                (infoComentario.getTipoUsuario().equals("2")) || infoComentario.getTipoUsuario().equals("6")) {
                                            if (!infoComentario.getUserId().equals(user.getUid())) {
                                                actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                if(actualPlaceId == null) {
                                                    placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                    obtenerDetallesLugar(infoComentario.getPlaceId());
                                                }
                                            }
                                        }
                                        break;
                                    case "3":
                                    case "7":
                                        if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 &&
                                                (infoComentario.getTipoUsuario().equals("3")) || infoComentario.getTipoUsuario().equals("7")) {
                                            if (!infoComentario.getUserId().equals(user.getUid())) {
                                                actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                if(actualPlaceId == null) {
                                                    placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                    obtenerDetallesLugar(infoComentario.getPlaceId());
                                                }
                                            }
                                        }
                                        break;
                                    case "4":
                                    case "8":
                                        if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3 &&
                                                (infoComentario.getTipoUsuario().equals("4")) || infoComentario.getTipoUsuario().equals("8")) {
                                            if (!infoComentario.getUserId().equals(user.getUid())) {
                                                actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                                if(actualPlaceId == null) {
                                                    placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                                    obtenerDetallesLugar(infoComentario.getPlaceId());
                                                }
                                            }
                                        }
                                        break;
                                }
                            } else if(!generoCheck && !edadCheck) {
                                Log.e("Tipo:", "No genero, no edad");
                                if (Integer.parseInt(infoComentario.getCalifEmociones()) >= 3) {
                                    if (!infoComentario.getUserId().equals(user.getUid())) {
                                        actualPlaceId = (String)placesHash.get(infoComentario.getPlaceId());
                                        if(actualPlaceId == null) {
                                            placesHash.put(infoComentario.getPlaceId() + "", infoComentario.getPlaceId() + "");
                                            obtenerDetallesLugar(infoComentario.getPlaceId());
                                        }
                                    }
                                }
                            }

                        } catch(Exception e) {
                            Log.e("InfoComentario", e.getMessage());
                            infoComentario.toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecomendacionActivity.this, "Búsqueda de recomendaciones cancelada", Toast.LENGTH_LONG).show();
            }
        });
    }
                               
   public synchronized void mostrarRecomendaciones() {
       LinkedList finalRecomendaciones = null;
       LinkedList finalGoogle = null;
       try {
           Log.e("Recomendaciones","...");
           finalRecomendaciones = sortLinkedList(this.listaDB);
           Log.e("Google","...");
           finalGoogle = sortLinkedList(this.listaCercanos);
       } catch(Exception e) {
           Log.e("FinalRecomendaciones", "Entrando en recomendaciones");
       }

       if (finalRecomendaciones != null && !finalRecomendaciones.isEmpty()) {
           try {
               Log.e("FinalRecomendaciones", "Entrando en recomendaciones");
               InfoPlace actual = new InfoPlace();

               actual = (InfoPlace) finalRecomendaciones.get(0);
               String photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen1);
               final String actualPlace1 = actual.getPlaceId();
               this.nombre1.setText(actual.getName());
               this.direccion1.setText(actual.getDireccion().toString());
               this.rating1.setText(actual.getRating() + "");
               this.recomend1.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                        iniciarDetalles(actualPlace1);
                   }
               });

               actual = (InfoPlace) finalRecomendaciones.get(1);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen2);
               final String actualPlace2 = actual.getPlaceId();
               this.nombre2.setText(actual.getName());
               this.direccion2.setText(actual.getDireccion().toString());
               this.rating2.setText(actual.getRating() + "");
               this.recomend2.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace2);
                   }
               });

               actual = (InfoPlace) finalRecomendaciones.get(2);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen3);
               final String actualPlace3 = actual.getPlaceId();
               this.nombre3.setText(actual.getName());
               this.direccion3.setText(actual.getDireccion().toString());
               this.rating3.setText(actual.getRating() + "");
               this.recomend3.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace3);
                   }
               });

               actual = (InfoPlace) finalRecomendaciones.get(3);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen4);
               final String actualPlace4 = actual.getPlaceId();
               this.nombre4.setText(actual.getName());
               this.direccion4.setText(actual.getDireccion().toString());
               this.rating4.setText(actual.getRating() + "");
               this.recomend4.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace4);
                   }
               });

               actual = (InfoPlace) finalRecomendaciones.get(4);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen5);
               final String actualPlace5 = actual.getPlaceId();
               this.nombre5.setText(actual.getName());
               this.direccion5.setText(actual.getDireccion().toString());
               this.rating5.setText(actual.getRating() + "");
               this.recomend5.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace5);
                   }
               });

               actual = (InfoPlace) finalGoogle.get(0);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen6);
               final String actualPlace6 = actual.getPlaceId();
               this.nombre6.setText(actual.getName());
               this.direccion6.setText(actual.getDireccion().toString());
               this.rating6.setText(actual.getRating() + "");
               this.gridGoogle1.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace6);
                   }
               });

               actual = (InfoPlace) finalGoogle.get(1);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen7);
               final String actualPlace7 = actual.getPlaceId();
               this.nombre7.setText(actual.getName());
               this.direccion7.setText(actual.getDireccion().toString());
               this.rating7.setText(actual.getRating() + "");
               this.gridGoogle2.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace7);
                   }
               });

               actual = (InfoPlace) finalGoogle.get(2);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen8);
               final String actualPlace8 = actual.getPlaceId();
               this.nombre8.setText(actual.getName());
               this.direccion8.setText(actual.getDireccion().toString());
               this.rating8.setText(actual.getRating() + "");
               this.gridGoogle3.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace8);
                   }
               });

               actual = (InfoPlace) finalGoogle.get(3);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen9);
               final String actualPlace9 = actual.getPlaceId();
               this.nombre9.setText(actual.getName());
               this.direccion9.setText(actual.getDireccion().toString());
               this.rating9.setText(actual.getRating() + "");
               this.gridGoogle4.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace9);
                   }
               });

               actual = (InfoPlace) finalGoogle.get(4);
               photoRef = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+actual.getPhoto()+"&key="+API_KEY;
               Picasso.with(getApplicationContext()).load(photoRef).into(this.imagen10);
               final String actualPlace10 = actual.getPlaceId();
               this.nombre10.setText(actual.getName());
               this.direccion10.setText(actual.getDireccion().toString());
               this.rating10.setText(actual.getRating() + "");
               this.gridGoogle5.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       iniciarDetalles(actualPlace10);
                   }
               });
           } catch(Exception e) {
               Log.e("Error DB empty", "DB empty");
           }
       } else {
           try {
               InfoPlace actual;

               actual = (InfoPlace) this.listaDB.get(0);
               this.nombre1.setText(actual.getName());
               this.direccion1.setText(actual.getLatlng().toString());
               this.rating1.setText(actual.getRating() + "");
           } catch(Exception e) {
               Log.e("Else actual", "Error: "+e.getMessage());
           }
       }
   }

   public void iniciarDetalles(String placeId) {
       Intent detallesIntent = new Intent(RecomendacionActivity.this, DetallesActivity.class);
       detallesIntent.putExtra("placeId", placeId);
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
           e.printStackTrace();
           return null;
       }
   }
    
    public String getTipoLugar(String tipoLugar) {
        String tipo = "";
        Log.e("Tipo lugar", tipoLugar);
        //Toast.makeText(getApplicationContext(), "Tipo de lugar: " + tipoLugar, Toast.LENGTH_LONG).show();
        //"Hospital", "Librería", "Bar", "Café", "Museo", "Escuela", "Banco", "Restaurante", "Tienda", "Alojamiento"
        try {
            switch (tipoLugar) {
                case "Hospital":
                    tipo = "hospital";
                    break;
                case "Librería":
                    tipo = "library";
                    break;
                case "Bar":
                    tipo = "bar";
                    break;
                case "Café":
                    tipo = "cafe";
                    break;
                case "Museo":
                    tipo = "museum";
                    break;
                case "Escuela":
                    tipo = "school";
                    break;
                case "Banco":
                    tipo = "bank";
                    break;
                case "Restaurante":
                    tipo = "restaurant";
                    break;
                case "Tienda":
                    tipo = "store";
                    break;
                case "Alojamiento":
                    tipo = "lodging";
                    break;
                default:
                    tipo = "";
                    break;
            }
        } catch(Exception e) {
            Log.e("Error tipo", e.getMessage());
        }
        Log.e("Tipo de lugar", tipo);
        return tipo;
    }
    
    public String getRangoBusqueda(String rangoBusqueda) {
        String rango = "";
        //"0.5km", "1km", "1.5kms", "2kms", "2.5kms", "3kms", "3.5kms", "4kms", "4.5kms", "5kms"
        switch(rangoBusqueda) {
            case "0.5km":
                rango = "500";
                break;
            case "1km":
                rango = "1000";
                break;
            case "1.5kms":
                rango = "1500";
                break;
            case "2kms":
                rango = "2000";
                break;
            case "2.5kms":
                rango = "2500";
                break;
            case "3kms":
                rango = "3000";
                break;
            case "3.5kms":
                rango = "3500";
                break;
            case "4kms":
                rango = "4000";
                break;
            case "4.5kms":
                rango = "4500";
                break;
            case "5kms":
            default:
                rango = "5000";
                break;
        }
        return rango;
    }

    public void onGeneroChecked(View v) {
        if(generoCheck) {
            generoCheck = false;
        } else {
            generoCheck = true;
        }
    }

    public void onEdadChecked(View v) {
        if(edadCheck) {
            edadCheck = false;
        } else {
            edadCheck = true;
        }
    }

    public void showRecomendaciones(View v) {
        Log.e("Sistema", "Mostando...");
        this.recomend1.setVisibility(View.VISIBLE);
        this.recomend2.setVisibility(View.VISIBLE);
        this.recomend3.setVisibility(View.VISIBLE);
        this.recomend4.setVisibility(View.VISIBLE);
        this.recomend5.setVisibility(View.VISIBLE);

        this.gridGoogle1.setVisibility(View.GONE);
        this.gridGoogle2.setVisibility(View.GONE);
        this.gridGoogle3.setVisibility(View.GONE);
        this.gridGoogle4.setVisibility(View.GONE);
        this.gridGoogle5.setVisibility(View.GONE);

    }

    public void showGoogle(View v) {
        Log.e("Google", "Mostando...");
        this.recomend1.setVisibility(View.GONE);
        this.recomend2.setVisibility(View.GONE);
        this.recomend3.setVisibility(View.GONE);
        this.recomend4.setVisibility(View.GONE);
        this.recomend5.setVisibility(View.GONE);

        this.gridGoogle1.setVisibility(View.VISIBLE);
        this.gridGoogle2.setVisibility(View.VISIBLE);
        this.gridGoogle3.setVisibility(View.VISIBLE);
        this.gridGoogle4.setVisibility(View.VISIBLE);
        this.gridGoogle5.setVisibility(View.VISIBLE);
    }
        
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
                case REQUEST_ACCESS_FINE_LOCATION: {
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permisos de ubicación concedidos", Toast.LENGTH_LONG).show();        
                    }
                    return;
                }
                    
        }
    }
}

package com.example.xxfin.tesinarecommendationsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xxfin.tesinarecommendationsystem.Objects.Comments;
import com.example.xxfin.tesinarecommendationsystem.Objects.InfoPlace;
import com.google.android.gms.maps.model.LatLng;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class RecomendacionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*Local Variables*/
    private String email = "";
    private String userId = "";
    private String tipoLista[] = {"Hospital", "Library", "Bar", "Cafe", "Museo", "Escuela", "Banco", "Restaurante", "Tienda", "Alojamiento"};
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
    private String tipoLugar = "";
    private String rangoBusqueda = "";

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
                Toast.makeText(getApplicationContext(), "User DataSnapshot", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

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
        tipoSpin.setAdapter(generoAdapter);
        rangoSpin.setAdapter(edadedAdapter);
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
        try {
            String tipoLugar = this.tipoSpin.getSelectedItem().toString();
            String rangoBusqueda = this.rangoSpin.getSelectedItem().toString();
            boolean generoSelected = this.generoBox.isSelected();
            boolean edadSelected = this.edadBox.isSelected();
            String genero = this.userInfo.getGenero();
            int edad = this.userInfo.getRangoEdad();
        } catch(Exception e) {
            Log.e("Recomendación error", e.getMessage());
        }
        
        this.tipoLugar = getTipoLugar(tipoLugar);
        this.rangoBusqueda = getRangoBusqueda(rangoBusqueda);

        obtenerResultadosSimilares(this.tipoLugar, this.rangoBusqueda);
    }
    
    public void obtenerDetallesLugar(String placeId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("placeid=").append(placeId);
            googlePlacesUrl.append("&key=").append(API_KEY);

            final JsonObjectRequest placeRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(DetectFacesActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            obtenerInfoLugar(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RecomendacionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(placeRequest);
        } catch(Exception e) {
            Log.e("DETALLES LUGAR", e.getMessage());
        }
    }
    
     public void obtenerResultadosSimilares(String tipo, String rango) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            //location=51.503186,-0.126446&radius=5000&types=hospital&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("location=").append(this.latitud).append(",").append(this.longitud);
            googlePlacesUrl.append("&radius=").append(rango);
            googlePlacesUrl.append("&type=").append(tipo);
            googlePlacesUrl.append("&key=").append(API_KEY);
            Log.e("Query Similares", googlePlacesUrl.toString());
            final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(DetectFacesActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            Log.e("ResponseSimilares", "...");
                            obtenerListaCercanos(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RecomendacionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(detailsRequest);
        } catch(Exception e) {
            Log.e("NearbySearch: ", e.getMessage());
            Toast.makeText(RecomendacionActivity.this, "Error en string NearbySearch", Toast.LENGTH_LONG).show();
        }
    }
    
    public void obtenerInfoLugar(JSONObject result) {
        try {
            InfoPlace lugarActual = new InfoPlace();
            
            JSONArray jsonArray = result.getJSONArray("result");
            JSONObject place = jsonArray.getJSONObject(0);

            lugarActual.setName(place.getString("name"));
            lugarActual.setPlaceId(place.getString("place_id"));

            JSONObject geometry = place.getJSONObject("geometry").getJSONObject("location");
            LatLng coordinates = new LatLng(geometry.getDouble("latitude"), geometry.getDouble("longitude"));
            lugarActual.setLatlng(coordinates);
            
            lugarActual.setPlaceTypes(place.getJSONArray("types"));
            lugarActual.setRating(place.getDouble("rating"));

            this.listaDB.addLast(lugarActual);
            Log.e("Place added", lugarActual.getPlaceId());
            //Toast.makeText(DetectFacesActivity.this, "Información del lugar lista", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Log.e("InfoLugar", e.getMessage());
            Toast.makeText(RecomendacionActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    
    public void obtenerListaCercanos(JSONObject result) {
        try {
            JSONArray jsonArray = result.getJSONArray("results");  
            for(int i = 0; i < jsonArray.length(); i++) {
                InfoPlace lugarActual = new InfoPlace();
                
                JSONObject place = jsonArray.getJSONObject(i);
                lugarActual.setName(place.getString("name"));
                
                JSONObject geometry = place.getJSONObject("geometry").getJSONObject("location");
                LatLng coordenadas = new LatLng(geometry.getDouble("lat"), geometry.getDouble("lng"));
                lugarActual.setLatlng(coordenadas);
                                                
                lugarActual.setPlaceId(place.getString("place_id"));

                this.listaCercanos.addLast(lugarActual);
            }
                                                
            obtenerRecomendacionesDB();
        } catch(Exception e) {
            Log.e("Lista Cercanos", "Error " + e.getMessage());
            Toast.makeText(RecomendacionActivity.this, "Error al obtener información de lugares cercanos", Toast.LENGTH_LONG).show();
        }
    }
                                                
    public void obtenerRecomendacionesDB() {
        mFirebaseDatabaseReference.child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comments infoComentario = new Comments();
                if(dataSnapshot != null) {
                    for(DataSnapshot comentarioSnap : dataSnapshot.getChildren()) {
                        try {
                            infoComentario = comentarioSnap.getValue(Comments.class);
                        } catch(Exception e) {
                            Log.e("Comentario null", e.getMessage());
                            Toast.makeText(RecomendacionActivity.this, "Comments null", Toast.LENGTH_LONG).show();
                        }
                        try {
                            if (infoComentario.getTipoUsuario().equals(userInfo.getTipo())) {
                                if (!infoComentario.getUserId().equals(user.getUid())) {
                                    Log.e("DETALLES", "Entrando en detalles de " + infoComentario.getUserId());
                                    obtenerDetallesLugar(infoComentario.getPlaceId());
                                }
                            }
                        } catch(Exception e) {
                            Log.e("InfoComentario", e.getMessage());
                            infoComentario.toString();
                        }
                    }

                    mostrarRecomendaciones();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecomendacionActivity.this, "Búsqueda de recomendaciones cancelada", Toast.LENGTH_LONG).show();
            }
        });
    }
                               
   public void mostrarRecomendaciones() {
       LinkedList finalRecomendaciones = new LinkedList();

       if(this.listaDB.isEmpty()) {
           for (int i = 0; i < this.listaCercanos.size(); i++) {
               InfoPlace actualPlace = (InfoPlace) this.listaCercanos.get(i);
               Log.e("Recomendaciones_Finales", actualPlace.getPlaceId());
           }
       } else {
           for (int i = 0; i < this.listaCercanos.size(); i++) {
               InfoPlace actualPlace = (InfoPlace) this.listaCercanos.get(i);
               for (int j = 0; j < this.listaDB.size(); j++) {
                   InfoPlace auxPlace = (InfoPlace) this.listaDB.get(j);
                   if (actualPlace.getPlaceId().equals(auxPlace.getPlaceId())) {
                       finalRecomendaciones.addLast(actualPlace);
                       Log.e("Recomendaciones_Finales", actualPlace.toString());
                       break;
                   }
               }
           }
       }
   }
    
    public String getTipoLugar(String tipoLugar) {
        String tipo = "";
        //"Hospital", "Librería", "Bar", "Café", "Museo", "Escuela", "Banco", "Restaurante", "Tienda", "Alojamiento"
        switch(tipoLugar) {
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

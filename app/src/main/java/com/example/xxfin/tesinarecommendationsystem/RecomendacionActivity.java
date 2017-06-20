package com.example.xxfin.tesinarecommendationsystem;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import com.example.xxfin.tesinarecommendationsystem.Objects.Users;

public class RecomendacionActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion);

        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");

        /*Crea cliente de para la localización*/
        crearClienteLocalizacion();
        
        if(!gpsEstaActivado()) {
            solicitarActivacionGPS();
        }
        
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        
        mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                        Users userObj = userSnap.getValue(Users.class);
                        if(userObj.getUserId().equals(user.getUid())) {
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
                    Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Spinner tipoSpin = (Spinner) findViewById(R.id.spinFiltro);
        Spinner rangoSpin = (Spinner) findViewById(R.id.spinRango);

        ArrayAdapter<String> generoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.tipoLista);
        ArrayAdapter<String> edadedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.rangoLista);
        tipoSpin.setAdapter(generoAdapter);
        rangoSpin.setAdapter(edadedAdapter);
    }
    
    public void fillUserInformation(String userId, String nombre, String genero, int rangoEdad, int tipo, String email) {
        this.userInfo = new Users(userId, nombre, genero, rangoEdad, tipo, email);
    }
    
    /**
     * Metodo que se llama cada vez que la vista se hace visible para el usuario
     */
    @Override
    protected void onStart() {
        super.onStart();

        if(gpsEstaActivado()) // Comprueba si el gps del dispositivo se encuentra activado
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
        Toast.makeText(getApplicationContext(), "No se pudieron obtener las coordenadas", Toast.TOAST_LONG),.show();
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
        String tipoLugar = (Spinner) findViewById(R.id.spinFiltro).getSelectedItem().toString();
        String rangoBusqueda = (Spinner) findViewById(R.id.spinRango).getSelectedItem().toString();
        
        tipoLugar = getTipoLugar(tipoLugar);
        rangoBusqueda = getRangoBusqueda(rangoBusqueda);
    }
    
    public void obtenerDetallesLugar(String placeId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        //placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
        googlePlacesUrl.append("placeid=").append(placeId);
        googlePlacesUrl.append("&key=").append(API_KEY);

        final JsonObjectRequest placeRequest = new JsonObjectRequest (
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
                Toast.makeText(DetectFacesActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(placeRequest);
    }
    
     public void obtenerResultadosSimilares(String tipo, String rango) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            //location=51.503186,-0.126446&radius=5000&types=hospital&key=AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE
            googlePlacesUrl.append("location=").append(this.latitud).append(",").append(this.longitud);
            googlePlacesUrl.append("&radious=").append(rango);
            googlePlacesUrl.append("&type=").append(tipo);
            googlePlacesUrl.append("&key=").append(API_KEY);

            final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(DetectFacesActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            obtenerListaCercanos(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetectFacesActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(detailsRequest);
        } catch(Exception e) {
            Toast.makeText(DetectFacesActivity.this, "Error en string NearbySearch", Toast.LENGTH_LONG).show();
        }
    }
    
    public void obtenerInfoLugar(JSONObject result) {
        try {
            InfoLugar lugarActual = new InfoLugar();
            
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
            //Toast.makeText(DetectFacesActivity.this, "Información del lugar lista", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(DetectFacesActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    
    public void obtenerListaCercanos(JSONObject result) {
        try {
            JSONArray jsonArray = result.getJSONArray("results");  
            for(int i = 0; i < jsonArray.lenght(); i++) {
                InfoLugar lugarActual = new InfoLugar();
                
                JSONObject place = jsonArray.getJSONObject(i);
                lugarActual.setNombre(place.getString("name"));
                
                JSONObject geometry = place.getJSONObject("geometry").getJSONObject("location");
                LatLng coordenadas = new LatLng(geometry.getDouble("latitude", geometry.getDouble("longitude"));
                lugarActual.setLatlng(coordenadas);
                                                
                lugarActual.setPlaceId(place.getString("place_id"));
                
                this.listaCercanos.addLast(lugarActual);
            }
                                                
            obtenerRecomendacionesDB();
        } catch(Exception e) {
            Toast.makeText(RecomendacionActivity.this, "Error al obtener información de lugares cercanos", Toast.LENGHT.LONG).show();  
        }
    }
                                                
    public void obtenerRecomendacionesDB() {
        mFirebaseReference.child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null && dataSnapshot != null) {
                    try {
                        for(DataSnapshot comentarioSnap : dataSnapshot.getChildren()) {
                            Comments infoComentario = comentarioSnap.getValue(Comments.class);
                            if(infoCommentario.getTipoUsuario().equals(this.userInfo.getTipo()) {
                                if(!infoComentario.getUserId().equals(user.getUid()) {
                                   obtenerDetallesLugar(infoComentario.getPlaceId());
                                }    
                            }
                        }
                                   
                        mostrarRecomendaciones();
                    } catch(Exception e) {
                        Toast.makeText(RecomendacionActivity.this, "Error al obtener recomendaciones de la base de datos", Toast.LENGHT_LONG).show();   
                    }
                }
            }
        });
    }
                               
   public void mostrarRecomendaciones() {
       LinkedList finalRecomendaciones = new LinkedList();
       
       for(int i = 0; i < this.listaCercanos.size(); i++) {
           InfoPlace actualPlace = (InfoPlace) this.listaCercanos.get(i);
           for(int j = 0; j < this.listaDB.size(); j++) {
               InfoPlace auxPlace = (InfoPlace) this.listaDB.get(j);
               if(actualPlace.getPlaceId.equals(auxPlace.getPlaceId()) {
                   finalRecomendaciones.addLast(actualPlace);
                   Log.e("Recomendaciones_Finales", actualPlace.toString());
                   break;
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
                tipo = "school"
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
}

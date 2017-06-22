package com.example.xxfin.tesinarecommendationsystem;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.example.xxfin.tesinarecommendationsystem.Objects.Comments;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.BitmapFactory.decodeFile;

public class ComentarioActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*Local Variables*/
    private double latitud = 0;
    private double longitud = 0;
    private Bitmap imageBitmap;
    private int imageWidth;
    private int imageHeight;
    private String rutaImagen;
    private Uri uriImagen;
    private String email = "";
    private String userId = "";
    private String lastimgdatetime;
    private Bitmap photo;
    private String gustarList[] = {"Si", "No"};
    private String primeraList[] = {"Si", "No"};

    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;

    private DatabaseReference mDatabase;
    private DatabaseReference mFirebaseDatabaseReference;

    private StorageReference mStorageRef;
    private FirebaseStorage storage;

    private Uri downloadUrl;

    /*Views and buttons*/
    private ImageView fotoUsuario;
    private Spinner spinGustar;
    private Spinner spinPrimera;
    private EditText editComentario;
    private String placeId;


    /*Static code of result*/
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_MAP_RESULT = 2;
    private static final String API_KEY = "AIzaSyALTyezzge7Tz1HdQMfBrUyfkJMWdk_RCE";
    private static final int RADIOUS_SEARCH = 5000;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
        
        /*Initialize views and buttons*/
        this.fotoUsuario = (ImageView) findViewById(R.id.fotoUsuario);
        this.imageWidth = 200;
        this.imageHeight = 200;
        this.spinGustar = (Spinner) findViewById(R.id.spinGustar);
        this.spinPrimera = (Spinner) findViewById(R.id.spinPrimera);
        this.editComentario = (EditText) findViewById(R.id.editComentario);

        ArrayAdapter<String> gustarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.gustarList);
        ArrayAdapter<String> primeraAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.primeraList);
        this.spinGustar.setAdapter(gustarAdapter);
        this.spinPrimera.setAdapter(primeraAdapter);

        this.storage = FirebaseStorage.getInstance();
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        /*Revisa configuración de GPS*/
        if (!gpsEstaActivado()) {
            solicitarActivacionGPS();
        }

        revisarPermisos();

        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");
        
        /*Crea cliente de para la localización*/
        crearClienteLocalizacion();
    }

    public void revisarPermisos() {
        if (ContextCompat.checkSelfPermission(ComentarioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ComentarioActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (gpsEstaActivado()) // Comprueba si el gps del dispositivo se encuentra activado
            mGoogleApiClient.connect(); // Conecta con el servicio de ubicación de Google
    }

    public void tomarFoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void enviarAporte(View v) {
        if(this.rutaImagen == "") {
            Toast.makeText(getApplicationContext(), "Debes seleccionar una imagen para continuar", Toast.LENGTH_LONG).show();
            return;
        }
        if(this.placeId == "") {
            Toast.makeText(getApplicationContext(), "La ubicación elegida no es válida. Intenta nuevamente", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Bitmap bmp = this.imageBitmap;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + ".jpg";
            uploadImage(bmp, imageFileName);
        } catch(Exception e) {
            Log.e("Bitmap Upload", e.getMessage());
        }
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


    public String obtenPlaceId() {
        if(this.longitud != 0.0 && this.latitud != 0.0) {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringBuilder googlePlacesUrl = new StringBuilder("http://maps.google.com/maps/api/geocode/json?");
            googlePlacesUrl.append("latlng=").append(latitud).append(",").append(longitud);
            googlePlacesUrl.append("&radious=").append(RADIOUS_SEARCH);
            googlePlacesUrl.append("&key=").append(API_KEY);

            JsonObjectRequest placeRequest = new JsonObjectRequest (
                    Request.Method.GET,
                    googlePlacesUrl.toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            getPlaceId(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ComentarioActivity.this, "No se pudieron obtener las coordenadas", Toast.LENGTH_LONG).show();
                }
            }
            );
            queue.add(placeRequest);
        } else {
            Toast.makeText(getApplicationContext(), "No se pudieron obtener las coordenadas", Toast.LENGTH_LONG).show();
        }
        return this.placeId;
    }

    public void uploadImage(Bitmap bitmap, String key) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tesinafinal-e025b.appspot.com");
        StorageReference photoRef = storageRef.child("imagenes/" + key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.e("Comentario Activity", "progress... " + progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(taskSnapshot);
            }
        });
    }

    public void getDownloadUrl(UploadTask.TaskSnapshot taskSnapshot) {
        this.downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
        cargarComentario();
    }

    public void cargarComentario() {
        Comments comment = new Comments();
        String key = this.mFirebaseDatabaseReference.child("Comments").push().getKey();
        comment.setCommentId(key);
        comment.setLikeVisit(this.spinGustar.getSelectedItem().toString());
        comment.setFirstTime(this.spinPrimera.getSelectedItem().toString());
        comment.setComments(this.editComentario.getText().toString());
        comment.setPlaceId(obtenPlaceId());
        comment.setPhotoRef(this.rutaImagen);
        comment.setUserId(this.userId);

        this.mFirebaseDatabaseReference.child("Comments").child(key).setValue(comment);

        Intent mainIntent = new Intent(ComentarioActivity.this, ConfirmActivity.class);
        mainIntent.putExtra("success", 1);
        this.finish();
    }

    public void getPlaceId(JSONObject result) {
        try {
            JSONArray jsonArray = result.getJSONArray("results");
            JSONObject place = jsonArray.getJSONObject(0);
            this.placeId = place.getString("place_id");
        } catch(Exception e) {
            Toast.makeText(ComentarioActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
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

    /**
     * Una vez que se conectó con los servicios de ubicación de google, obtiene las coordenadas de la posición actual
     *
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); // Obtiene la última localización conocida del dispositivo
            if (mLastLocation != null) {
                this.latitud = mLastLocation.getLatitude(); // Asignar latitud a variable de clase
                this.longitud = mLastLocation.getLongitude(); // Asignar longitud a variable de clase
            } else {
                Log.d("Comentarios Actividad", "mLastLocation nulo. No se obtuvieron las coordenadas");
            }
        }
    }

    /**
     * Mensaje de error en caso de que la conexión falle
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Comentarios Acitividad", "No se pudieron obtener las coordenadas");
    }

    /**
     * Mensaje de alerta en caso de que la conexión se haya suspendido
     *
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("Comentarios Actividad", "Conexión suspendida");
        //mGoogleApiClient.connect();
    }

    public boolean solicitarActivacionGPS() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Construye la alerta. Se coloca mensaje, botón para confirmación, y cancelación.
            builder.setMessage("Debe habilitar los servicios de GPS antes de continuar.")
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

    /**
     * Metodo que obtiene la ruta absoluta de una imagen
     *
     * @param imagenSeleccionada Uri de la imagen
     * @return string con la ruta absoluta de la imagen
     */
    public String obtenerRutaRealUri(Uri imagenSeleccionada) {
        try {
            String[] informacion_imagen = {MediaStore.Images.Media.DATA}; // Obtener la metadata de todas las imagenes guardadas en el dispositivo
            Cursor cursor = getContentResolver().query(imagenSeleccionada, informacion_imagen, null, null, null); // Buscar la imagen que coincide con el Uri dado
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  // Buscar la columna de url de imagen
            cursor.moveToFirst(); // Ir al primer elemento
            Log.e("Cursor en orden...", cursor.getString(column_index));
            return cursor.getString(column_index); // Regresar ruta real
        } catch (Exception e) {
            Log.e("Obtener ruta error", e.getMessage());
            return imagenSeleccionada.getPath(); // Regresar ruta decodificada
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.imageBitmap = (Bitmap) extras.get("data");
            this.fotoUsuario.setImageBitmap(this.imageBitmap);
        } else if(requestCode == REQUEST_MAP_RESULT && resultCode == RESULT_OK) {
            this.placeId = data.getStringExtra("place_id");
        } else {
            Log.e("Imagen no seleccionada", "Error de data");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permisos de escritura concedidos", Toast.LENGTH_LONG).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

package com.example.xxfin.tesinarecommendationsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.example.xxfin.tesinarecommendationsystem.Objects.Comments;

import java.io.File;
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

    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;

    private DatabaseReference mDatabase;
    private DatabaseReference mFirebaseDatabaseReference;

    /*Views and buttons*/
    private ImageView fotoUsuario;
    private ProgressDialog prDialog;
    private Spinner spinGustar;
    private Spinner spinPrimera;
    private EditText editComentario;
    private String placeId;


    /*Static code of result*/
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_MAP_RESULT = 2;

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
        
        /*Revisa configuración de GPS*/
        if (!gpsEstaActivado()) {
            solicitarActivacionGPS();
        }
        
        /*Crea cliente de para la localización*/
        crearClienteLocalizacion();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (gpsEstaActivado()) // Comprueba si el gps del dispositivo se encuentra activado
            mGoogleApiClient.connect(); // Conecta con el servicio de ubicación de Google
    }

    public void tomarFoto(View v) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File archivoImagen = crearArchivoSalida();
        uriImagen = Uri.fromFile(archivoImagen);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);
        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
    }

    public void enviarAporte(View v) {
        if(this.rutaImagen == "") {
            Toast.makeText(getApplicationContext(), "Debes seleccionar una imagen para continuar", Toast.LENGTH_LONG).show();
            return;
        }
        if(this.placeId == "") {

        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Comments comment = new Comments();
        String key = mFirebaseDatabaseReference.child("Comments").push().getKey();
        comment.setCommentId(key);
        comment.setLikeVisit(this.spinGustar.getSelectedItem().toString());
        comment.setFirstTime(this.spinPrimera.getSelectedItem().toString());
        comment.setComments(this.editComentario.getText().toString());


        mDatabase.child("Comments").child(key).setValue(comment);
    }

    /**
     * Metodo que crea un archivo de imagen vacio donde se va a guardar la foto capturada
     *
     * @return archivo creado
     */
    protected File crearArchivoSalida() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Recommendation_System");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
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

    public void obtenerCoordenadas() {
        Location coordenadas = null;

        LocationManager location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.latitud = coordenadas.getLatitude();
        this.longitud = coordenadas.getLongitude();
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
            return cursor.getString(column_index); // Regresar ruta real
        } catch (Exception e) {
            return imagenSeleccionada.getPath(); // Regresar ruta decodificada
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri imagenSeleccionada = data.getData();
            this.rutaImagen = obtenerRutaRealUri(imagenSeleccionada);

            if (this.longitud != 0.0 && this.latitud != 0.0) {
                /*Forma alternativa para cargar la imagen en caso de que no funcione*/
                Bitmap bmp = decodeFile(this.rutaImagen);
                this.fotoUsuario.setImageBitmap(bmp);
                prDialog = new ProgressDialog(this);
                prDialog.setCancelable(false);

            } else {
                //TODO error
            }
        }

        if(requestCode == REQUEST_MAP_RESULT && resultCode == RESULT_OK) {
            this.placeId = data.getStringExtra("place_id");
        }
    }
}

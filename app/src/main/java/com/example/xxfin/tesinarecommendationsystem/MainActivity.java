package com.example.xxfin.tesinarecommendationsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xxfin.tesinarecommendationsystem.Objects.AportesTest;
import com.example.xxfin.tesinarecommendationsystem.Objects.Asociaciones;
import com.example.xxfin.tesinarecommendationsystem.Objects.Comments;
import com.example.xxfin.tesinarecommendationsystem.Objects.Place;
import com.example.xxfin.tesinarecommendationsystem.Objects.Registro;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    /*Variables para la actividad*/
    private String email = "";
    private String userId = "";
    private int userType = 0;
    private String genero = "";
    private int edad = 0;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;

    private LinkedList comentarios;
    private LinkedList cafeId;
    private LinkedList restaurantId;
    private LinkedList hotelId;
    private ProgressDialog prDialog;

    private ImageView logo;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;
    private ImageView icon6;
    private ImageView aporte;
    private ImageView recomendacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.gc();
        Bundle extras = getIntent().getExtras();
        this.email = (String) extras.get("email");
        this.userId = (String) extras.get("userId");
        this.userType = (int) extras.get("userType");
        this.genero = (String) extras.get("genero");
        this.edad = (int) extras.get("edad");
        this.comentarios = new LinkedList();
        this.cafeId = new LinkedList();
        this.restaurantId = new LinkedList();
        this.hotelId = new LinkedList();

        this.logo = (ImageView) findViewById(R.id.imagenLogo);
        this.icon1 = (ImageView) findViewById(R.id.icon1);
        this.icon2 = (ImageView) findViewById(R.id.icon2);
        this.icon3 = (ImageView) findViewById(R.id.icon3);
        this.icon4 = (ImageView) findViewById(R.id.icon4);
        this.icon5 = (ImageView) findViewById(R.id.icon5);
        this.icon6 = (ImageView) findViewById(R.id.icon6);
        this.aporte = (ImageView) findViewById(R.id.enviarAporte);
        this.recomendacion = (ImageView) findViewById(R.id.buscarRecomendacion);
        this.logo.setImageResource(R.drawable.turista);
        this.icon1.setImageResource(R.drawable.icon1);
        this.icon2.setImageResource(R.drawable.icon2);
        this.icon3.setImageResource(R.drawable.icon3);
        this.icon4.setImageResource(R.drawable.icon4);
        this.icon5.setImageResource(R.drawable.icon5);
        this.icon6.setImageResource(R.drawable.icon6);
        this.aporte.setImageResource(R.drawable.comentario);
        this.recomendacion.setImageResource(R.drawable.recomendar);


        this.mAuth = FirebaseAuth.getInstance();
        this.mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        prDialog = new ProgressDialog(this); // Crear un dialogo para mostrar progreso
        prDialog.setCancelable(false);

        cafesIds();
        restaurantIds();
        hotelesIds();
    }

    public void enviarAporte(View v) {
        Intent aporteIntent = new Intent(MainActivity.this, ComentarioActivity.class);
        aporteIntent.putExtra("email", this.email);
        aporteIntent.putExtra("userId", this.userId);
        aporteIntent.putExtra("userType", this.userType);
        this.startActivity(aporteIntent);
        this.finish();
    }

    public void buscarRecomendacion(View v) {
        Intent recomendacionIntent = new Intent(MainActivity.this, RecomendacionActivity.class);
        recomendacionIntent.putExtra("email", this.email);
        recomendacionIntent.putExtra("userId", this.userId);
        recomendacionIntent.putExtra("userType", this.userType);
        recomendacionIntent.putExtra("genero", this.genero);
        recomendacionIntent.putExtra("edad", this.edad);
        this.startActivity(recomendacionIntent);
        this.finish();
    }

    public void obtenerUsuariosId() {
        String genero[] = {"Masculino", "Femenino"};
        String rangoEdad[] = {};
    }

    public void cargarUsuariosPrueba(View v) {
        try {
            AportesTest aportes = new AportesTest();
            aportes.insertarUsers();
            Log.e("Main", "Usuarios cargados exitosamente");
        } catch(Exception e) {
            Log.e("Main", "Error al cargar usuarios..." + e.getMessage());
        }
    }

    public void cargarAportesPrueba(View v) {
        //try {
            AportesTest aportes = new AportesTest();
            aportes.insertarAportes();
            Log.e("Main", "Aportes cargados exitosamente");
        /*} catch(Exception e) {
            Log.e("Main", "Error al cargar aportes...");
        }*/
    }

    public void cargarAsociaciones(View v) {
        try {
            mFirebaseDatabaseReference.child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Comments infoComentario = new Comments();
                    if (dataSnapshot != null) {
                        for (DataSnapshot comentarioSnap : dataSnapshot.getChildren()) {
                            infoComentario = comentarioSnap.getValue(Comments.class);
                            comentarios.addLast(infoComentario);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Búsqueda de asociaciones cancelada", Toast.LENGTH_LONG).show();
                }
            });

            prDialog.setMessage("Obteniendo lista de recomendaciones..."); // Asignar mensaje al dialogo de progreso
            prDialog.show(); // Mostrar dialogo de progreso
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    cargarAsociacionPrueba();
                    prDialog.hide();
                }
            }, 10000);
        } catch(Exception e) {
            Log.e("Asociaciones Error", "-"+e.getMessage());
        }

        Log.e("Tamaño List", "-"+comentarios.size());
    }

    public void cargarAsociacionPrueba() {
        /**/
        String _idUser;
        String _idPlace;
        String _placeType;
        String _placeName;


        TreeMap<String, Place> idLugarTMap = new TreeMap<>();
        TreeMap<String, Integer> idPersonasTMap = new TreeMap<>();
        System.out.println("Reglas de Asociación");

        Place[][] matData = new Place[50][54];

        int inxCol = 0;
        int inxRow = 0;

//      Ejemplo 1
//        Registro[] inputData = new Registro[8];
//      Ejemplo 2
        //Registro[] inputData = new Registro[22];
        Registro[] inputData = new Registro[this.comentarios.size()];


//        inputData[0] = new Registro("iduser0000001", "idPlace0000001", "placeName", "Category");
//      Ejemplo 1
/*
        inputData[0] = new Registro("P1", "B", "lB", "food");
        inputData[1] = new Registro("P3", "B", "lB", "food");
        inputData[2] = new Registro("P1", "A", "lA", "food");
        inputData[3] = new Registro("P2", "B", "lB", "food");
        inputData[4] = new Registro("P3", "A", "lA", "food");
        inputData[5] = new Registro("P2", "D", "lD", "food");
        inputData[6] = new Registro("P2", "C", "lC", "food");
        inputData[7] = new Registro("P3", "D", "lD", "food");
*/
//      Ejemplo 2


        for(int i = 0; i < inputData.length; i++) {
            String persona = ""; String origen = "";
            String nombre = ""; String tipo = "";

            Comments comentarioActual = (Comments) this.comentarios.get(i);
            persona = comentarioActual.getUserId();
            origen = comentarioActual.getPlaceId();
            nombre = comentarioActual.getPlaceId();

            boolean flagCafe = false;
            boolean flagRestaurante = false;
            boolean flagAlojamiento = false;
            for(int j = 0; j < this.cafeId.size(); j++) {
                if(this.cafeId.get(j).equals(origen)) {
                    flagCafe = true;
                    tipo = "cafe";
                    break;
                }
            }
            if(!flagCafe) {
                for(int j = 0; j < this.restaurantId.size(); j++) {
                    if(this.restaurantId.get(j).equals(origen)) {
                        flagRestaurante = true;
                        tipo = "restaurant";
                        break;
                    }
                }
            }
            if(!flagRestaurante) {
                for(int j = 0; j < this.hotelId.size(); j++) {
                    if(this.hotelId.get(j).equals(origen)) {
                        flagAlojamiento = true;
                        tipo = "lodging";
                        break;
                    }
                }
            }
            inputData[i] = new Registro(persona, origen, nombre, tipo);
        }

        /*inputData[0] = new Registro("P1", "A", "lA", "food");
        inputData[1] = new Registro("P10", "B", "lB", "food");
        inputData[2] = new Registro("P10", "A", "lA", "food");
        inputData[3] = new Registro("P2", "B", "lB", "food");
        inputData[4] = new Registro("P2", "C", "lC", "food");
        inputData[5] = new Registro("P2", "D", "lD", "food");
        inputData[6] = new Registro("P3", "D", "lD", "food");
        inputData[7] = new Registro("P4", "A", "lA", "food");
        inputData[8] = new Registro("P5", "B", "lB", "food");
        inputData[9] = new Registro("P5", "A", "lA", "food");
        inputData[10] = new Registro("P5", "D", "lD", "food");
        inputData[11] = new Registro("P6", "E", "lE", "food");
        inputData[12] = new Registro("P7", "B", "lB", "food");
        inputData[13] = new Registro("P7", "E", "lE", "food");
        inputData[14] = new Registro("P8", "B", "lB", "food");
        inputData[15] = new Registro("P8", "A", "lA", "food");
        inputData[16] = new Registro("P9", "B", "lB", "food");
        inputData[17] = new Registro("P9", "D", "lD", "food");
        inputData[18] = new Registro("P9", "E", "lE", "food");
        inputData[19] = new Registro("P9", "A", "lA", "food");
        inputData[20] = new Registro("P1", "B", "lB", "food");
        inputData[21] = new Registro("P1", "E", "lE", "food");*/

        Place lugarN = new Place("?", "??", "-1");

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 54; j++) {
                matData[i][j] = lugarN;
            }
        }

        System.out.println("inicializó");

        int totalInputData =  inputData.length;
        int inxReadData = 0;
        System.out.println("Input Data  = " + totalInputData);

        while (inxReadData < totalInputData) {
            _idUser = inputData[inxReadData].getIdUser();
            _idPlace = inputData[inxReadData].getIdPlace();
            _placeType = inputData[inxReadData].getPlaceType();
            _placeName = inputData[inxReadData].getPlaceName();

            if (!_idUser.equals("") && !_idPlace.equals("") && !_placeType.equals("") ){
                if (!(idLugarTMap.containsKey(_idPlace))) {
                    Place objLugar = new Place(_idPlace, _placeName, inxCol, _placeType);
                    idLugarTMap.put(_idPlace, objLugar);                           // guarda los lugares únicos en TreeMap-Lugares
                    inxCol++;                                                      // inxCol contador de lugares que sirve para localizar la columna en la matriz "matData"
                }
                // guarda un Tree de idPersonas
                if (!(idPersonasTMap.containsKey(_idUser))) {                      // guarda personas únicas en TreeMap-Personas
                    idPersonasTMap.put((String) _idUser, inxRow++);                // inxRow contador de personas que sirve para localizar el renglón en la matriz "matData"
                }
                // guarda en la matriz los lugares que cada uno de las personas visitó
                Integer rr = idPersonasTMap.get(_idUser);                          // identifica un Renglón Único por persona
                Place auxLugar = (Place) idLugarTMap.get(_idPlace);
                int cc = auxLugar.getPosition();
                matData[rr][cc] = auxLugar;
            }
            inxReadData++;
        }
        System.out.println("Numero de lugares registrados: " + idLugarTMap.size());
        System.out.println("CONCLUYÓ lectura de lugares ");

        int numLugares = idLugarTMap.size();
        int numPersonas = idPersonasTMap.size();

        Place[][] matPreferenciasLugares = new Place[numLugares][numLugares];

        lugarN = new Place("?", "??", -1, "0");

        for (int i = 0; i < numLugares; i++) {
            for (int j = 0; j < numLugares; j++) {
                matPreferenciasLugares[i][j] = lugarN;
            }
        }

        System.out.println("Numero de personas: " + idPersonasTMap.size());
        System.out.println("Numero de lugares:  " + idLugarTMap.size());


        /*
         * Ejemplo para aplicar las Reglas de Asociación Basket Market
         * personas: P1, P2, P3
         * lugareas: lA, lB, lC, lD
         *
         * matData:
         *       lA  lB  lC  lD
         *   P1  x   x   -   -
         *   P2  -   x   x   x
         *   P3  x   x   -   x
         *
         * matPreferenciaLugares:
         *       lA  lB  lC  lD
         *    lA  2   2   0   1
         *    lB  2   3   1   2
         *    lC  0   1   1   1
         *    lD  1   2   1   2
         *
         *  Otro ejemplo
         *
         * matData:
         *       lA  lB  lC  lD  lE
         *   P1  x   x   -   -   x
         *   P2  -   x   x   x   -
         *   P3  -   -   -   x   -
         *   P4  x   -   -   -   -
         *   P5  x   x   -   x   -
         *   P6  -   -   -   -   x
         *   P7  -   x   -   -   x
         *   P8  x   x   -   -   -
         *   P9  x   x   -   x   x
         *   P10 x   x   -   -   -
         *
         * matPreferenciaLugares:
         *       lA  lB  lC  lD  lE
         *    lA  6   5   0   2   2
         *    lB  5   7   1   3   3
         *    lC  0   1   1   1   0
         *    lD  2   3   1   4   1
         *    lE  2   3   0   1   4
        */
        System.out.println(" INICIO DE R-A ");
        long startTime = System.currentTimeMillis();
        for (int p = 0; p < numLugares; p++) {
            for (int x = 0; x < numPersonas; x++) {
                if (!(matData[x][p].getKeyPlace().equals("?"))) {
                    for (int y = 0; y < numLugares; y++) {
                        // valida que la Clasificación del Lugar sea el mismo, para contabilizarlo
                        if (!(matData[x][y].getKeyPlace().equals("?")) && (matData[x][p].getCategory().equals(matData[x][y].getCategory()))) {
                            if (matPreferenciasLugares[p][y].getKeyPlace().equals("?")) {
                                matPreferenciasLugares[p][y] = new Place(matData[x][y].getKeyPlace(), matData[x][y].getName(), matData[x][y].getPosition(), matData[x][y].getCategory());
                            }
                            matPreferenciasLugares[p][y].increaseVisits(1);
                        }
                    }
                }
            }
        }
        System.out.println(" FIN DE R-A ");
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo Total: " + (endTime - startTime) + " ms");

        NumberFormat formatoDefinido = new DecimalFormat(".");
        formatoDefinido.setMaximumFractionDigits(2);
        formatoDefinido.setMinimumFractionDigits(2);

        String strAux;
        String placeKey;
        String pivotProductKey;

        for (int i = 0; i < numLugares; i++) {
            strAux = matPreferenciasLugares[i][i].getKeyPlace();
            pivotProductKey = "";
            for (int m = 0; m < strAux.length(); m++) {
                if (strAux.charAt(m) != '\"') {
                    pivotProductKey += strAux.charAt(m);
                }
            }

            for (int j = 0; j < numLugares; j++) {
                if (matPreferenciasLugares[i][j].getNumberVisits() != 0) {
                    double d1 = Double.parseDouble(Integer.toString(matPreferenciasLugares[i][j].getNumberVisits()));
                    double d2 = Double.parseDouble(Integer.toString(matPreferenciasLugares[i][i].getNumberVisits()));

                    if (i != j) {
                        strAux = matPreferenciasLugares[i][j].getKeyPlace();
                        placeKey = "";
                        for (int m = 0; m < strAux.length(); m++) {
                            if (strAux.charAt(m) != '\"') {
                                placeKey += strAux.charAt(m);
                            }
                        }
                        try {
                            String key = this.mFirebaseDatabaseReference.child("Asociaciones").push().getKey();
                            Asociaciones nuevaAsociacion = new Asociaciones();
                            nuevaAsociacion.setPlaceIdOrigen(pivotProductKey);
                            nuevaAsociacion.setPlaceIdDestino(placeKey);
                            nuevaAsociacion.setVisitasOrigen(matPreferenciasLugares[i][i].getNumberVisits() + "");
                            nuevaAsociacion.setVisitasDestino(matPreferenciasLugares[i][j].getNumberVisits() + "");
                            nuevaAsociacion.setConfianza(formatoDefinido.format((d1 / d2) * 100));
                            nuevaAsociacion.setSoporte(formatoDefinido.format((d1 / numPersonas) * 100));

                            this.mFirebaseDatabaseReference.child("Asociaciones").child(key).setValue(nuevaAsociacion);
                            Log.e("Asociacion agregada", "-"+nuevaAsociacion.getPlaceIdOrigen());
                        }catch(Exception e) {
                            Log.e("Error asociacion", "-"+e.getMessage());
                            e.printStackTrace();
                        }
                        System.out.println(
                                pivotProductKey + ","
                                        + "'" + matPreferenciasLugares[i][i].getNumberVisits() + "'" + ","
                                        + placeKey + ","
                                        + "'" + matPreferenciasLugares[i][j].getNumberVisits() + "'"
                                        + " confidence = " + formatoDefinido.format((d1 / d2) * 100)
                                        + " support = " + formatoDefinido.format((d1/numPersonas) * 100)
                        );
/*
                        queryDB = "INSERT INTO AsociacionProd (nDescarRef, claveRef, nDescarRelac, claveRelac, porcentRelac) VALUES (";
                        queryDB += matPreferenciasLugares[i][i].getNumberDownload() + ",";
                        queryDB += "'" + pivotProductKey + "'" + ",";
                        queryDB += matPreferenciasLugares[i][j].getNumberDownload() + ",";
                        queryDB += "'" + productKey + "'" + ",";
                        queryDB += formatoDefinido.format((d1 / d2) * 100);
                        queryDB += ")";
*/
                    }
                }
            }
        }
        System.out.println("\n Matriz de Preferencias de Lugares (matPreferenciaLugares)\n");
        for(int x=0; x<numLugares; x++){
            for(int y=0; y<numLugares; y++){
                System.out.print(matPreferenciasLugares[x][y].getName()+":"+ matPreferenciasLugares[x][y].getNumberVisits() + " ");
            }
            System.out.println();
        }
        System.out.println("\n Lugares visitados por cada persona  matData \n");

        for(int x=0; x<numPersonas; x++){
            for(int y=0; y<numLugares; y++){
                System.out.print(matData[x][y].getName()+ " ");
            }
            System.out.println();
        }
        /***********/
    }

    public void cafesIds() {
        this.cafeId.add("ChIJ6772Reda04URKDkrsUue9YA"); this.cafeId.add("ChIJ-cU1Q_FE04URWV1D6HbaKOk");
        this.cafeId.add("ChIJydKwKCpb04URcSPLPIJad4M"); this.cafeId.add("ChIJYU-PMypb04UR7yZ0Kdkx588");
        this.cafeId.add("ChIJK-BhczFb04URYqG9JeTTBks"); this.cafeId.add("ChIJ23XB0zxb04URKEMEapQT12U");
        this.cafeId.add("ChIJzSt8yjVb04URfWDx0JRkC70"); this.cafeId.add("ChIJ43-jwRhb04URN5rOxPgJrL8");
        this.cafeId.add("ChIJ96SyDyxb04URL9--LFUM1YY"); this.cafeId.add("ChIJz-bwLTpb04URxYNxEx6wzN0");
        this.cafeId.add("ChIJ-8prZ81a04URrh-0pSmc4d8"); this.cafeId.add("ChIJTQpfY_da04URAHMnI_MtHD0");
        this.cafeId.add("ChIJIw5AQzZb04URkE-B4cheWr0"); this.cafeId.add("ChIJ____ky9F04URwSPXyRLfPgE");
        this.cafeId.add("ChIJIfdvQjRb04URnNMpVCoOyac"); this.cafeId.add("ChIJmXPjv-Va04URJN2T7SZFX84");
        this.cafeId.add("ChIJq3X1kdVa04URaZDSSRovB-4"); this.cafeId.add("ChIJDybk5S9F04URuUtL2ftszpI");
    }

    public void restaurantIds() {
        this.restaurantId.add("ChIJI8sKiipb04URiTKY1h347Kk");   this.restaurantId.add("ChIJAQeR59VE04URIdDTJTt_uVI");
        this.restaurantId.add("ChIJE6Bjxp5a04URLzXL5WHipiY");   this.restaurantId.add("ChIJgQJcdhhb04UR_gvl26c-iHw");
        this.restaurantId.add("ChIJydKwKCpb04URcSPLPIJad4M");   this.restaurantId.add("ChIJ0SHrESxF04URwItDQLVi900");
        this.restaurantId.add("ChIJG7laIpha04UR0lN4WWAFl4g");   this.restaurantId.add("ChIJ-yocUSpb04URbi_GN95koN4");
        this.restaurantId.add("ChIJAe2pQTtb04URQDvPRqHN6fU");   this.restaurantId.add("ChIJK-BhczFb04URYqG9JeTTBks");
        this.restaurantId.add("ChIJCzisuB5b04URvDmfy2FBl2E");   this.restaurantId.add("ChIJHz-5e5VQ04URc_hATpYwbEU");
        this.restaurantId.add("ChIJ9btWFTZb04URlAumHG-1aIs");   this.restaurantId.add("ChIJcWuYyhhb04URkiqngDTX_YQ");
        this.restaurantId.add("ChIJ23XB0zxb04URKEMEapQT12U");   this.restaurantId.add("ChIJ05DPyBhb04URd1mFgIsGWjc");
        this.restaurantId.add("ChIJ4YnFTx5b04URQ-kR4wBx_1I");   this.restaurantId.add("ChIJEQqPZOVa04URVc8jIaoruKQ");
    }

    public void hotelesIds() {
        this.hotelId.add("ChIJI8sKiipb04URiTKY1h347Kk");    this.hotelId.add("ChIJIf9MzTNF04URXPqD-K2u95w");
        this.hotelId.add("ChIJBxoxLypb04URuvi7myYSgHQ");    this.hotelId.add("ChIJgXB9DtpE04UR7dX_EA7s9P0");
        this.hotelId.add("ChIJm_S0gCNF04URaGE8B-pMD7A");    this.hotelId.add("ChIJCQ0cmtRE04URgK8OS3ZKNxk");
        this.hotelId.add("ChIJ71n6PMta04URB6Q5KyH1w50");    this.hotelId.add("ChIJqyDcKxBb04URYorVuMO6DOA");
        this.hotelId.add("ChIJwW5SVWxb04URC_JigWrz8lQ");    this.hotelId.add("ChIJ4UVGNDJF04URMMYgtG_ALNU");
        this.hotelId.add("ChIJHagK_ABb04URKsEvap6UK5A");    this.hotelId.add("ChIJL1Ll3Stb04URjoNHCFXjcrY");
        this.hotelId.add("ChIJsytbntNE04URbfu_cpGVCt4");    this.hotelId.add("ChIJUW5vMixb04UR8iq_LHParC8");
        this.hotelId.add("ChIJHz-5e5VQ04UR73nUfTTLuNE");    this.hotelId.add("ChIJV2DhBYKVYoYRIG3QB8JficA");
        this.hotelId.add("ChIJUYsDji5b04URGr-u-MtMmO0");    this.hotelId.add("ChIJ66obS4djzYURpLprHvCimxw");
    }

    public void signOut(View v) {
        mAuth.signOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        this.finish();
    }
}

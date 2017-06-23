package com.example.xxfin.tesinarecommendationsystem.Objects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.TreeMap;

/**
 * Created by xxfin on 22/06/2017.
 */

public class R_A {

//    Ejemplo 1
//    private final static int NUM_MAX_PERSONAS = 3;
//    private final static int NUM_MAX_LUGARES = 4;

    //    Ejemplo 2
    private final static int NUM_MAX_PERSONAS = 10;
    private final static int NUM_MAX_LUGARES = 5;

    public static void main(String[] args) {
        String _idUser;
        String _idPlace;
        String _placeType;
        String _placeName;


        TreeMap<String, Place> idLugarTMap = new TreeMap<>();
        TreeMap<String, Integer> idPersonasTMap = new TreeMap<>();
        System.out.println("Reglas de Asociación");

        Place[][] matData = new Place[NUM_MAX_PERSONAS][NUM_MAX_LUGARES];

        int inxCol = 0;
        int inxRow = 0;

//      Ejemplo 1
//        Registro[] inputData = new Registro[8];
//      Ejemplo 2
        Registro[] inputData = new Registro[22];


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

        inputData[0] = new Registro("P1", "A", "lA", "food");
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
        inputData[21] = new Registro("P1", "E", "lE", "food");


        Place lugarN = new Place("?", "??", "-1");

        for (int i = 0; i < NUM_MAX_PERSONAS; i++) {
            for (int j = 0; j < NUM_MAX_LUGARES; j++) {
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

    }
}

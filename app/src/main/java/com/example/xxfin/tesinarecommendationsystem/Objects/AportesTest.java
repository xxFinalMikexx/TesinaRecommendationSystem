package com.example.xxfin.tesinarecommendationsystem.Objects;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by xxfin on 22/06/2017.
 */

public class AportesTest {
    private String aportesRestaurant[];
    private String aportesTienda[];
    private String aportesAlojamiento[];
    private LinkedList usersIds;
    private LinkedList usersEmails;
    private LinkedList usersGenero;
    private LinkedList cafeId;
    private LinkedList restaurantId;
    private LinkedList hotelId;

    private DatabaseReference mFirebaseDatabaseReference;

    public AportesTest() {
        this.usersIds = new LinkedList();
        this.usersEmails = new LinkedList();
        this.usersGenero = new LinkedList();
        this.cafeId = new LinkedList();
        this.restaurantId = new LinkedList();
        this.hotelId = new LinkedList();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        comentariosSeed();
        usuariosIds();
        usuariosEmails();
        usuariosGenero();
        insertarUsers();
    }

    public void insertarUsers() {
        Random ran = new Random();
        for(int i = 0; i < 50; i++) {
            int edad = ran.nextInt(4 - 1) + 1;
            int type = getUserType(this.usersGenero.get(i)+"", edad);
            Users actual = new Users(this.usersIds.get(i)+"",this.usersEmails.get(i)+"",
                    this.usersGenero.get(i)+"",edad,type,this.usersEmails.get(i)+"");

            String key = mFirebaseDatabaseReference.child("Users").push().getKey();
            mFirebaseDatabaseReference.child("Users").child(key).setValue(actual);
        }
    }

    public void usuariosIds() {
        this.usersIds.add("1nBQJGmj5metwdoF0FGs4Fx3DUo2"); this.usersIds.add("YVb2BcKJYRNoroxjNHU0ycp6o3i1");
        this.usersIds.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersIds.add("Yl8d9DYVM8Ml3M1PCfdYoYOnX0H2");
        this.usersIds.add("4oZoE42t1ZMlrxnvsI92G6whsCG2"); this.usersIds.add("Z8GzFglPEPZYjiTRHvW06SREUKo2");
        this.usersIds.add("56qQ0vbTdeTMVcEC49WJ9vDdRmk1"); this.usersIds.add("ZPM6ZZXbTbNpRE5x7TLpLd5Ozbx2");
        this.usersIds.add("5gVDY4BG5SU1G1j0hSG0syeIbgn1"); this.usersIds.add("ZqS4gHxiCqdzwdyu52JucSJwoQx1");
        this.usersIds.add("5vtITfg5ZCQHtg1SUHWSV2042qZ2"); this.usersIds.add("aU4hFLxU7OPGfhGaRBeNHFUockD3");
        this.usersIds.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2"); this.usersIds.add("d3yIUt3DSkVQAFNVqHCPmHQiyHb2");
        this.usersIds.add("8FMPFVSIArh9lFdgcDklHR8EsPW2"); this.usersIds.add("fA9xptNdjQTaqpmY6khwCRq0GbA2");
        this.usersIds.add("AXll5qkg1eVQaZVkrMIrV3JnJi73"); this.usersIds.add("fsQgdEYqFGXY3cXLdJggxCbUiwt1");
        this.usersIds.add("BoklpDoxk0WeCgsIRxlkkCxnk6v1"); this.usersIds.add("gnlAV6D6KgbSV0gk0ww8IHdpvBT2");
        this.usersIds.add("CAoQxGbjPUNnFmczGrXB58i4V5Q2"); this.usersIds.add("hWTpN5CZHcYBhjWmxjMPzQ3c7P72");
        this.usersIds.add("CrCIhdmC4xbI683quSYidikEnFZ2"); this.usersIds.add("hkef5qtruOOoCkFSMq4NzTpPTRo1");
        this.usersIds.add("D1jfIGwOA2Prl0p5gyTxGQKh6Ho1"); this.usersIds.add("jRQ1qtgbCsXl8tGNoNgpXP4wh072");
        this.usersIds.add("EjnbZHLd3habKLALDoxK0s9gNV13"); this.usersIds.add("kQovzI2W3iYWvLJgyXBs7naMYYt1");
        this.usersIds.add("GAuQmeFN8eRi6i8iBRM6wX19Kb63"); this.usersIds.add("krLqeP7AkfaedEsO1X5dmNQy3tb2");
        this.usersIds.add("GyQvtBa0ZzWvGI3780Sf77qfjj13"); this.usersIds.add("lj2kFP51HxQIvOTLOsNTsIQoJbz1");
        this.usersIds.add("IjSHugg1SYSE4ZQS7JNTSmMQ9lE2"); this.usersIds.add("nhnJN4umrrfsJIzXCktd53nSTF33");
        this.usersIds.add("Jlqw0gYAJThZWqqRTHkt1xZcJVP2"); this.usersIds.add("nlIvonwca9NZqpexd6u8TRh0g5D2");
        this.usersIds.add("KZ967dQPrOhMFws9hSZIPOqZv5l2"); this.usersIds.add("pxBSUEaIbFdgzIKFA6s9juQGkKq2");
        this.usersIds.add("Kv9Te9Hc6Raxt4nCFm0UBb2jfrl2"); this.usersIds.add("w10vvduQyAXKJl1J678ZXbPhXTB3");
        this.usersIds.add("RFsnyV6Wjdgd9mjoIhzyZoIcPO32"); this.usersIds.add("w74IKszcPQRgnLLtUT2fVs7Whs83");
        this.usersIds.add("Sl4AfRlflXRNXWBXIPkmapU6NSv1"); this.usersIds.add("w8HxhKTb2FPd6domR8p8z3TrCRe2");
        this.usersIds.add("U1DR0VG7vkhLdFIpmkoN6fy0SY82"); this.usersIds.add("wlCEIVHu62OtP48EFhWRIIYtqmH3");
        this.usersIds.add("XQwJ0M3La9cborqDtOkKTYbFfJp2"); this.usersIds.add("ya0QYOLfOQexs1FURfN8qAXbwz73");
        this.usersIds.add("XQy3XXVZIgeAfkNkMByUFXy358o1"); this.usersIds.add("yuYcElWqXWe3zFoKkM1rl1TJVpB3");
    }

    public void usuariosEmails() {
        this.usersEmails.add("minbaltazar@gmail.com"); this.usersEmails.add("brandonaviles@gmail.com");
        this.usersEmails.add("erickpaz@gmail.com"); this.usersEmails.add("josueflores@gmail.com");
        this.usersEmails.add("jorgegarcia@gmail.com"); this.usersEmails.add("antonioflores@gmail.com");
        this.usersEmails.add("monicalucia@gmail.com"); this.usersEmails.add("fernandoorozco@gmail.com");
        this.usersEmails.add("beatrizhernandez@gmail.com"); this.usersEmails.add("monicapelayo@gmail.com");
        this.usersEmails.add("jaimerodriguez@gmail.com"); this.usersEmails.add("josefinagaspar@gmail.com");
        this.usersEmails.add("carmenangeles@gmail.com"); this.usersEmails.add("grasielalopez@gmail.com");
        this.usersEmails.add("jmikeflores@gmail.com"); this.usersEmails.add("robertomonzon@gmail.com");
        this.usersEmails.add("cristiangonzales@gmail.com"); this.usersEmails.add("luciaojeda@gmail.com");
        this.usersEmails.add("marthagarcia@gmail.com"); this.usersEmails.add("cristinasantos@gmail.com");
        this.usersEmails.add("marcosanguiano@gmail.com"); this.usersEmails.add("fabioladiaz@gmail.com");
        this.usersEmails.add("isabelhernandez@gmail.com"); this.usersEmails.add("jesusalberto@gmail.com");
        this.usersEmails.add("estelaflores@gmail.com"); this.usersEmails.add("rocioaldeco@gmail.com");
        this.usersEmails.add("jorgeulises@gmail.com"); this.usersEmails.add("julianavazquez@gmail.com");
        this.usersEmails.add("karentoledo@gmail.com"); this.usersEmails.add("orlandobarrera@gmail.com");
        this.usersEmails.add("eleuteriopaz@gmail.com"); this.usersEmails.add("ernestovazquez@gmail.com");
        this.usersEmails.add("luisarenas@gmail.com"); this.usersEmails.add("abdiasguilar@gmail.com");
        this.usersEmails.add("eldaflores@gmail.com"); this.usersEmails.add("aaronflores@gmail.com");
        this.usersEmails.add("xxfinalmikexx@gmail.com"); this.usersEmails.add("carolinagovea@gmail.com");
        this.usersEmails.add("ignacioperez@gmail.com"); this.usersEmails.add("omarangeles@gmail.com");
        this.usersEmails.add("miguelcabrera@gmail.com"); this.usersEmails.add("diegogaspar@gmail.com");
        this.usersEmails.add("pastorflores@gmail.com"); this.usersEmails.add("akaren.paz@gmail.com");
        this.usersEmails.add("juanarenas@gmail.com"); this.usersEmails.add("susanaesquivel@gmail.com");
        this.usersEmails.add("raulosorio@gmail.com"); this.usersEmails.add("pedroperez@gmail.com");
        this.usersEmails.add("agustinlara@gmail.com"); this.usersEmails.add("eduardojuarez@gmail.com");
    }

    public void usuariosGenero() {
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Femenino");   this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Femenino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Femenino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
        this.usersGenero.add("Masculino");  this.usersGenero.add("Masculino");
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

    public int getUserType(String genero, int edad) {
        int generoType = -1;
        switch(genero) {
            case "Masculino":
                generoType = 1;
                break;
            case "Femenino":
                generoType = 2;
                break;
        }

        int userType = -1;
        if(generoType == 1) {
            if(edad == 1) {
                userType = 1;
            } else if(edad == 2) {
                userType = 2;
            } else if(edad == 3) {
                userType = 3;
            } else if(edad == 4) {
                userType = 4;
            } else {
                //TODO not defined
                userType = -1;
            }
        } else if(generoType == 2) {
            if(edad == 1) {
                userType = 5;
            } else if(edad == 2) {
                userType = 6;
            } else if(edad == 3) {
                userType = 7;
            } else if(edad == 4) {
                userType = 8;
            } else {
                //TODO not defined
                userType = -1;
            }
        } else {
            //TODO not defined
            userType = -1;
        }
        return userType;
    }

    public void comentariosSeed() {
        this.aportesRestaurant = new String[20];
        this.aportesTienda = new String[20];
        this.aportesAlojamiento = new String [20];
        LinkedList listaComentarios = new LinkedList();

        Random ran = new Random();
        int low = 0;
        int high = 6;

        listaComentarios.add("El lugar es excelente, el servicio muy bueno y sumamente recomendable. ");
        listaComentarios.add("Buenos precios y buena ubicación. Lo recomiendo totalmente");
        listaComentarios.add("Muy buenos precios, el personal muy amable, lo mejor estacionamiento gratis, por lo cual puedes estar tranquilamente.");
        listaComentarios.add("Lugar tradicional para pasear o comprar");
        listaComentarios.add("Hay una parada de camiones justo en frente. Tiene todo accesible y a la mano.");
        listaComentarios.add("Pésimo estacionamiento locales en mal servicio y de poco real interes");
        listaComentarios.add("Esta horrible, no hay estacionamiento, v esta como abandonada.");

        for(int i = 0; i < this.aportesRestaurant.length; i++) {
            int random = ran.nextInt(high - low) + low;
            this.aportesRestaurant[i] = listaComentarios.get(random) + "";
        }
        for(int i = 0; i < this.aportesTienda.length; i++) {
            int random = ran.nextInt(high - low) + low;
            this.aportesTienda[i] = listaComentarios.get(random) + "";
        }
        for(int i = 0; i < this.aportesAlojamiento.length; i++) {
            int random = ran.nextInt(high - low) + low;
            this.aportesAlojamiento[i] = listaComentarios.get(random) + "";
        }

    }

}

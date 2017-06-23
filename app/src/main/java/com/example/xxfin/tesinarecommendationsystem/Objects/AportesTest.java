package com.example.xxfin.tesinarecommendationsystem.Objects;

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

    public AportesTest() {
        this.usersIds = new LinkedList();
        this.usersEmails = new LinkedList();
        comentariosSeed();
        usuariosIds();
        usuariosEmails();
    }

    public void usuariosIds() {
        this.usersIds.add("yuYcElWqXWe3zFoKkM1rl1TJVpB3"); this.usersIds.add("XQy3XXVZIgeAfkNkMByUFXy358o1");
        this.usersIds.add("ya0QYOLfOQexs1FURfN8qAXbwz73"); this.usersIds.add("XQwJ0M3La9cborqDtOkKTYbFfJp2");
        this.usersIds.add("wlCEIVHu62OtP48EFhWRIIYtqmH3"); this.usersIds.add("U1DR0VG7vkhLdFIpmkoN6fy0SY82");
        this.usersIds.add("w8HxhKTb2FPd6domR8p8z3TrCRe2"); this.usersIds.add("Sl4AfRlflXRNXWBXIPkmapU6NSv1");
        this.usersIds.add("w74IKszcPQRgnLLtUT2fVs7Whs83"); this.usersIds.add("RFsnyV6Wjdgd9mjoIhzyZoIcPO32");
        this.usersIds.add("w10vvduQyAXKJl1J678ZXbPhXTB3"); this.usersIds.add("Kv9Te9Hc6Raxt4nCFm0UBb2jfrl2");
        this.usersIds.add("pxBSUEaIbFdgzIKFA6s9juQGkKq2"); this.usersIds.add("KZ967dQPrOhMFws9hSZIPOqZv5l2");
        this.usersIds.add("nlIvonwca9NZqpexd6u8TRh0g5D2"); this.usersIds.add("Jlqw0gYAJThZWqqRTHkt1xZcJVP2");
        this.usersIds.add("nhnJN4umrrfsJIzXCktd53nSTF33"); this.usersIds.add("IjSHugg1SYSE4ZQS7JNTSmMQ9lE2");
        this.usersIds.add("lj2kFP51HxQIvOTLOsNTsIQoJbz1"); this.usersIds.add("GyQvtBa0ZzWvGI3780Sf77qfjj13");
        this.usersIds.add("krLqeP7AkfaedEsO1X5dmNQy3tb2"); this.usersIds.add("GAuQmeFN8eRi6i8iBRM6wX19Kb63");
        this.usersIds.add("kQovzI2W3iYWvLJgyXBs7naMYYt1"); this.usersIds.add("EjnbZHLd3habKLALDoxK0s9gNV13");
        this.usersIds.add("jRQ1qtgbCsXl8tGNoNgpXP4wh072"); this.usersIds.add("CrCIhdmC4xbI683quSYidikEnFZ2");
        this.usersIds.add("hkef5qtruOOoCkFSMq4NzTpPTRo1"); this.usersIds.add("CAoQxGbjPUNnFmczGrXB58i4V5Q2");
        this.usersIds.add("hWTpN5CZHcYBhjWmxjMPzQ3c7P72"); this.usersIds.add("BoklpDoxk0WeCgsIRxlkkCxnk6v1");
        this.usersIds.add("gnlAV6D6KgbSV0gk0ww8IHdpvBT2"); this.usersIds.add("AXll5qkg1eVQaZVkrMIrV3JnJi73");
        this.usersIds.add("fsQgdEYqFGXY3cXLdJggxCbUiwt1"); this.usersIds.add("8FMPFVSIArh9lFdgcDklHR8EsPW2");
        this.usersIds.add("fA9xptNdjQTaqpmY6khwCRq0GbA2"); this.usersIds.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersIds.add("d3yIUt3DSkVQAFNVqHCPmHQiyHb2"); this.usersIds.add("5vtITfg5ZCQHtg1SUHWSV2042qZ2");
        this.usersIds.add("aU4hFLxU7OPGfhGaRBeNHFUockD3"); this.usersIds.add("5gVDY4BG5SU1G1j0hSG0syeIbgn1");
        this.usersIds.add("ZqS4gHxiCqdzwdyu52JucSJwoQx1"); this.usersIds.add("56qQ0vbTdeTMVcEC49WJ9vDdRmk1");
        this.usersIds.add("ZPM6ZZXbTbNpRE5x7TLpLd5Ozbx2"); this.usersIds.add("4oZoE42t1ZMlrxnvsI92G6whsCG2");
        this.usersIds.add("Z8GzFglPEPZYjiTRHvW06SREUKo2"); this.usersIds.add("D1jfIGwOA2Prl0p5gyTxGQKh6Ho1");
        this.usersIds.add("Yl8d9DYVM8Ml3M1PCfdYoYOnX0H2"); this.usersIds.add("krLqeP7AkfaedEsO1X5dmNQy3tb2");
        this.usersIds.add("YVb2BcKJYRNoroxjNHU0ycp6o3i1"); this.usersIds.add("XQwJ0M3La9cborqDtOkKTYbFfJp2");
    }

    public void usuariosEmails() {
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
        this.usersEmails.add("4UHI9LCQEMf2mtxjJaUBgCR5x072"); this.usersEmails.add("6ARXMAE4HSOHhTyDMJ5L92YKZhr2");
    }

    public void comentariosSeed() {
        this.aportesRestaurant = new String[20];
        this.aportesTienda = new String[20];
        this.aportesAlojamiento = new String [20];
        LinkedList listaComentarios = new LinkedList();

        Random ran = new Random();
        int low = 0;
        int high = 6;

        String tokenRestaurant = "Restaurant";
        String tokenTienda = "Tienda";
        String tokenHotel = "Hotel";

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

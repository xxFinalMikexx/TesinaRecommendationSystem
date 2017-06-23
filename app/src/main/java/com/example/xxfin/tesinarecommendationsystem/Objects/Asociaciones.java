package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 22/06/2017.
 */

public class Asociaciones {
    private String placeIdOrigen;
    private String visitasOrigen;
    private String placeIdDestino;
    private String visitasDestino;
    private String confianza;
    private String soporte;

    public Asociaciones() {

    }

    public String getPlaceIdOrigen() {
        return placeIdOrigen;
    }

    public void setPlaceIdOrigen(String placeIdOrigen) {
        this.placeIdOrigen = placeIdOrigen;
    }

    public String getVisitasOrigen() {
        return visitasOrigen;
    }

    public void setVisitasOrigen(String visitasOrigen) {
        this.visitasOrigen = visitasOrigen;
    }

    public String getPlaceIdDestino() {
        return placeIdDestino;
    }

    public void setPlaceIdDestino(String placeIdDestino) {
        this.placeIdDestino = placeIdDestino;
    }

    public String getVisitasDestino() {
        return visitasDestino;
    }

    public void setVisitasDestino(String visitasDestino) {
        this.visitasDestino = visitasDestino;
    }

    public String getConfianza() {
        return confianza;
    }

    public void setConfianza(String confianza) {
        this.confianza = confianza;
    }

    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }
}

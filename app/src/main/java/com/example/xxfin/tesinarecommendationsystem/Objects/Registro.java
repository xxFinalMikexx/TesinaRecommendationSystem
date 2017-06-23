package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 22/06/2017.
 */

public class Registro {
    String idUser;
    String idPlace;
    String placeType;
    String placeName;

    public Registro(String idUser, String idPlace, String placeName, String placeType) {
        this.idUser = idUser;
        this.idPlace = idPlace;
        this.placeType = placeType;
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

}

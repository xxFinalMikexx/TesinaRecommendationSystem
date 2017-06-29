package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 11/06/2017.
 */
 
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import java.util.List;

public class InfoPlace {
    private CharSequence name;
    private String placeId;
    private LatLng latlng;
    private JSONArray placeTypes;
    private double rating;
    private String photo;
    private String direccion;
    private JSONArray reviews;

    public InfoPlace() {

    }

    public InfoPlace(CharSequence name, String placeId, LatLng latlng, JSONArray placeTypes, double rating, String photo, String direccion, JSONArray reviews) {
        this.name = name;
        this.placeId = placeId;
        this.latlng = latlng;
        this.placeTypes = placeTypes;
        this.rating = rating;
        this.photo = photo;
        this.direccion = direccion;
        this.reviews = reviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public JSONArray getPlaceTypes() {

        return placeTypes;
    }

    public void setPlaceTypes(JSONArray placeTypes) {
        this.placeTypes = placeTypes;
    }

    public LatLng getLatlng() {

        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public CharSequence getName() {

        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setReviews(JSONArray reviews) {
        this.reviews = reviews;
    }

    public JSONArray getReviews() {
        return this.reviews;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String toString() {
        return "Nombre: " + this.name + "\nPlaceId"  + this.placeId;
    }
}

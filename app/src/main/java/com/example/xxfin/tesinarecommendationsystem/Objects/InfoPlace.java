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

    public InfoPlace() {

    }

    public InfoPlace(CharSequence name, String placeId, LatLng latlng, JSONArray placeTypes, double rating) {
        this.name = name;
        this.placeId = placeId;
        this.latlng = latlng;
        this.placeTypes = placeTypes;
        this.rating = rating;
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
    
    public String toString() {
        return "Nombre: " + this.name + "\nPlaceId"  + this.placeId;
    }
}

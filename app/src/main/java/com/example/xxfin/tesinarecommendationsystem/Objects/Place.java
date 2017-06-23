package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 22/06/2017.
 */

public class Place {
    private String keyPlace;
    private String nameP;
    private int visitsNumber;
    private int position;
    private float numberMatchWords;
    private double associationVal;
    private String placeCategory;


    public Place(String keyPlace, String namePlace, int numPos, String placeCategory) {
        this.keyPlace = keyPlace;
        this.nameP = namePlace;
        this.position=numPos;
        this.visitsNumber=0;
        this.numberMatchWords=0;
        this.placeCategory = placeCategory;
    }

    public Place(String keyLugar, int numPos, String categoriaDeLugar) {
        this.keyPlace = keyLugar;
        this.nameP = "";
        this.position=numPos;
        this.visitsNumber=0;
        this.numberMatchWords=0;
        this.placeCategory = categoriaDeLugar;
    }

    public Place(String keyLugar, String nameLugar, String categoriaDeLugar) {
        this.keyPlace = keyLugar;
        this.nameP = nameLugar;
        this.position=0;
        this.visitsNumber=0;
        this.numberMatchWords=0;
        this.placeCategory = categoriaDeLugar;
    }


    public void setNumberVisits(int numD){
        this.visitsNumber=numD;
    }

    public int getNumberVisits(){
        return this.visitsNumber;
    }

    public int getPosition(){
        return this.position;
    }

    public void increaseVisits(int n){
        this.visitsNumber+=n;
    }

    public void increasNumberMatchs(float n){
        this.numberMatchWords+=n;
    }

    public float getNumberMatchs(){
        return this.numberMatchWords;
    }

    public void setAsociationValue(double associationVa){
        associationVal = associationVa;
    }

    public double getAssociationVal(){
        return this.associationVal;
    }

    public String getName(){
        return this.nameP;
    }

    public String getKeyPlace(){
        return this.keyPlace;
    }

    public String getCategory(){
        return this.placeCategory;
    }
}
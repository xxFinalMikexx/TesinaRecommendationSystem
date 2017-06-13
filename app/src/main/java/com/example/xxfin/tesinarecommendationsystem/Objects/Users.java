package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 11/06/2017.
 */

public class Users {
    private String nombre;
    private String genero;
    private int rangoEdad;
    private int tipo;
    private String email;

    public Users() {

    }
    public Users(String nombre, String genero, int rangoEdad, int tipo, String email) {
        this.nombre = nombre;
        this.genero = genero;
        this.rangoEdad = rangoEdad;
        this.tipo = tipo;
        this.email = email;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getRangoEdad() {

        return rangoEdad;
    }

    public void setRangoEdad(int rangoEdad) {
        this.rangoEdad = rangoEdad;
    }

    public String getGenero() {

        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}

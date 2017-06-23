package com.example.xxfin.tesinarecommendationsystem.Objects;

/**
 * Created by xxfin on 11/06/2017.
 */
 
 public class Comments {
    private String commentId;
    private String userId;
    private String placeId;
    private String photoRef;
    private String likeVisit;
    private String firstTime;
    private String comments;
    private String tipoUsuario;
    private String califEmociones;
    private double calificacion;
    
    public Comments() {
    
    }
  
    public Comments(String commentId, String userId, String placeId, String photoRef, String likeVisit, String firstTime, String comments, String tipoUsuario, String califEmociones, double calificacion) {
        this.commentId = commentId;
        this.userId = userId;
        this.placeId = placeId;
        this.photoRef = photoRef;
        this.likeVisit = likeVisit;
        this.firstTime = firstTime;
        this.comments = comments;
        this.tipoUsuario = tipoUsuario;
        this.califEmociones = califEmociones;
        this.calificacion = calificacion;
    }
    
    public void setCalifEmociones(String califEmociones) {
        this.califEmociones = califEmociones;
    }
    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }
    public void setLikeVisit(String likeVisit) {
        this.likeVisit = likeVisit;
    }
    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getCalifEmociones() {
        return this.califEmociones;
    }
    public double getCalificacion() {
        return this.calificacion;
    }
    public String getTipoUsuario() {
        return this.tipoUsuario;
    }
    public String getCommentId() {
        return this.commentId;
    }
    public String getUserId() {
        return this.userId;
    }
    public String getPlaceId() {
        return this.placeId;
    }
    public String getPhotoRef() {
        return this.photoRef;
    }
    public String getLikeVisit() {
        return this.likeVisit;
    }
    public String getFirstTime() {
        return this.firstTime;
    }
    public String getComments() {
        return this.comments;
    }
 }

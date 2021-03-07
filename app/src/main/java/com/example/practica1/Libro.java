package com.example.practica1;

import java.util.ArrayList;

public class Libro {
    private String ISBN;
    private String titulo;
    private String imagen;
    private String editorial;
    private String autores;
    private String descripcion;
    private int numHojas;
    private String idioma;
    private String previewLink;
    private Double rating;

    public Libro(String ISBN, String title, String thumbnail, String autores, String editorial, String descripcion, int numHojas, String idioma, String previewLink,double rating) {
        this.ISBN=ISBN;
        this.titulo = title;
        this.imagen = thumbnail;
        this.autores = autores;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.numHojas = numHojas;
        this.idioma = idioma;
        this.previewLink = previewLink;
        this.rating = rating;
    }
    public Libro(String ISBN, String title,String autores,String editorial,String descripcion, String imagen, String preview){
        this.ISBN=ISBN;
        this.titulo=title;
        this.editorial=editorial;
        this.descripcion=descripcion;
        this.imagen=imagen;
        this.previewLink=preview;
        this.autores=autores;
    }
    // creating getter and setter methods
    public String getTitle() {
        return titulo;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }

    public String getAutores(){
        return autores;
    }

    public String getISBN() { return ISBN; }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getThumbnail() {
        return imagen;
    }

    public void setThumbnail(String thumbnail) {
        this.imagen = thumbnail;
    }

    public String getEditorial(){
        return this.editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getNumHojas() { return numHojas; }
    public void setNumHojas(int numHojas) { this.numHojas = numHojas; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getPreviewLink() { return previewLink; }
    public void setPreviewLink(String previewLink) { this.previewLink = previewLink; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

}

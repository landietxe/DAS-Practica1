package com.example.practica1;

public class Libro {
    private String titulo;
    private String imagen;

    public Libro(String title, String thumbnail) {
        this.titulo = title;
        this.imagen = thumbnail;
    }
    // creating getter and setter methods
    public String getTitle() {
        return titulo;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }


    public String getThumbnail() {
        return imagen;
    }

    public void setThumbnail(String thumbnail) {
        this.imagen = thumbnail;
    }


}

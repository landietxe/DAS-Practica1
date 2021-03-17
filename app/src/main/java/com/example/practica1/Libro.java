package com.example.practica1;

import java.util.ArrayList;



//Clase auxiliar para guardar la información de los libros.
public class Libro {
    private String ISBN;
    private String titulo;
    private String imagen;
    private String editorial;
    private String autores;
    private String descripcion;
    private String idioma;
    private String previewLink;


    public Libro(String ISBN, String title,String autores,String editorial,String descripcion, String imagen, String preview){
        //Constructor de la clase Libro
        this.ISBN=ISBN;
        this.titulo=title;
        this.editorial=editorial;
        this.descripcion=descripcion;
        this.imagen=imagen;
        this.previewLink=preview;
        this.autores=autores;
    }
    // Métodos get para obtener los atributos de la clase
    public String getTitle() {
        return titulo;
    }
    public String getAutores(){
        return autores;
    }
    public String getISBN() { return ISBN; }
    public String getThumbnail() {
        return imagen;
    }
    public String getEditorial(){
        return this.editorial;
    }
    public String getDescripcion() { return descripcion; }
    public String getIdioma() { return idioma; }
    public String getPreviewLink() { return previewLink; }

}

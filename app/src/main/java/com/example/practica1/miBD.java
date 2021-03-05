package com.example.practica1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class miBD extends SQLiteOpenHelper {
    public miBD(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("CREATE TABLE Libreria ('Palabra' VARCHAR(255) PRIMARY KEY NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE Libro ('ISBN' VARCHAR(16) PRIMARY KEY NOT NULL, 'Titulo' VARCHAR(255), 'Autor' VARCHAR(255), 'Editorial' VARCHAR(255), 'Descripción' TEXT(800),'Imagen' VARCHAR(255),'Preview' VARCHAR(255));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String comprobarLibro(String ISBN){
        System.out.println("-..........................");
        SQLiteDatabase bd = getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Libro WHERE ISBN='"+ISBN+"'", null);
        String respuesta="";
        if(c.moveToNext()){
            respuesta = c.getString(0);
        }
        //cerrar conexiones y cursores
        c.close();
        bd.close();
        return respuesta;
    }
    public void insertarLibro(String ISBN,String titulo, String autor,String editorial,String descripcion,String imagen,String preview){
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("ISBN",ISBN);
        content.put("Titulo",titulo);
        content.put("Autor",autor);
        content.put("Editorial",editorial);
        content.put("Descripción",descripcion);
        content.put("Imagen",imagen);
        content.put("Preview",preview);
        bd.insert("Libro", null, content);
    }

}

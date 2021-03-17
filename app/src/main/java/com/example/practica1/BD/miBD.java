package com.example.practica1.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.practica1.Libro;

import java.sql.SQLOutput;
import java.util.ArrayList;
//Clase que extiende SQLiteOpenHelper para gestionar la base de datos de la aplicación
public class miBD extends SQLiteOpenHelper {
    public miBD(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*Método que se ejecuta cuando hay que crear la BD porque no existe.
        Por un lado, se crean las tablas "Usuarios", "Libro" y "Usurio_Libro" y después
        se añade un usuario de prueba a la tabla Usuarios.*/
        sqLiteDatabase.execSQL("CREATE TABLE Usuarios ( user_id INTEGER PRIMARY KEY AUTOINCREMENT,username varchar(255) NOT NULL,password varchar(255) NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE Libro ('ISBN' VARCHAR(16) PRIMARY KEY NOT NULL, 'Titulo' VARCHAR(255), 'Autor' VARCHAR(255), 'Editorial' VARCHAR(255), 'Descripción' TEXT(800),'Imagen' VARCHAR(255),'Preview' VARCHAR(255));");
        sqLiteDatabase.execSQL("CREATE TABLE Usuario_Libro ( user_id INTEGER NOT NULL REFERENCES Usuarios(user_id),ISBN VARCHAR(16) NOT NULL REFERENCES Libro(ISBN), PRIMARY KEY(user_id,ISBN));");
        sqLiteDatabase.execSQL("INSERT INTO Usuarios(username,password) VALUES ('prueba','123')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String comprobarLibroUsuario(String ISBN,String user_id){
        /*Método que ejecuta una sentencia SELECT en la base de datos para comprobar si el
        usuario actual ya tiene el libro indicando mediante el ISBN en su biblioteca. En caso de qeu esté en la base de datos,
        devuelve su ISBN, en caso contrario devuelve un string vacío.*/
        SQLiteDatabase bd = getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Libro l JOIN Usuario_Libro ul ON l.ISBN = ul.ISBN JOIN Usuarios u ON u.user_id = ul.user_id WHERE u.user_id='"+user_id+"' AND l.ISBN='" + ISBN + "'", null);
        String respuesta="";
        if(c.moveToNext()){
            respuesta = c.getString(0);
        }
        //cerrar conexiones y cursores
        c.close();
        bd.close();
        return respuesta;
    }

    public String comprobarLibro(String ISBN){
        /*Método que ejecuta una sentencia SELECT en la base de datos para comprobar si el
    libro con el ISBN indicado ya se encuentra en la base de datos. En caso de qeu esté en la base de datos,
    devuelve su ISBN, en caso contrario devuelve un string vacío.*/
        SQLiteDatabase bd = getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Libro l WHERE l.ISBN='" + ISBN + "'", null);
        String respuesta="";
        if(c.moveToNext()){
            respuesta = c.getString(0);
        }
        //cerrar conexiones y cursores
        c.close();
        bd.close();
        return respuesta;

    }

    public void insertarLibro(String ISBN,String titulo, String autor,String editorial,String descripcion,String imagen,String preview,String user_id){
            /*Método que ejecuta una sentencia INSERT para asignar un nuevo libro a un usuario.
       En primer lugar, se comprueba que el libro no esté en la base de datos, y de ser así se añade
       un nuevo libro a la tabla "Libro". Después se añade ese libro al usuario actual en la tabla "Usuario_Libro".*/
        String libroIsbn = this.comprobarLibro(ISBN);
        SQLiteDatabase bd = getWritableDatabase();
        if ("".equals(libroIsbn)){ //El libro no está en la base de datos
            ContentValues content = new ContentValues();
            content.put("ISBN",ISBN);
            content.put("Titulo",titulo);
            content.put("Autor",autor);
            content.put("Editorial",editorial);
            content.put("Descripción",descripcion);
            content.put("Imagen",imagen);
            content.put("Preview",preview);
            bd.insert("Libro", null, content);

        }//Si el libro está en la base de datos (por que otro usuario lo tiene) no hay que volver a meterlo.
        //Asignamos el libro al usuario
        ContentValues content2 = new ContentValues();
        content2.put("user_id",user_id);
        content2.put("ISBN",ISBN);
        bd.insert("Usuario_Libro", null, content2);

        //Cerrar conexión
        bd.close();
    }
    public ArrayList<Libro> getLibros(String user_id, String orden){
        /*Método que ejecuta una sentencia SELECT para obtener los libros que el usuario actual tiene en su biblioteca.
        Por cada libro se creará una instancia de la clase "Libro" y se irán añadiendo a un ArrayList para devolverlo al final.*/
        ArrayList<Libro> listalibros = new ArrayList<Libro>();
        SQLiteDatabase bd = getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Libro l JOIN Usuario_Libro ul ON l.ISBN = ul.ISBN JOIN Usuarios u ON u.user_id = ul.user_id WHERE u.user_id='"+user_id+"' ORDER BY l."+orden+" ASC", null);
        while (c.moveToNext()){
            String ISBN = c.getString(0);
            String titulo = c.getString(1);
            String autor = c.getString(2);
            String editorial = c.getString(3);
            String descripcion = c.getString(4);
            String imagen = c.getString(5);
            String preview = c.getString(6);
            Libro l= new Libro(ISBN,titulo,autor,editorial,descripcion,imagen,preview);
            listalibros.add(l);
        }
        //Cerrar conexiones
        c.close();
        bd.close();

        return listalibros;
    }

    public void borrarUsuarioLibro(String ISBN,String user_id) {
        /*Método que ejecuta una sentencia DELETE para borrar de la tabla "Usuario_Libro" el libro indicado con el usuario actual.
    Una vez borrado un libro a un usuario, se comprueba si ese libro lo tiene algun otro usuario, y en caso de que nadie lo tenga
    también se borrará de la tabla "Libro".*/
        SQLiteDatabase bd = getWritableDatabase();
        bd.execSQL("DELETE FROM Usuario_Libro WHERE user_id ='"+user_id+"' AND ISBN='"+ISBN+"'");
        Cursor c = bd.rawQuery("SELECT * FROM Libro l JOIN Usuario_Libro ul ON l.ISBN = ul.ISBN JOIN Usuarios u ON u.user_id = ul.user_id WHERE l.ISBN='" + ISBN +"'", null);
        if (!c.moveToNext()){ //El libro ya no lo tiene ningun usuario, por lo tanto se puede borrar de la base de datos
            this.borrarLibro(ISBN);
        }
        c.close();
        bd.close();
    }

    public void borrarLibro(String ISBN){
        /* Método que ejecuta una sentencia DELETE para borrar un libro de la tabla "libro" con el ISBN indicado.*/
        SQLiteDatabase bd = getWritableDatabase();
        bd.execSQL("DELETE FROM Libro WHERE ISBN='"+ISBN+"'");
        bd.close();

    }

    public int getUsuario(String username, String password){
        /*Método que ejecuta una sentencia SELECT para comprobar si ya existe un usuario
      en la tabla "Usuarios" con el nombre y contraseña indicados. De ser así,  devuelve el identificador del usuario.
      En caso contrario devuelve -1;*/
        SQLiteDatabase bd = getWritableDatabase();
        int id = -1;
        Cursor c = bd.rawQuery("SELECT user_id FROM Usuarios WHERE username='"+username+"' AND password='" + password+"';", null);
        if(c.moveToNext()){
            id = c.getInt(0);
        }
        bd.close();
        return id;
    }

    public void insertarUsuario(String user, String password) {
        /* Método que ejecuta una sentencia INSERT para añadir un nuevo usuario a la
        tabla "Usuarios" con el nombre y contraseña indicados.*/
        SQLiteDatabase bd = getWritableDatabase();
        bd.execSQL("INSERT INTO Usuarios(username,password) VALUES ('"+user+"','"+password+"')");
        bd.close();

    }


}

package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.practica1.Dialogos.DialogoConfirmarBorrar;
import com.example.practica1.Fragments.fragmentBiblioteca;
import com.example.practica1.Fragments.fragmentInfoLibroBibliotecaLand;
import com.example.practica1.R;
import com.example.practica1.BD.miBD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/*Actividad que muestra los libros que el usuario tenga añadidos en su biblioteca. Esta actividad está compuesta de fragments
* para tener un comportamiento diferente según la orientación en la que se encuentre el móvil*/
public class MainActivityBiblioteca extends AppCompatActivity implements fragmentBiblioteca.listenerDelFragment,fragmentInfoLibroBibliotecaLand.listener2,
        DialogoConfirmarBorrar.ListenerdelDialogo{

    private String ISBN;
    private miBD gestorDB;
    private String user_id;
    private String ordenLibros;
    private Toolbar toolbar;
    private String nombreUsuario="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Obtener preferencias de idioma para actualizar los elementos del layout según el idioma y el orden
        en el que se quiere ordenar los libros*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es"); //Idioma
        String orden = prefs.getString("orden","title"); //Orden
        this.ordenLibros=orden;

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        //Establecer la vista "activity_main_biblioteca.xml"
        setContentView(R.layout.activity_main_biblioteca);

        //Obtener nombre del Usuario
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuario_actual.txt")));
            ficherointerno.readLine();
            String linea = ficherointerno.readLine();
            this.nombreUsuario= linea.split(":")[1];
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Añadir toolbar al layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.nombreUsuario);
        setSupportActionBar(toolbar);

        //Obtener la base de datos de la aplicación
        gestorDB = new miBD(this, "Libreria", null, 1);

    }

    @Override
    public void seleccionarElemento(String isbn, String title, String autores, String editorial, String descripcion, String thumbnail, String previewLink) {
        /*Método que se ejecuta cuando el usuario selecciona uno de sus libros. Por un lado se comprueba la orientación en la que
        se encuentra el móvil. Si el móvil está en vertical, se abrirá la actividad "InfoLibroBiblioteca" pasandole los datos del libro.
        Si el móvil está en horizontal, ya existe otro fragment en el layout, por lo que se hace cast a su clase y se llama al método
        "actualizar" para visualizar los datos.*/
        this.ISBN=isbn;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){ //Pantalla en horizontal, usamos el otro fragment
            fragmentInfoLibroBibliotecaLand elotro=(fragmentInfoLibroBibliotecaLand) getSupportFragmentManager().findFragmentById(R.id.fragment4);
            elotro.actualizar(isbn,title,autores,editorial,descripcion,previewLink);

        }
        else{ //Pantalla en vertical, abrimos nueva actividad
            Intent i= new Intent(this,InfoLibroBiblioteca.class);
            i.putExtra("isbn",isbn);
            i.putExtra("titulo", title);
            i.putExtra("autor", autores);
            i.putExtra("editorial", editorial);
            i.putExtra("descripcion", descripcion);
            i.putExtra("imagen", thumbnail);
            i.putExtra("previewlink",previewLink);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Método que se ejecuta cuando se pulsa alguno de los botones de la Toolbar.
        int id=item.getItemId();
        switch (id){
            case R.id.opcion1:{  //Botón buscar, abrirá la actividad "MainActivity"
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id",this.user_id);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.opcion2:{//Boton Ajustes,abrirá la actividad "PreferenciasActivity"
                Intent intent = new Intent(this, PreferenciasActivity.class);
                startActivity(intent);

            }

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Asignar el fichero xml con la definición del menú a la Toolbar
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public void seleccionarElemento() {
        /*Método implementado del listener de fragmentInfoBibliotecaLand"
        El método se ejecutará cuando el usuario pulse el botón de borrar el libro cuando tenga el móvil en orientación
        horizontal y abrirá un diálogo de la clase "DialogoConfirmarBorrar"*/
        DialogFragment dialogoalerta= new DialogoConfirmarBorrar();
        dialogoalerta.show(getSupportFragmentManager(), "etiqueta");
    }

    @Override
    public void alpulsarSI() {
        /*Método que se ejecuta cuando el usuario pulsa el botoón "Sí" en el dialogo de borrar el libro
        de su biblioteca. Por un lado, se lee del fichero "usuario_actual.txt" cual es el identificador del usuario actual. Con ese identificador,
        se llama al método "borrarUsuarioLibro" de la base de datos para quitar el libro al usuario. Después se abre una notificación
        indicando que el libro ha sido borrado. Por último, se recarga la actividad.*/

        //Obtener identificador del usuario actual
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuario_actual.txt")));
            String linea = ficherointerno.readLine();
            this.user_id= linea.split(":")[1]; //id:num
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Quitar libro a usuario
        gestorDB.borrarUsuarioLibro(this.ISBN,this.user_id);

        //Crear notificación indicando que se ha eliminado el libro de la biblioteca
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "Notificación libro",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.setDescription("Notificación libro eliminado");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(false);

            elManager.createNotificationChannel(elCanal);
        }

        String contentTitle = getString(R.string.notificacionBorrar1);
        String contentText =  getString(R.string.notificacionBorrar2);
        String subText = getString(R.string.notificacionBorrar1);

        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSubText(subText)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        elManager.notify(1, elBuilder.build());

        //Recargar la actividad
        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }

    @Override
    public void alpulsarNO() {
        /* Método que se ejecuta cuando el usuario pulsa el bóton "No" en el diálogo de borrar el libro
        de su biblioteca. Se abrirá un Toast indicando que el libro no se ha borrado.*/
        String mensaje = getString(R.string.toastNoBorrado);
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }

    @Override
    public void onBackPressed(){
        /*Método que se ejecuta cuando el usuario pulsa el bóton del móvil para volver hacia atras.
          El método abrirá la actividad anterior a la actual, en este caso, "LoginActivity" y finalizará la
          actividad actual.*/
        Context context = getApplicationContext();
        Intent newIntent = new Intent(context, LoginActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        finish();
    }
}
package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practica1.Dialogos.DialogoConfirmarBorrar;
import com.example.practica1.R;
import com.example.practica1.BD.miBD;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
/*Actividad que muestra la información de un libro seleccionado desde el recyclerview de la clase "MainAcitvityBiblioteca" que
 contiene los libros añadidos por el usuario. La actividad permite borrar el libro de la biblioteca del usuario o ver su previsualización.
 */
public class InfoLibroBiblioteca extends AppCompatActivity implements DialogoConfirmarBorrar.ListenerdelDialogo {

    //Elementos del layout
    private TextView tvTitulo;
    private TextView tvAutor;
    private TextView tvEditorial;
    private TextView tvDescripcion;
    private ImageView imagen;

    //Base de datos
    private miBD gestorDB;

    //Información del libro
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private String descripcion;
    private String urlImagen;
    private String preview;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtener preferencias de idioma para actualizar los elementos del layout según el idioma
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es");

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        //Establecer la vista "activity_info_libro_biblioteca.xml"
        setContentView(R.layout.activity_info_libro_biblioteca);

        //Obtener la base de datos de la aplicación
        gestorDB = new miBD(this, "Libreria", null, 1);

        //Obtener referencias a los elementos del layout
        tvTitulo = (TextView) findViewById(R.id.info_libro_titulo);
        tvAutor = (TextView) findViewById(R.id.info_libro_autor);
        tvEditorial = (TextView) findViewById(R.id.info_libro_editorial);
        tvDescripcion = (TextView) findViewById(R.id.info_libro_descripcion);
        imagen = (ImageView) findViewById(R.id.info_libro_imagen);

        //Obtener información pasada desde la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String isbn = extras.getString("isbn");
            String titulo = extras.getString("titulo");
            String autor = extras.getString("autor");
            String editorial = extras.getString("editorial");
            String descripcion = extras.getString("descripcion");
            String imagen = extras.getString("imagen");
            String preview = extras.getString("previewlink");

            this.ISBN = isbn;
            this.titulo = titulo;
            this.autor = autor;
            this.editorial = editorial;
            this.descripcion = descripcion;
            this.urlImagen = imagen;
            this.preview = preview;

            this.tvTitulo.setText(titulo);
            this.tvAutor.setText(autor);
            this.tvEditorial.setText(editorial);
            this.tvDescripcion.setText(descripcion);

            //Cargar la imagen
            Picasso.get().load(imagen.replace("http", "https")).into(this.imagen);


        }
    }

    public void onClickBorrar(View v){
        /*Método que se ejecuta cuando el usuario pulsa el botón de borrar el libro de su biblioteca.
        Se abrirá un diálogo de la clase "DialogoConfirmarBorrar" para que el usuario confirme si quiere borrar el libro.*/
        DialogFragment dialogoalerta= new DialogoConfirmarBorrar();
        dialogoalerta.show(getSupportFragmentManager(), "etiqueta");
    }

    @Override
    public void alpulsarSI() {
        /*Método que se ejecuta cuando el usuario pulsa el botoón "Sí" en el dialogo de borrar el libro
        de su biblioteca. Por un lado, se lee del fichero "usuario_actual.txt" cual es el identificador del usuario actual. Con ese identificador,
        se llama al método "borrarUsuarioLibro" de la base de datos para quitar el libro al usuario. Después se abre una notificación
        indicando que el libro ha sido borrado. Por último, se vuelve a abrir la actividad "MainActivityBiblioteca".*/

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

        //Abrir actividad "MainActivityBiblioteca"
        Intent newIntent = new Intent(this, MainActivityBiblioteca.class);
        startActivity(newIntent);
        finish();


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
          El método abrirá la actividad anterior a la actual, en este caso, MainActivityBiblioteca y finalizará la
          actividad actual.*/
        Context context = getApplicationContext();
        Intent newIntent = new Intent(context, MainActivityBiblioteca.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        finish();
    }

    public void onClickPreview(View v){
        /*Método que se ejecuta cuando el usuario pulsa el botón de previsualizar el libro.
        Este método abre un intent implícito que muestra en el navegador una previsualización del libro.*/
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(this.preview));
        startActivity(i);
    }

}
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

import com.example.practica1.Dialogos.DialogoConfirmar;
import com.example.practica1.R;
import com.example.practica1.BD.miBD;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/*Actividad que muestra la información de un libro seleccionado desde el recyclerview de la clase "MainAcitvity" y que permite
añadir el libro a la biblioteca del usuario o ver su previsualización.
 */
public class InfoLibro extends AppCompatActivity implements DialogoConfirmar.ListenerdelDialogo {

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

        //Establecer la vista "activity_info_libro.xml"
        setContentView(R.layout.activity_info_libro);

        //Obtener la base de datos de la aplicación
        gestorDB = new miBD (this, "Libreria", null, 1);

        //Obtener referencias a los elementos del layout
        tvTitulo = (TextView) findViewById(R.id.info_libro_titulo);
        tvAutor = (TextView) findViewById(R.id.info_libro_autor);
        tvEditorial = (TextView) findViewById(R.id.info_libro_editorial);
        tvDescripcion = (TextView) findViewById(R.id.info_libro_descripcion);
        imagen = (ImageView)  findViewById(R.id.info_libro_imagen);

        //Obtener información pasada desde la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String isbn = extras.getString("isbn");
            String titulo= extras.getString("titulo");
            String autor= extras.getString("autor");
            String editorial= extras.getString("editorial");
            String descripcion= extras.getString("descripcion");
            String imagen= extras.getString("imagen");
            String preview = extras.getString("previewlink");

            this.ISBN = isbn;
            this.titulo=titulo;
            this.autor=autor;
            this.editorial=editorial;
            this.descripcion=descripcion;
            this.urlImagen = imagen;
            this.preview=preview;

            this.tvTitulo.setText(titulo);
            this.tvAutor.setText(autor);
            this.tvEditorial.setText(editorial);
            this.tvDescripcion.setText(descripcion);

            //Cargar la imagen
            Picasso.get().load(imagen.replace("http", "https")).into(this.imagen);
        }
    }

    public void onClickAñadir(View v){
        /* Método que se ejecuta cuando el usuario pulsa el botón para añadir el libro a su biblioteca.
        Por un lado, se lee del fichero "usuario_actual.txt" cual es el identificador del usuario actual. Con ese identificador,
        se comprueba si el usuario ya tiene ese libro en su biblioteca. En ese caso, se mostrará un Toast indicándolo. En caso de
        que no lo tenga, se abrirá un diálogo de la clase "DialogoConfirmar" para que el usuario confirme si quiere añadir el libro o no.
         */

        //Obtener identificador del usuario actual
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuario_actual.txt")));
            String linea = ficherointerno.readLine();
            this.user_id= linea.split(":")[1]; //id:num
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Comprobar si el usuario ya tiene el libro
        String respuesta =gestorDB.comprobarLibroUsuario(this.ISBN,this.user_id);
        if(respuesta.equals("")){//Si el libro no se encuentra en la base de datos
            //Abrir diálogo
            DialogFragment dialogoalerta= new DialogoConfirmar();
            dialogoalerta.show(getSupportFragmentManager(), "etiqueta");
        }
        else{ //El libro ya se encuentra en la base de datos
            String mensaje = getString(R.string.info1);
            Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
            toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    @Override
    public void alpulsarSI() {
        /*Método que se ejecuta cuando el usuario pulsa el botoón "Sí" en el dialogo de añadir el libro a su biblioteca.
        Por un lado, se ejecutará el método "insertarLibro" de la base de datos para añadir el libro a su biblioteca y después
        se mostrará una notificación indicando que el libro ha sido añadido.
         */

        //Guardar libro
        gestorDB.insertarLibro(this.ISBN,this.titulo,this.autor,this.editorial,this.descripcion,this.urlImagen,this.preview,this.user_id);

        //Crear notificación indicando que se ha añadido un nuevo libro a la libreria
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "Notificación libro",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.setDescription("Notificación libro añadido");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(false);

            elManager.createNotificationChannel(elCanal);
        }

        String contentTitle = getString(R.string.notificacion1);
        String contentText =  getString(R.string.notificacion2);
        String subText = getString(R.string.notificacion3);

        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSubText(subText)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        elManager.notify(1, elBuilder.build());
    }

    @Override
    public void alpulsarNO() {
        /* Método que se ejecuta cuando el usuario pulsa el bóton "No" en el diálogo de añadir el libro
        a su biblioteca. Se abrirá un Toast indicando que el libro no se ha añadido.*/
        String mensaje = getString(R.string.toastNoAñadido);
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }

    public void onClickPreview(View v){
        /*Método que se ejecuta cuando el usuario pulsa el botón de previsualizar el libro.
        Este método abre un intent implícito que muestra en el navegador una previsualización del libro.*/
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(this.preview));
        startActivity(i);
    }

}
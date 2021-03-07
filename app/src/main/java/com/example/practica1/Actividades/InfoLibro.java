package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practica1.DialogoConfirmar;
import com.example.practica1.R;
import com.example.practica1.miBD;
import com.squareup.picasso.Picasso;

public class InfoLibro extends AppCompatActivity implements DialogoConfirmar.ListenerdelDialogo {

    private TextView tvTitulo;
    private TextView tvAutor;
    private TextView tvEditorial;
    private TextView tvDescripcion;
    private ImageView imagen;

    private miBD gestorDB;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private String descripcion;
    private String urlImagen;
    private String preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_libro);

        gestorDB = new miBD (this, "Libreria", null, 1);

        tvTitulo = (TextView) findViewById(R.id.info_libro_titulo);
        tvAutor = (TextView) findViewById(R.id.info_libro_autor);
        tvEditorial = (TextView) findViewById(R.id.info_libro_editorial);
        tvDescripcion = (TextView) findViewById(R.id.info_libro_descripcion);
        imagen = (ImageView)  findViewById(R.id.info_libro_imagen);

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
            Picasso.get().load(imagen.replace("http", "https")).into(this.imagen);

        }


    }

    public void onClickAñadir(View v){
        System.out.println("prueba");
        String respuesta =gestorDB.comprobarLibro(this.ISBN);
        if(respuesta.equals("")){//Si el libro no se encuentra en la base de datos
            //gestorDB.insertarLibro(this.ISBN,this.titulo,this.autor,this.editorial,this.descripcion);
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
        gestorDB.insertarLibro(this.ISBN,this.titulo,this.autor,this.editorial,this.descripcion,this.urlImagen,this.preview);

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
        String mensaje = getString(R.string.toastNoAñadido);
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }

    public void onClickPreview(View v){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(this.preview));
        startActivity(i);
    }

}
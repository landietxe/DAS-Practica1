package com.example.practica1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InfoLibro extends AppCompatActivity {

    private TextView titulo;
    private TextView autor;
    private TextView editorial;
    private TextView descripcion;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_libro);
        titulo = (TextView) findViewById(R.id.info_libro_titulo);
        autor = (TextView) findViewById(R.id.info_libro_autor);
        editorial = (TextView) findViewById(R.id.info_libro_editorial);
        descripcion = (TextView) findViewById(R.id.info_libro_descripcion);
        imagen = (ImageView)  findViewById(R.id.info_libro_imagen);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String titulo= extras.getString("titulo");
            String autor= extras.getString("autor");
            String editorial= extras.getString("editorial");
            String descripcion= extras.getString("descripcion");
            String imagen= extras.getString("imagen");
            this.titulo.setText(titulo);
            this.autor.setText(autor);
            this.editorial.setText(editorial);
            this.descripcion.setText(descripcion);
            System.out.println(imagen);
            Picasso.get().load(imagen.replace("http", "https")).into(this.imagen);

        }


    }
}
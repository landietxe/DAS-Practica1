package com.example.practica1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica1.Actividades.InfoLibro;
import com.example.practica1.ViewHolder.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*Clase para obtener un recyclerview que visualize los libros que se consiguen a partir
de la búsqueda del usuario. Esta clase recibe los datos que se quieren mostrar en la lista
y los asigna a los atributos de la clase.
 */
public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder>  {
    private ArrayList<Libro> listaLibros;
    private Context context;

    public AdaptadorRecycler(ArrayList<Libro> listaLibros, Context context) {
        /*Método constructor en el que se reciben los datos que se quieren mostrar en el recyclerview. En este caso, un ArrayList de libros.*/
        this.listaLibros = listaLibros;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* "Infla" el layout definido para cada elemento y crea y devuelve una instancia de la clase que extiende a ViewHolder*/
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder view = new ViewHolder(layoutItem);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Asigna a los atributos del ViewHolder los valores a mostrar para una posición concreta

        Libro libro = listaLibros.get(position);
        holder.eltexto.setText(libro.getTitle());
        holder.autores.setText(libro.getAutores());
        holder.editorial.setText(libro.getEditorial());

        /*Cargar la imagen utilizando la libreria Picasso
        Obtenido de https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library : "Loading an Image from Url"*/
        String url = libro.getThumbnail().replace("http", "https");
        Picasso.get().load(url).into(holder.laimagen);

        //Listener para gestionar la interacción con una fila del recyclerview
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Método que se ejecuta cuando se pulsa en una fila del recyclerview. Por un lado
                se guarda la información  que se pasará a la siguiente actividad y se abre la actividad "InfoLibro".*/
                Intent i = new Intent(context, InfoLibro.class);
                i.putExtra("isbn",libro.getISBN());
                i.putExtra("titulo", libro.getTitle());
                i.putExtra("autor", libro.getAutores());
                i.putExtra("editorial", libro.getEditorial());
                i.putExtra("descripcion", libro.getDescripcion());
                i.putExtra("imagen", libro.getThumbnail());
                i.putExtra("previewlink",libro.getPreviewLink());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        //la cantidad de datos total a mostrar
        return listaLibros.size();
    }
}

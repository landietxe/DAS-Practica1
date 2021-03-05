package com.example.practica1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder>  {
    private ArrayList<Libro> listaLibros;
    private Context context;
    //private boolean[] seleccionados;

    public AdaptadorRecycler(ArrayList<Libro> listaLibros, Context context) {
        this.listaLibros = listaLibros;
        this.context = context;
        System.out.println(listaLibros.size());
        //seleccionados=new boolean[listaLibros.size()];
    }

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder view = new ViewHolder(layoutItem);
        //view.seleccion = seleccionados;
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Libro libro = listaLibros.get(position);
        holder.eltexto.setText(libro.getTitle());
        holder.autores.setText(libro.getAutores().toString());
        holder.editorial.setText(libro.getEditorial());
        String url = libro.getThumbnail().replace("http", "https");
        Picasso.get().load(url).into(holder.laimagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                Intent i = new Intent(context, InfoLibro.class);
                i.putExtra("isbn",libro.getISBN());
                i.putExtra("titulo", libro.getTitle());
                i.putExtra("autor", libro.getAutores());
                i.putExtra("editorial", libro.getEditorial());
                i.putExtra("descripcion", libro.getDescripcion());
                i.putExtra("imagen", libro.getThumbnail());
                i.putExtra("previewlink",libro.getPreviewLink());
                context.startActivity(i);
            }
        });


        //System.out.println(libro.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }
}

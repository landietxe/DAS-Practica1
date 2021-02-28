package com.example.practica1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Libro> listaLibros;
    private Context mcontext;

    public AdaptadorRecycler(ArrayList<Libro> listaLibros, Context mcontext) {
        this.listaLibros = listaLibros;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder view = new ViewHolder(layoutItem);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Libro libro = listaLibros.get(position);
        holder.eltexto.setText(libro.getTitle());
        //Picasso.get().load(bookInfo.getThumbnail()).into(holder.laimagen);
        String url = libro.getThumbnail().replace("http", "https");
        Picasso.get().load(url).into(holder.laimagen);
        System.out.println(libro.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }
}

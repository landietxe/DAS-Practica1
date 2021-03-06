package com.example.practica1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorRecyclerBiblioteca extends RecyclerView.Adapter<ViewHolderBiblioteca> {
    private ArrayList<Libro> listaLibros;
    private Context context;

    public AdaptadorRecyclerBiblioteca(ArrayList<Libro> listaLibros, Context context) {
        this.listaLibros = listaLibros;
        this.context = context;
        System.out.println(listaLibros.size());
    }


    @NonNull
    @Override
    public ViewHolderBiblioteca onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_biblioteca_layout, parent, false);
        ViewHolderBiblioteca view = new ViewHolderBiblioteca(layoutItem);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBiblioteca holder, int position) {

        Libro libro = listaLibros.get(position);
        String url = libro.getThumbnail().replace("http", "https");
        Picasso.get().load(url).into(holder.laimagen);

    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }
}

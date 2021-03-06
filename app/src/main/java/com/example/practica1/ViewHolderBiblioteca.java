package com.example.practica1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderBiblioteca extends RecyclerView.ViewHolder{
    public TextView eltexto;
    public ImageView laimagen;
    public TextView autores;
    public TextView editorial;
    public boolean[] seleccion;

    public ViewHolderBiblioteca(@NonNull View itemView) {
        super(itemView);
        laimagen = itemView.findViewById(R.id.biblioteca_imagen);
    }
}

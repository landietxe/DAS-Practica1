package com.example.practica1;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView eltexto;
    public ImageView laimagen;
    public TextView autores;
    public TextView editorial;
    public boolean[] seleccion;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        eltexto = itemView.findViewById(R.id.libro_titulo);
        laimagen = itemView.findViewById(R.id.libro_imagen);
        autores = itemView.findViewById(R.id.libro_autor);
        editorial = itemView.findViewById(R.id.libro_editorial);
    }
}

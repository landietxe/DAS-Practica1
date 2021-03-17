package com.example.practica1.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica1.R;

/*ViewHolder de la clase AdaptadorRecyclerBiblioteca*/
public class ViewHolderBiblioteca extends RecyclerView.ViewHolder{
    public ImageView laimagen;
    public ViewHolderBiblioteca(@NonNull View itemView) {
        /*Constructor de la clase para hacer la asociación entre los
        campos de la clase y los elementos gráficos del layout*/
        super(itemView);
        laimagen = itemView.findViewById(R.id.biblioteca_imagen);
    }
}

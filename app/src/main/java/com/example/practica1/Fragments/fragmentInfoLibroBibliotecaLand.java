package com.example.practica1.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.practica1.DialogoConfirmar;
import com.example.practica1.DialogoConfirmarBorrar;
import com.example.practica1.R;
import com.example.practica1.miBD;

public class fragmentInfoLibroBibliotecaLand extends Fragment{
    private miBD gestorDB;
    private String isbn;
    private String previewLink;
    private listener2 elListener;


    public interface listener2{
        void seleccionarElemento();
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            elListener=(fragmentInfoLibroBibliotecaLand.listener2) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " +context.toString()
                    + "debe implementar listenerDelFragment");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_info_biblioteca_land,container,false);
        gestorDB = new miBD(getActivity(), "Libreria", null, 1);
        return v;
    }
    public void actualizar(String isbn,String title,String autores,String StringEditorial, String StringDescripcion,String previewLink){
        this.isbn=isbn;
        TextView titulo = (TextView) getActivity().findViewById(R.id.info_libro_titulo);
        TextView autor = (TextView) getActivity().findViewById(R.id.info_libro_autor);
        TextView editorial = (TextView) getActivity().findViewById(R.id.info_libro_editorial);
        TextView descripcion = (TextView) getActivity().findViewById(R.id.info_libro_descripcion);
        Button botonPreview = (Button) getActivity().findViewById(R.id.botonPreview);
        Button boton = (Button)getActivity().findViewById(R.id.button);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("askdjfhaskldjfhalskjdfhalksjdf");
                elListener.seleccionarElemento();
            }
        });

        botonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(previewLink));
                startActivity(i);
            }
        });

        TextView tvTitulo = (TextView) getActivity().findViewById(R.id.tvTitulo);
        TextView tvAutores = (TextView) getActivity().findViewById(R.id.tvAutor);
        TextView tvEditorial = (TextView) getActivity().findViewById(R.id.tvEditorial);

        tvTitulo.setVisibility(View.VISIBLE);
        tvAutores.setVisibility(View.VISIBLE);
        tvEditorial.setVisibility(View.VISIBLE);
        boton.setVisibility(View.VISIBLE);
        botonPreview.setVisibility(View.VISIBLE);

        titulo.setText(title);
        autor.setText(autores);
        editorial.setText(StringEditorial);
        descripcion.setText(StringDescripcion);
    }

}

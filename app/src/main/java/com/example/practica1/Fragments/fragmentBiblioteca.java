package com.example.practica1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica1.Libro;
import com.example.practica1.R;
import com.example.practica1.ViewHolder.ViewHolderBiblioteca;
import com.example.practica1.BD.miBD;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Fragment que muestra los libros que el usuario tiene en su biblioteca.
public class fragmentBiblioteca extends Fragment {

    private RecyclerView recyclerViewBiblioteca;
    private AdaptadorRecyclerBiblioteca eladaptador;
    private ArrayList<Libro> bookInfoArrayList;
    private miBD gestorDB;
    private TextView tvVacio;
    private listenerDelFragment elListener;
    private String user_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Enlazar la clase java del fragment con el fichero "fragment_biblioteca.xml"
        View v= inflater.inflate(R.layout.fragment_biblioteca,container,false);
        return v;
    }

    //Interfaz con el método del listener para la comunicación con el fragment definido en la actividad "MainActivityBiblioteca"
    public interface listenerDelFragment{
        void seleccionarElemento(String isbn, String title, String autores, String editorial, String descripcion, String thumbnail, String previewLink);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvVacio = (TextView) getView().findViewById(R.id.tvVacio);
        recyclerViewBiblioteca = (RecyclerView)getView().findViewById(R.id.recyclerViewBiblioteca);

        //Obtener identificador del usuario actual
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(getActivity().openFileInput("usuario_actual.txt")));
            String linea = ficherointerno.readLine();
            this.user_id= linea.split(":")[1]; //id:num
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Obtener preferencia de ordenar libros, por defecto se ordenarán utilizando el título.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orden = prefs.getString("orden","Titulo");

        //Obtener referencia a la base de datos
        gestorDB = new miBD (getActivity(), "Libreria", null, 1);
        //Conseguir la lista de libros del usuario
        bookInfoArrayList = gestorDB.getLibros(this.user_id,orden);

        //Si no hay ningun libro, mensaje indicando como añadir uno nuevo
        if(bookInfoArrayList.isEmpty()){
            tvVacio.setVisibility(View.VISIBLE);
        }
        else{
            tvVacio.setVisibility(View.INVISIBLE);
        }

        eladaptador = new AdaptadorRecyclerBiblioteca(bookInfoArrayList,getActivity());

        //Establecer cómo se desea que se organicen los elementos dentro del RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewBiblioteca.setLayoutManager(linearLayoutManager);
        recyclerViewBiblioteca.setAdapter(eladaptador);
    }

    public void onAttach(Context context) {
        //Método para unir el listener con los métodos implementado en la actividad
        super.onAttach(context);
        try{
            elListener=(listenerDelFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " +context.toString()
                    + "debe implementar listenerDelFragment");
        }
    }

    //Clase interna para obtener un recyclerview que visualize los libros del usuario y pueda ser accedido desde el fragment
    public class AdaptadorRecyclerBiblioteca extends RecyclerView.Adapter<ViewHolderBiblioteca> {
        private ArrayList<Libro> listaLibros;
        private Context context;

        /*Método constructor en el que se reciben los datos que se quieren mostrar en el recyclerview. En este caso, un ArrayList de libros.*/
        public AdaptadorRecyclerBiblioteca(ArrayList<Libro> listaLibros, Context context) {
            this.listaLibros = listaLibros;
            this.context = context;
        }


        @NonNull
        @Override
        public ViewHolderBiblioteca onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /* "Infla" el layout definido para cada elemento y crea y devuelve una instancia de la clase que extiende a ViewHolder*/
            View layoutItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_biblioteca_layout, parent, false);
            ViewHolderBiblioteca view = new ViewHolderBiblioteca(layoutItem);
            return view;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderBiblioteca holder, int position) {
            //Asigna a los atributos del ViewHolder los valores a mostrar para una posición concreta
            Libro libro = listaLibros.get(position);
            String url = libro.getThumbnail().replace("http", "https");
            Picasso.get().load(url).into(holder.laimagen);

            //Listener para gestionar la interacción con una elemento del recyclerview
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Método que se ejecuta cuando se pulsa en una elemento del recyclerview. Este método
                    asigna el método "seleccionarElemento" del listener.*/
                    elListener.seleccionarElemento(libro.getISBN(),libro.getTitle(),libro.getAutores(),libro.getEditorial(),libro.getDescripcion(),libro.getThumbnail(),libro.getPreviewLink());
                }
            });
            }

        @Override
        //la cantidad de datos total a mostrar
        public int getItemCount() {
            return listaLibros.size();
        }
    }
}

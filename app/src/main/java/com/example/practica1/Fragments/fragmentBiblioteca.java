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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica1.Actividades.MainActivity;
import com.example.practica1.Libro;
import com.example.practica1.R;
import com.example.practica1.ViewHolderBiblioteca;
import com.example.practica1.miBD;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        View v= inflater.inflate(R.layout.fragment_biblioteca,container,false);
        return v;
    }


    public interface listenerDelFragment{
        void seleccionarElemento(String isbn, String title, String autores, String editorial, String descripcion, String thumbnail, String previewLink);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        tvVacio = (TextView) getView().findViewById(R.id.tvVacio);
        recyclerViewBiblioteca = (RecyclerView)getView().findViewById(R.id.recyclerViewBiblioteca);

        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(getActivity().openFileInput("usuario_actual.txt")));
            String linea = ficherointerno.readLine();
            this.user_id= linea.split(":")[1]; //id:num
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orden = prefs.getString("orden","title");

        gestorDB = new miBD (getActivity(), "Libreria", null, 1);
        bookInfoArrayList = gestorDB.getLibros(this.user_id,orden);

        //Si no hay ningun libro, mensaje indicando como añadir uno nuevo
        if(bookInfoArrayList.isEmpty()){
            tvVacio.setVisibility(View.VISIBLE);
        }
        else{
            tvVacio.setVisibility(View.INVISIBLE);
        }

        eladaptador = new AdaptadorRecyclerBiblioteca(bookInfoArrayList,getActivity());
        //GridLayoutManager elLayoutRejillaIgual= new GridLayoutManager(getActivity(),1, GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewBiblioteca.setLayoutManager(linearLayoutManager);
        recyclerViewBiblioteca.setAdapter(eladaptador);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            elListener=(listenerDelFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " +context.toString()
                    + "debe implementar listenerDelFragment");
        }
    }

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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    elListener.seleccionarElemento(libro.getISBN(),libro.getTitle(),libro.getAutores(),libro.getEditorial(),libro.getDescripcion(),libro.getThumbnail(),libro.getPreviewLink());
                }
            });
            }

        @Override
        public int getItemCount() {
            return listaLibros.size();
        }
    }
}

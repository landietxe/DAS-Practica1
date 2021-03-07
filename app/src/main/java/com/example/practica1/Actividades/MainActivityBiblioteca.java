package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.practica1.Actividades.MainActivity;
import com.example.practica1.AdaptadorRecyclerBiblioteca;
import com.example.practica1.Libro;
import com.example.practica1.R;
import com.example.practica1.miBD;

import java.util.ArrayList;

public class MainActivityBiblioteca extends AppCompatActivity {

    private RecyclerView recyclerViewBiblioteca;
    private AdaptadorRecyclerBiblioteca eladaptador;
    private ArrayList<Libro> bookInfoArrayList;
    private miBD gestorDB;
    private TextView tvVacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_biblioteca);

        //PARA HACER PRUEBAS----------------------
        //this.deleteDatabase("Libreria");
        //-----------------------------------------

        tvVacio = (TextView) findViewById(R.id.tvVacio);
        recyclerViewBiblioteca = (RecyclerView)findViewById(R.id.recyclerViewBiblioteca);

        gestorDB = new miBD (this, "Libreria", null, 1);
        bookInfoArrayList = gestorDB.getLibros();

        if(bookInfoArrayList.isEmpty()){
            tvVacio.setVisibility(View.VISIBLE);
        }
        else{
            tvVacio.setVisibility(View.INVISIBLE);
        }



        eladaptador = new AdaptadorRecyclerBiblioteca(bookInfoArrayList,this);
        GridLayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recyclerViewBiblioteca.setLayoutManager(elLayoutRejillaIgual);
        recyclerViewBiblioteca.setAdapter(eladaptador);
    }
    public void onClickBuscar(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
package com.example.practica1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practica1.R;

//Fragment que muestra la información de un libro seleccionado cuando el móvil se encuentra en orientación vertical.
public class fragmentInfoLibroBiblioteca extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Enlazar la clase java del fragment con el fichero "fragment_info_biblioteca.xml"
        View v= inflater.inflate(R.layout.fragment_info_biblioteca,container,false);
        return v;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}

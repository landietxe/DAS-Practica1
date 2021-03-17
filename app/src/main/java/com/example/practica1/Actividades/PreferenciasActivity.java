package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.practica1.R;

import java.util.Locale;

/*Actividad que contiene un fragment para mostrar y editar
    las preferencias del usuario*/
public class PreferenciasActivity extends AppCompatActivity {

    /*Método que se ejecuta al crear la  actividad.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtener preferencias de idioma para actualizar los elementos del layout según el idioma.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es");
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        //Establecer la vista "activity_prefencias.xml"
        setContentView(R.layout.activity_preferencias);
    }

    @Override
    public void onBackPressed(){
        /*Método que se ejecutará cuando el usuario pulse el botón del movil para volver atras.
        Abrirá la Actividad "MainActivityBiblioteca" y cerrará la actividad actual.*/
        Context context = getApplicationContext();
        Intent newIntent = new Intent(context, MainActivityBiblioteca.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        finish();
    }

    public void onClickGuardar(View v){
            /*Método que se ejecutará cuando el usuario pulse el botón "Guardar".
        Abrirá la Actividad "MainActivityBiblioteca" y cerrará la actividad actual.*/
        Intent intent = new Intent(this, MainActivityBiblioteca.class);
        startActivity(intent);
        finish();
    }
}
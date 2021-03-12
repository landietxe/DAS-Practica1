package com.example.practica1.Actividades;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.practica1.R;
import com.example.practica1.miBD;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText contraseña;
    private miBD gestorDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es");

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());


        setContentView(R.layout.activity_login);

        usuario = (EditText)findViewById(R.id.editTextUsuario);
        contraseña = (EditText) findViewById(R.id.editTextTextPassword);

        //PARA HACER PRUEBAS----------------------
        //this.deleteDatabase("Libreria");
        //-----------------------------------------

        gestorDB = new miBD(this, "Libreria", null, 1);
    }


    public void onClickLogin(View v){

        String user=usuario.getText().toString();
        String password = contraseña.getText().toString();
        int id = gestorDB.getUsuario(user,password);

        if(id != -1){ //El usuario existe
            System.out.println("EL USUARIO EXISTE");

            try {
                OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuario_actual.txt",
                        Context.MODE_PRIVATE));
                fichero.write("id:"+id+"\n"+"Usuario:"+user);
                fichero.close();
            } catch (IOException e){

            }
            Intent intent = new Intent(this,MainActivityBiblioteca.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("id",id);
            startActivity(intent);
        }
        else{ //El usuario no existe o la contraseña es incorrecta
            String mensaje = getString(R.string.ususuarioContraseña);
            Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
            toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

        }
    }

    public void onClickRegistrar(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        //intent.putExtra("id",id);
        startActivity(intent);
    }
}
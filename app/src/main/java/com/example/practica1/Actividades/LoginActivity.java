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
import com.example.practica1.BD.miBD;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

/*Actividad que permite al usuario iniciar sesión con un nombre y una contraseña
 o abrir otra actividad para registrarse.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText contraseña;
    private miBD gestorDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtener preferencias de idioma para actualizar los elementos del layout según el idioma
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es");

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        //Establecer la vista "activity_login.xml"
        setContentView(R.layout.activity_login);

        //Obtener referencias a los elementos del layout
        usuario = (EditText)findViewById(R.id.editTextUsuario);
        contraseña = (EditText) findViewById(R.id.editTextTextPassword);

        //Obtener la base de datos de la aplicación
        gestorDB = new miBD(this, "Libreria", null, 1);
    }


    public void onClickLogin(View v){
        /*Método que se ejecuta cuando el usuario pulsa en el botón de Login.
    Por un lado, se obtienen los datos que el usuario ha introducido y se llama al método
    "getUsuario" de la base de datos para comprobar si con esos datos existe algún usuario.
    Si el usuario existe, se escribe en el fichero externo "usuario_actual.txt" el nombre del usuario y
    su identificador y a continuación se abre la actividad "MainActivityBiblioteca".
    Si el usuario no se ha encontrado en la base de datos se mostrará un Toast.*/

        String user=usuario.getText().toString();
        String password = contraseña.getText().toString();

        //Obtener identificador del usuario de la base de datos
        int id = gestorDB.getUsuario(user,password);

        if(id != -1){ //El usuario existe
            try {
                //Escribir en fichero externo el identificado y nombre del usuario
                OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuario_actual.txt", Context.MODE_PRIVATE));
                fichero.write("id:"+id+"\n"+"Usuario:"+user);
                fichero.close();
            } catch (IOException e){

            }
            //Abrir la actividad MainActivityBiblioteca
            Intent intent = new Intent(this,MainActivityBiblioteca.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{ //Si el usuario no existe o la contraseña es incorrecta mostrar un Toast
            String mensaje = getString(R.string.ususuarioContraseña);
            Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
            toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }
    public void onClickRegistrar(View v){
        /*Método que se ejecuta cuando el usuario pulsa en el botón de registrarse.Este método
    abrira la actividad "RegisterActivity" para que el usuario se pueda registrar.*/
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practica1.R;
import com.example.practica1.BD.miBD;

import java.util.Locale;

/*Actividad que permite al usuario registrarse en la aplicación con un nombre de usuario
    y una contraseña.*/
public class RegisterActivity extends AppCompatActivity {

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

        //Establecer la vista "activity_register.xml"
        setContentView(R.layout.activity_register);

        //Obtener referencias a los elementos del layout
        usuario = (EditText)findViewById(R.id.editTextUsuario);
        contraseña = (EditText) findViewById(R.id.editTextTextPassword);
        gestorDB = new miBD(this, "Libreria", null, 1);
    }

    public void onClickRegistrar(View v){
        /*Método que se ejecuta cuando el usuario pulsa el botón de registrarse.
        Este método en primer lugar comprueba si ya existe un usuario con el nombre y contraseña introducidos.
        Si ya existe, se muestra un Toast indicándolo. En caso contrario, se añade el usuario a la base de datos
        utilizando el método "insertarUsuario" definido en la clase "miBD".*/
        String user=usuario.getText().toString();
        String password = contraseña.getText().toString();

        if(!user.equals("")){
            int id = gestorDB.getUsuario(user,password);
            if(id != -1){ //El usuario existe
                String mensaje = getString(R.string.usuarioContraseña2);
                Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
                toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
            else{ //El usuario no existe
                gestorDB.insertarUsuario(user,password);
                Intent intent = new Intent(this,LoginActivity.class);
                //intent.putExtra("id",id);
                startActivity(intent);
            }
        }

    }
}
package com.example.practica1.Actividades;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practica1.R;
import com.example.practica1.miBD;

public class LoginActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText contraseña;
    private miBD gestorDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Intent intent = new Intent(this,MainActivityBiblioteca.class);
            //intent.putExtra("id",id);
            startActivity(intent);
        }
        else{ //El usuario no existe o la contraseña es incorrecta
            System.out.println("EL USUARIO NO EXISTE O LA CONTRASEÑA ES INCORRECTA");
        }
    }

    public void onClickRegistrar(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        //intent.putExtra("id",id);
        startActivity(intent);
    }
}
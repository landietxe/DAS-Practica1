package com.example.practica1.Preferencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.preference.PreferenceFragmentCompat;

import com.example.practica1.R;

/*Clase que hereda de PreferenceFragmentCompat para permitir almacenar las
preferencias al usuario en la aplicación.
 */
public class Preferencias extends PreferenceFragmentCompat {
    @Override
    //Método para indicar cual es el fichero XML donde están definidas las preferencias(pref_config.xml)
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_config);
    }
}

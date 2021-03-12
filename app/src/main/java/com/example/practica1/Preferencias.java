package com.example.practica1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.preference.PreferenceFragmentCompat;


public class Preferencias extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_config);
    }
}

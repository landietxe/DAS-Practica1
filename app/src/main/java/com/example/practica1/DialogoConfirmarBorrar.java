package com.example.practica1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.practica1.R;

public class DialogoConfirmarBorrar extends DialogFragment {
    ListenerdelDialogo miListener;
    public interface ListenerdelDialogo {
        void alpulsarSI();
        void alpulsarNO();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener =(ListenerdelDialogo) getActivity();

        //Obtener string según el idioma para el texto del dialog
        String titulo = getString(R.string.Borrar);
        String texto= getString(R.string.borrarBilbioteca);
        String si = getString(R.string.Si);
        String no = getString(R.string.No);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo);
        builder.setMessage(texto);


        builder.setPositiveButton(si, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            miListener.alpulsarSI();

        }

        });
        builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                miListener.alpulsarNO();
            }

        });


        return builder.create();
    }
}

package com.example.practica1.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.practica1.R;

/*Clase para crear una ventana emergente con un diálogo para confirmar si se quiere
    eliminar el libro seleccionado o no.
 */
public class DialogoConfirmarBorrar extends DialogFragment {
    ListenerdelDialogo miListener;

    /*Interfaz del diálogo para que las acciones se ejecuten
    en la actividad que llamó al dialogo*/
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
        //Crear  un AlertDialog con el estilo "AlertDialogCustom"
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        builder.setTitle(titulo);
        builder.setMessage(texto);

        //Establecer boton para confirmar la acción del usuario.
        builder.setPositiveButton(si, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //Método a ejecutar cuando el usuario pulsa el botón afirmando la acción.
            miListener.alpulsarSI();
        }
        });
        //Establecer boton para negar la acción del usuario.
        builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Método a ejecutar cuando el usuario pulsa el botón negando la acción.
                miListener.alpulsarNO();
            }

        });

        return builder.create();
    }
}

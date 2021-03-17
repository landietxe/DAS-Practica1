package com.example.practica1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practica1.R;
import com.example.practica1.BD.miBD;

//Fragment que muestra la información de un libro seleccionado cuando el móvil se encuentra en orientación horizontal.
public class fragmentInfoLibroBibliotecaLand extends Fragment{
    private miBD gestorDB;
    private String isbn;
    private String previewLink;
    private listener2 elListener;

    //Interfaz con el método del listener para la comunicación con el fragment definido en la actividad "MainActivityBiblioteca"
    public interface listener2{
        void seleccionarElemento();
    }


    public void onAttach(Context context) {
        //Método para unir el listener con los métodos implementado en la actividad
        super.onAttach(context);
        try{
            elListener=(fragmentInfoLibroBibliotecaLand.listener2) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " +context.toString()
                    + "debe implementar listenerDelFragment");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Enlazar la clase java del fragment con el fichero "fragment_biblioteca.xml"
        View v= inflater.inflate(R.layout.fragment_info_biblioteca_land,container,false);
        //Obtener referencia a la base de datos de la aplicación
        gestorDB = new miBD(getActivity(), "Libreria", null, 1);
        return v;
    }
    public void actualizar(String isbn,String title,String autores,String StringEditorial, String StringDescripcion,String previewLink){
        /*Método llamado desde la clase MainActivityBiblioteca cuando la orientación del móvil es horizontal
        y el usuario selecciona uno de los libros. Este método obtiene las referencias a los elementos del layout, los hace visibles
        y los actualiza con los datos recibimos como parámetros.*/

        //Obtener referencias a los elementos del layout
        this.isbn=isbn;
        TextView titulo = (TextView) getActivity().findViewById(R.id.info_libro_titulo);
        TextView autor = (TextView) getActivity().findViewById(R.id.info_libro_autor);
        TextView editorial = (TextView) getActivity().findViewById(R.id.info_libro_editorial);
        TextView descripcion = (TextView) getActivity().findViewById(R.id.info_libro_descripcion);
        Button botonPreview = (Button) getActivity().findViewById(R.id.botonPreview);
        Button boton = (Button)getActivity().findViewById(R.id.button);

        TextView tvTitulo = (TextView) getActivity().findViewById(R.id.tvTitulo);
        TextView tvAutores = (TextView) getActivity().findViewById(R.id.tvAutor);
        TextView tvEditorial = (TextView) getActivity().findViewById(R.id.tvEditorial);
        TextView tvDescripcion = (TextView) getActivity().findViewById(R.id.tvDescripcion);

        //Listener del bóton Borrar. Ejecutará el método seleccionarElemento del listener
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elListener.seleccionarElemento();
            }
        });

        //Listener del bóton Preview. Se abre un intent implícito que muestra en el navegador una previsualización del libro.
        botonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(previewLink));
                startActivity(i);
            }
        });

        //Hacer visibles los elementos del layout
        tvTitulo.setVisibility(View.VISIBLE);
        tvAutores.setVisibility(View.VISIBLE);
        tvEditorial.setVisibility(View.VISIBLE);
        boton.setVisibility(View.VISIBLE);
        botonPreview.setVisibility(View.VISIBLE);
        tvDescripcion.setVisibility(View.VISIBLE);

        //Establecer la información del libro
        titulo.setText(title);
        autor.setText(autores);
        editorial.setText(StringEditorial);
        descripcion.setText(StringDescripcion);
    }

}

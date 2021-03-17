package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica1.AdaptadorRecycler;
import com.example.practica1.Libro;
import com.example.practica1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


/*Actividad que permite al usuario hacer una busqueda relacionada con libros tras lo cual se visualizarán los resultados
en un recyclerview. Si el usuario selecciona alguno se abrirá una nueva actividad con la información de ese libro.
 */
public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<Libro> bookInfoArrayList;
    private RecyclerView elreciclerview;
    private AdaptadorRecycler eladaptador;
    private EditText editTextLibro;

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
        setContentView(R.layout.activity_main);


        //Obtener referencias a los elementos del layout
        editTextLibro = (EditText) findViewById(R.id.editTextLibro);
        elreciclerview = (RecyclerView) findViewById(R.id.recyclerview);


        bookInfoArrayList = new ArrayList<>();
        //Establecer cómo se desea que se organicen los elementos dentro del RecyclerView
        eladaptador = new AdaptadorRecycler(bookInfoArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        elreciclerview.setLayoutManager(linearLayoutManager);
        elreciclerview.setAdapter(eladaptador);


    }

    private void inicializarRecyclerView() {
        //Este método crea el adaptador con los datos a mostrar y se los asigna al RecyclerView
        bookInfoArrayList = new ArrayList<>();
        eladaptador = new AdaptadorRecycler(bookInfoArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        elreciclerview.setLayoutManager(linearLayoutManager);
        elreciclerview.setAdapter(eladaptador);
    }

    public void onClickBuscar(View v){
        /*Método que se ejecuta cuando el usuario pulsa el bóton buscar.
        Este método recoge la sentencia de búsqueda introducida en el campo de texto,
        inicializa el adaptador del reyclerview y llama al método "obtenerDatos".*/

        //Obtener busqueda introducida por el usuario
        String busqueda = editTextLibro.getText().toString();

        inicializarRecyclerView();
        obtenerDatos(busqueda);
    }

    public void obtenerDatos(String query){
        /*Este método realiza una petición HTTP a la API de Google Books utilizando la búsqueda introducida por el usuario.
        Como respuesta de la petición obtendremos un  objeto JsonObjectRequest, del cual se conseguirá un Array con objetos Json que contienen
        la información de los libros encontrados con la búsqueda.

        Peticiones HTTP usando Volley :
        https://www.develou.com/android-volley-peticiones-http/

        Uso de la API de Google Books : "Working with volumes"
        https://developers.google.com/books/docs/v1/using */

        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        //Limpiar el cache
        requestQueue.getCache().clear();
        //Url con el que obtener los datos en formato JSON en el API de Google Books
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&printType=books";

        //Nueva petición JSONObject
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Obtener un JSONArray con el atributo llamado "items"
                    JSONArray itemsArray = response.getJSONArray("items");
                    //Recorrer todos los objetos JSON
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        //Obtener objeto JSON con los datos del libro
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        //Si la respuesta JSON no contiene ISBN y imagen, no vamos a recoger ese libro
                        if(volumeObj.has("industryIdentifiers") && volumeObj.has("imageLinks")){
                            String ISBN = volumeObj.getJSONArray("industryIdentifiers").optJSONObject(0).optString("identifier");//ISBN
                            String title = volumeObj.optString("title"); //Titulo
                            String editorial =  volumeObj.optString("publisher");//Editorial
                            //String idioma = volumeObj.optString("language"); //Idioma del libro
                            String previewLink = volumeObj.optString("previewLink"); //PreviewLink
                            //Double rating = volumeObj.optDouble("averageRating"); //Nota media
                            //int numHojas = volumeObj.optInt("pageCount"); //Número de hojas
                            String descripcion = volumeObj.optString("description"); //Descripción
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String thumbnail = imageLinks.optString("thumbnail"); //Imagen del libro
                            JSONArray arrayAutores = volumeObj.optJSONArray("authors"); //Autores
                            ArrayList<String> autores=new ArrayList<String>();
                            if (arrayAutores != null) {
                                for (int j = 0; j < arrayAutores.length(); j++) {
                                    autores.add(arrayAutores.optString(j));
                                }
                            }
                            //En caso de que haya varios autores, se guardaran como autor1,autor2,autor3
                            String stringAutores = android.text.TextUtils.join(",", autores);

                            //Crear una instancia de Libro con los datos conseguidos y añadirlos al ArrayList de libros.
                            Libro libro = new Libro(ISBN,title,stringAutores,editorial,descripcion,thumbnail,previewLink);
                            bookInfoArrayList.add(libro);
                            }
                        }
                    //Notificar al adaptador de que se ha actualizado el ArrayList
                    eladaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    //e.printStackTrace();
                    String toast = getString(R.string.errorBusqueda);
                    Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String toast = getString(R.string.errorBusqueda);
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
        // Añadir petición a la cola
        requestQueue.add(booksObjrequest);

    }

    @Override
    public void onBackPressed(){
        /*Método que se ejecuta cuando el usuario pulsa el bóton del móvil para volver hacia atras.
          El método abrirá la actividad anterior a la actual, en este caso, MainActivityBiblioteca y finalizará la
          actividad actual.*/
        Context context = getApplicationContext();
        Intent newIntent = new Intent(context, MainActivityBiblioteca.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        finish();
    }

}
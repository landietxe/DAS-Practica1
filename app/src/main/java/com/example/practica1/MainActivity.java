package com.example.practica1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private ArrayList<Libro> bookInfoArrayList;
    private RecyclerView elreciclerview;
    private AdaptadorRecycler eladaptador;
    private EditText editTextLibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLibro = (EditText) findViewById(R.id.editTextLibro);
        elreciclerview = (RecyclerView) findViewById(R.id.recyclerview);
        bookInfoArrayList = new ArrayList<>();
        eladaptador = new AdaptadorRecycler(bookInfoArrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        //GridLayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        //elreciclerview.setLayoutManager(elLayoutRejillaIgual);
        elreciclerview.setLayoutManager(linearLayoutManager);
        elreciclerview.setAdapter(eladaptador);

    }

    public void onClickBuscar(View v){
        String busqueda = editTextLibro.getText().toString();
        System.out.println(busqueda);
        editTextLibro = (EditText) findViewById(R.id.editTextLibro);
        elreciclerview = (RecyclerView) findViewById(R.id.recyclerview);
        bookInfoArrayList = new ArrayList<>();
        eladaptador = new AdaptadorRecycler(bookInfoArrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        //GridLayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        //elreciclerview.setLayoutManager(elLayoutRejillaIgual);
        elreciclerview.setLayoutManager(linearLayoutManager);
        elreciclerview.setAdapter(eladaptador);
        obtenerDatos(busqueda);

    }

    public void obtenerDatos(String query){

        //Inicialización de la variable para pedir el recurso
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        //Limpiar el cache
        mRequestQueue.getCache().clear();

        //Url con el que obtener los datos en formato JSON
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&printType=books";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        //System.out.println(volumeObj);
                        String title = volumeObj.optString("title"); //Titulo
                        String editorial =  volumeObj.optString("publisher");//Editorial
                        String idioma = volumeObj.optString("language"); //Idioma del libro
                        String previewLink = volumeObj.optString("previewLink"); //PreviewLink
                        Double rating = volumeObj.optDouble("averageRating"); //Nota media
                        int numHojas = volumeObj.optInt("pageCount"); //Número de hojas
                        String descripcion = volumeObj.optString("description"); //Descripción
                        JSONArray arrayAutores = volumeObj.optJSONArray("authors"); //Autores

                        ArrayList<String> autores=new ArrayList<String>();
                        if (arrayAutores != null) {
                            for (int j = 0; j < arrayAutores.length(); j++) {
                                autores.add(arrayAutores.optString(j));
                            }
                        }
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        if(imageLinks != null){
                            String thumbnail = imageLinks.optString("thumbnail");
                            Libro libro = new Libro(title,thumbnail,autores,editorial, descripcion, numHojas, idioma, previewLink, rating);
                            bookInfoArrayList.add(libro);
                        }
                    }
                    eladaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "No se ha encontrado información" + e, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(booksObjrequest);

    }
}
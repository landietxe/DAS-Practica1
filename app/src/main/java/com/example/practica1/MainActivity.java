package com.example.practica1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        // below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        if(imageLinks != null){
                            String thumbnail = imageLinks.optString("thumbnail");
                            Libro libro = new Libro(title,thumbnail);
                            bookInfoArrayList.add(libro);
                        }
                    }
                    eladaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);

    }
}
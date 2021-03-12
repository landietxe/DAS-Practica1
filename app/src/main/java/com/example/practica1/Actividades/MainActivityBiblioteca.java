package com.example.practica1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.practica1.DialogoConfirmarBorrar;
import com.example.practica1.Fragments.fragmentBiblioteca;
import com.example.practica1.Fragments.fragmentInfoLibroBibliotecaLand;
import com.example.practica1.R;
import com.example.practica1.miBD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivityBiblioteca extends AppCompatActivity implements fragmentBiblioteca.listenerDelFragment,fragmentInfoLibroBibliotecaLand.listener2,
        DialogoConfirmarBorrar.ListenerdelDialogo{

    private String ISBN;
    private miBD gestorDB;
    private String user_id;
    private String ordenLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma","es");
        String orden = prefs.getString("orden","title");
        this.ordenLibros=orden;

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main_biblioteca);

        setSupportActionBar(findViewById(R.id.toolbar));
        gestorDB = new miBD(this, "Libreria", null, 1);

    }

    @Override
    public void seleccionarElemento(String isbn, String title, String autores, String editorial, String descripcion, String thumbnail, String previewLink) {
        this.ISBN=isbn;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){ //Pantalla en horizontal
            System.out.println(title);
            //EL OTRO FRAGMENT EXISTE
            fragmentInfoLibroBibliotecaLand elotro=(fragmentInfoLibroBibliotecaLand) getSupportFragmentManager().findFragmentById(R.id.fragment4);
            elotro.actualizar(isbn,title,autores,editorial,descripcion,previewLink);

        }
        else{ //Pantalla en vertical, abrimos nueva actividad
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            Intent i= new Intent(this,InfoLibroBiblioteca.class);
            i.putExtra("isbn",isbn);
            i.putExtra("titulo", title);
            i.putExtra("autor", autores);
            i.putExtra("editorial", editorial);
            i.putExtra("descripcion", descripcion);
            i.putExtra("imagen", thumbnail);
            i.putExtra("previewlink",previewLink);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.opcion1:{
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id",this.user_id);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.opcion2:{//Ajustes
                Intent intent = new Intent(this, PreferenciasActivity.class);
                startActivity(intent);

            }

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public void seleccionarElemento() {
        System.out.println("HAS CLICKADO EN BORRAR");
        DialogFragment dialogoalerta= new DialogoConfirmarBorrar();
        dialogoalerta.show(getSupportFragmentManager(), "etiqueta");
    }

    @Override
    public void alpulsarSI() {

        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuario_actual.txt")));
            String linea = ficherointerno.readLine();
            this.user_id= linea.split(":")[1]; //id:num
            ficherointerno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gestorDB.borrarUsuarioLibro(this.ISBN,this.user_id);
        //Crear notificación indicando que se ha eliminado el libro de la biblioteca
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "Notificación libro",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.setDescription("Notificación libro eliminado");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(false);

            elManager.createNotificationChannel(elCanal);
        }

        String contentTitle = getString(R.string.notificacionBorrar1);
        String contentText =  getString(R.string.notificacionBorrar2);
        String subText = getString(R.string.notificacionBorrar1);

        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSubText(subText)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        elManager.notify(1, elBuilder.build());

        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }

    @Override
    public void alpulsarNO() {
        System.out.println("HA PULSADO QUE NO");
        String mensaje = getString(R.string.toastNoBorrado);
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }

    @Override
    public void onBackPressed(){
        Context context = getApplicationContext();
        Intent newIntent = new Intent(context, LoginActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        finish();
    }
}
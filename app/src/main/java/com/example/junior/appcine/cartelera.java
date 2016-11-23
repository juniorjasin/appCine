package com.example.junior.appcine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class cartelera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartelera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // debo recuperar los datos que me pasaron en la vista que me llamo
        Intent intent= getIntent();
        // con getExtras obtengo los datos que me pasaron en la mochila que cargue con putExtra en la vista que llamo a esta vista
        // y se guardan en un objeto de la clase Bundle
        Bundle bundle = intent.getExtras();


        //Log.d("QUE PASA ACA", "io no se");
        // verifico que el bundle no este vacio
        if(!bundle.isEmpty()){
            // bundle.get(MainActivity.EXTRA_MESSAGE); Asi lo hace el del tutorial
            // asi obtiene el dato de la mochila y le pongo la clave que setee en la clase que lo llamo
            // asi lo hacen en la documentacion
            String nombre_pelicula = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); // lo llama asi a EXTRA_MESSAGE porque es un public final statci string
            Log.d("nombre peli", nombre_pelicula);
            setTitle(nombre_pelicula);

        }
    }

}

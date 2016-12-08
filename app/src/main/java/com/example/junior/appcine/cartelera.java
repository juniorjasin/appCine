package com.example.junior.appcine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * CAMBIOS EN: build.gradle(Module:app)
 * Agregue yendo a file -> project structure y en la pestaña dependencias, seleccione una
 * library dependency y busque com.google.android.gms:play-services:10.0.0, la añadi y me la agrego
 * en build.gradle (Module:app) en dependencies. Lo habia agregado a mano pero no funcionaba porque
 * al precer tienen que conincidir las versiones (:10.0.0) con las otras dependencias y yo habia
 * puesto unas que no correspondian y de todos modos parece que la forma correcta de hacerlo es como
 * explique en project structure.
 *
 * CAMBIOS EN: AndroidManifest.xml
 *
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 *
 * FINE es para obtener mayor precision en la localizacion y COARSE(grueso) para obtener una
 * precision mucho menor.
 *
 * FALTA:   1) entender como toma los datos de la lat y long
 *          2) saber si esta o no activado gps y pedir en caso de no estarlo
 *          3) hacer algoritmo de cines cercanos.
 */



public class cartelera extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // objeto usado en onConnected que almacena la latitud y longitud
    Location mLastLocation = null;
    // objeto que inicializo y luego con este llamo a connect() en onStart y disconnect() en onStop
    GoogleApiClient mGoogleApiClient = null;
    // int que utilizo en onConnected para la parte de los permisos, no se bien para que sirve AVERIGUAR
    public static int MY_PERMISON = 0;

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


        /**
         * Inicializo el objeto de la clase GoogleApiClient con el Builder
         * AVERIGUAR bien que hacer cada parte
         */
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Log.d("1", "onCreate");

        // debo recuperar los datos que me pasaron en la vista que me llamo
        Intent intent = getIntent();
        // con getExtras obtengo los datos que me pasaron en la mochila que cargue con putExtra en la vista que llamo a esta vista
        // y se guardan en un objeto de la clase Bundle
        Bundle bundle = intent.getExtras();


        //Log.d("QUE PASA ACA", "io no se");
        // verifico que el bundle no este vacio
        if (!bundle.isEmpty()) {
            // bundle.get(MainActivity.EXTRA_MESSAGE); Asi lo hace el del tutorial
            // asi obtiene el dato de la mochila y le pongo la clave que setee en la clase que lo llamo
            // asi lo hacen en la documentacion
            String nombre_pelicula = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); // lo llama asi a EXTRA_MESSAGE porque es un public final statci string
            Log.d("nombre peli", nombre_pelicula);
            setTitle(nombre_pelicula);

        }
    }

    /**
     * redefinicion que se llama luego del metodo onCreate
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("onStart", "onStart");
    }

    /**
     * redefinicion que se llama cuando se cambia de activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.d("onStop", "onStop");
    }


    /**
     * Se llama luego de que es llamado el metodo connect() en onStart,
     * primero se piden los permisos para acceder al gps
     * y luego obtengo la latitud y longitud con el metodo getLastlocation que retorna un Location
     * en este objeto obtengo la latitud y longitud
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected:", "before checked permission");


        /**
         * Primero consulta si tiene los permisos (segun lo que lei, a partir de la API 23 las apps te piden
         * que les des los permisos necesarios al momento de usarlos, por eso por mas que le ponga en el manifest
         * que uso la localizacion me lo vuelve a preguntar, eso lei, pero AVERIGUAR). Como los permisos los tiene
         * que dar el usuario entra en el if la primera vez y salta un cartel que pide los permisos. Esto lo
         * tengo que hacer porque sino no compila cuando quiero llamar a getlastlocation. Este metodo me devuelve
         * un Location con el que accedo en otras cosas a lat y long y los muestro con un Toast.
         */
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{

                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISON);
            }

            return;
        }
        Log.d("onConnected:", "after checked permission");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_LONG).show();
            Log.d("milatitud", String.valueOf(mLastLocation.getLatitude()));
            Log.d("milongitud ", String.valueOf(mLastLocation.getLongitude()));

            //mLastLocation.getLatitude()
            //mLastLocation.getLongitude()
        }
        else{

            // message box pidiendo que active el gps, solo se activa si el gps no esta activado
            // y uno presiona ok y se queda ahi.
            // --> Hacer que siga preguntando hasta que este activado
            new AlertDialog.Builder(this)
                    .setTitle("posicion")
                    .setMessage("Active el gps por favor...")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended", "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", "");
    }
}

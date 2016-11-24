package com.example.junior.appcine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /* creo esta variable para pasarsela en el intent a la 2da activity, no es necesario que sea este tipo ni que sea
      public final static, pero segun la documentacion es recomendable que sea asi y que tenga el nombre com.example.junior.appcine
      de manera que sea unico y no se tenga inconvenientes si la app quiere interactuar con otras apps
     */
    public final static String EXTRA_MESSAGE = "com.example.junior.appcine";
    ListView listview;
    /** boolean para saber a que metodo llamar dependiendo si se consulto la tabla de peliculas o no
      peliculas = false -> todavia no se pidio la tabla peliculas
      peliculas = true -> ya se pidio la tabla y puede consultar la tabla de cines
    */

    // controlo a que metodo llamar en la clase JsonTask segun si se pidio consultar tabla de cines o peliculas
    boolean firstAction = false;

    // creo listas donde se alamcena los id de las peliculas y los nombres
    List<String> pelicula_id = new ArrayList<String>();
    List<String> peliculas = new ArrayList<String>();
    ArrayAdapter<String> adapterPeliculas;

    // creo un objeto para almacenar los objetos de la tabla cines
    Cine cine = new Cine();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
        Este metodo se invoca solo al abrir la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // http://piap.com.ar/1.php?key=y3rb44m4nd4&tabla=peliculas

        // obtengo un manejador para el listView que esta en la parte de diseño
        listview = (ListView) findViewById(R.id.listView);

        /** creo nueva instancia de la clase que esta metida dentro de esta clase, y llama a execute,
            AVERIGUAR que hace bien execute, en la parte de preocesos android que tengo marcada como favorito
            y ver en que momento se llama y cuales son los subprocesos y como funcionan como doInBackground
         */

        // le pego a la API que me deuvele la tabla de pelicuas
        new JsonTask().execute("http://piap.com.ar/1.php?key=y3rb44m4nd4&tabla=peliculas");
        // le pego a la API que me devuelve la tabla de cines
        new JsonTask().execute("http://piap.com.ar/1.php?key=y3rb44m4nd4&tabla=cines");

        // hago que el ListView escuche cual item se clickeo
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();

                Intent intent;
                // los parametros son (contexto, nombre del .java de la vista y .class)
                // pero tengo que poner NombreDeMiClase.this porque aca estoy adentro de otra clase y mi contexto no es this
                intent = new Intent(MainActivity.this, cartelera.class);
                // con putExtra le cargo al intent los datos que le quiero pasar a la otra vista, puedo hacer muchos de estos

                String selectedFromList = (String) listview.getItemAtPosition(i);

                intent.putExtra(EXTRA_MESSAGE, selectedFromList); // VER porque view.toString no tiene el nombre de la pelicula
                // inicio la otra actividad y le paso el intent que cree con o sin datos
                startActivity(intent);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
        Deriva de extends AsyncTask<String, String, String>, AVERIGUAR bien para que sirve esta clase, pero por lo que vi
        este como un sincrinizador de tareas (o como su nombre indica) donde heredo (creo) el metodo doInBackground,
        y redefino los onPre.onPost.
        Esta clase ejecuta el proceso de pegarle a la api (en background por lo que tengo entendido)
        ya que no lo puedo hacer directo llamando a un metodo (no se bien porque). en OnCreate creo un objeto
        de este tipo y le hago execute() que no es ningun metodo de esta clase. AVERIGUAR como funciona ese execute.
        Antes de llegar al punto donde hace un return buffer.toString() guardo lo que viene de la respuesta en una variable
        String que creo antes del OnCreate(), No entiendo quien llama a este metodo, ni a donde va a parar el buffer.String()
        que se retorna, AVERIGUAR.
        AVERIGUAR para que sirven onPreExecute() y el otro bien.
     */
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                // lo que se retorna aca se recibe en el metodo onPostExecute(String result)
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        /** este metodo se llama al termina de ejecutarse el metodo doInBackground()
           y aca esto se ejecuta dentro del UiThread y solo en uiThread puedo llamar a
            notifyDataSetChanged(). <Eso entendi, creo que es pasa eso, la verdad es que se llama esto>
        */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(firstAction == false) {
                setDataPeliculas(result);
            }
            else{
                setDataCines(result);
            }

            // ejecuto esto en el UiThread porque en este hilo es donde puedo actualizar los datos del Ui
            // para eso creo este metodo indicando la activity.this(contexto).runOnUiThread()
            // VERIFICAR que pasa si no seteo esto !, creo que va a andar igual pero no estaria demas tener esto
            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapterPeliculas.notifyDataSetChanged();
                }
            });
        }
    }


    /** este metdo se llama dentro de la clase JsonTask en onPostExecute y en json viene el json que recibio de la api
       decodifico los nombres y los id, los guardo en List<String>, antes de finalizar inicializo el adapter y se lo seteo
       al listview que ya tenia referenciado en el onCreate
    */
    public void setDataPeliculas(String json) {
        // aca adentro tengo que decodigicar el json, crear el adapter y setearselo al listview

        // indico que ya comenzo el proceso de obtener la tabla peliculas y puede obtener la de cines
        firstAction = true;

        //*
        JSONArray pages = null;
        try {
            pages = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int len = pages.length();
        String s = Integer.toString(len);
        Log.d("length JsonArray", s); // me dice 28, que es correcto, es la cant de peliculas

        for (int i = 0; i < pages.length(); ++i) {

            JSONObject rec = null;
            try {
                rec = pages.getJSONObject(i);
                Log.d("JsonObject", rec.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                pelicula_id.add(rec.getString("pelicula_id"));
                Log.d("id", pelicula_id.get(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                // decodigico los nombres de base64 a utf-8
                byte[] data = Base64.decode(rec.getString("nombre"), Base64.DEFAULT);
                String text = null;
                try {
                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                peliculas.add(text);
                Log.d("nombre", peliculas.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //*/

        // inicializo el adaptador y se lo seteo al listview
        adapterPeliculas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapterPeliculas.addAll(peliculas);
        listview.setAdapter(adapterPeliculas);
    }

    /** carga tabla cines recibida de la API al objeto Cine cine
     *  Invocado desde la clase JsonTask segundo si el boolean firstAction = true
     * @param json
     */
    public void setDataCines(String json) {
        // aca adentro tengo que decodigicar el json, crear el adapter y setearselo al listview

        //*
        JSONArray pages = null;
        try {
            pages = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int len = pages.length();
        String s = Integer.toString(len);
        Log.d("length JsonArray", s); // me dice 28, que es correcto, es la cant de peliculas

        for (int i = 0; i < pages.length(); ++i) {

            JSONObject rec = null;
            try {
                rec = pages.getJSONObject(i);
                Log.d("JsonObject", rec.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // decodifico cine_id
            try {
                cine.cine_id.add(rec.getString("cine_id"));
                Log.d("id", cine.cine_id.get(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // decodifico latitud
            try {
                cine.latitud.add(rec.getString("latitud"));
                Log.d("latitud", cine.latitud.get(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // decodifico longitud
            try {
                cine.longitud.add(rec.getString("longitud"));
                Log.d("longitud", cine.longitud.get(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // decodifico el nombre que venia en base64
            try {

                // decodigico los nombres de base64 a utf-8
                byte[] data = Base64.decode(rec.getString("nombre"), Base64.DEFAULT);
                String text = null;
                try {
                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                cine.nombre.add(text);
                Log.d("nombre", cine.nombre.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // vuevlo a poner fist action en false para cuando vuelve a entrar
        firstAction = false;

        /** tests
         * Log.d("test metodo nombreTS",cine.nombreToString()); // PASS
         * Log.d("test metodo ToString", cine.toString()); // PASS
         */
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.junior.appcine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.junior.appcine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}



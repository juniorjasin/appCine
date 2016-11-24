package com.example.junior.appcine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junior on 24/11/16.
 *
 * Clase para representar los datos de la tabla cines, con 4 List<String>,
 * sus metodos accesores, metodos toString de cada List y ToString que me arma cadena completa
 *  de todos los List atributos
 */
public class Cine {

    public List<String> cine_id;
    public List<String> nombre;
    public List<String> latitud;
    public List<String> longitud;

    // constructor solo inicializa los atributos
    public Cine(){
        cine_id = new ArrayList<String>();
        nombre = new ArrayList<String>();
        latitud = new ArrayList<String>();
        longitud = new ArrayList<String>();
    }


    // metodos toString

    // retorna una cadena con [i]: nombre id lat y long uno debajo del otro y separados por ------
    public String toString(){
        String cines = "";

        int l1 = this.nombre.size();
        int l2 = this.latitud.size();
        int l3 = this.longitud.size();
        int l4 = this.cine_id.size();

        // controlo que los vectores sen todos del mismo tamaño
        if( !(l1 == l2 && l2 == l3 && l3 == l4) ){
            cines += "Los vectores no son todos del mismo tamaño...";
            return cines;
        }

        // podrian tener todos el mismo tamaño pero de tamaño cero por eso controlo los tamaños
        if(this.cine_id.isEmpty() || this.latitud.isEmpty() || this.longitud.isEmpty() || this.nombre.isEmpty()) {
            cines = "No se cargaron datos en los atributos aun...";
            return cines;
        }

        // recorro todos los vectores y concateno
        int len = this.nombre.size();
        for(int i = 0; i < len; i++){
            cines += "[" + String.valueOf(i) + "]:";
            cines += this.nombre.get(i) + "\n";
            cines += "id: "+ this.cine_id.get(i) + "\n";
            cines += "latitud: "+ this.latitud.get(i) + "\n";
            cines += "longitud: "+ this.longitud.get(i) + "\n";
            cines += "--------------------------" + "\n";
        }
        return cines;
    }
    // obtengo todos los nombres separados uno debajo del otro // metodo solo para testing
    public String nombreToString(){
        String nombres = "";
        if(this.nombre.isEmpty()){
            nombres = "El vector de nombres esta vacio";
            return nombres;
        }

        for (String name: this.nombre) {
            nombres += name +"\n";
        }
        return nombres;
    }
    // obtengo todos los cine_id separados uno debajo del otro // metodo solo para testing
    public String cine_idToString(){
        String cine_id = "";
        if(this.cine_id.isEmpty()){
            cine_id = "El vector de cine_id esta vacio";
            return cine_id;
        }

        for (String name: this.cine_id) {
            cine_id += name +"\n";
        }
        return cine_id;
    }
    // obtengo todos las latitudes separados uno debajo del otro // metodo solo para testing
    public String latitudToString(){
        String latitud = "";
        if(this.latitud.isEmpty()){
            latitud = "El vector de latitudes esta vacio";
            return latitud;
        }

        for (String name: this.latitud) {
            latitud += name +"\n";
        }
        return latitud;
    }
    // obtengo todos las longitudes separados uno debajo del otro // metodo solo para testing
    public String longitudToString(){
        String longitud = "";
        if(this.longitud.isEmpty()){
            longitud = "El vector de longitudes esta vacio";
            return longitud;
        }

        for (String name: this.longitud) {
            longitud += name +"\n";
        }
        return longitud;
    }


    // metodos accesores
    public List<String> getCine_id(){return cine_id;}
    public List<String> getNombre(){return nombre;}
    public List<String> getLatitud(){return latitud;}
    public List<String> getLongitud(){return longitud;}

    public void setCine_id(List<String> cine_id){this.cine_id = cine_id;}
    public void setNombre(List<String> nombre){this.nombre = nombre;}
    public void setLatitud(List<String> latitud){this.latitud = latitud;}
    public void setLongitud(List<String> longitud){this.longitud = longitud;}
}

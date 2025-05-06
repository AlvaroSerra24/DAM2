package com.example.buscaminas;

//Clase que almacena las coordenadas de una celda en el tablero.

public class CordenadasCelda {
    public int fila; //La fila de la celda.
    public int columna; //La columna de la celda.

    //Constructor para inicializar las coordenadas de la celda.
    public CordenadasCelda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
}

package com.example.buscaminas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Clase que representa el tablero del juego

public class Logica {
    private boolean[][] minas;       // Matriz que indica dónde están las minas
    private boolean[][] revelar;     // Matriz que indica qué celdas han sido reveladas
    private boolean[][] banderas;    // Matriz que indica dónde se han colocado banderas
    private int filas;               // Número de filas del tablero
    private int columnas;            // Número de columnas del tablero
    private int totalMinas;          // Número total de minas en el tablero

    // Constructor que inicializa el campo de minas con el tamaño y número de minas especificados.

    public Logica(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.totalMinas = calcularTotalMinas(filas);
        generarMinas();
        this.revelar = new boolean[filas][columnas];
        this.banderas = new boolean[filas][columnas];
    }

    // Calcula el número total de minas basándose en el tamaño del tablero.

    private int calcularTotalMinas(int filas) {
        switch (filas) {
            case Constantes.FACIL:
                return Constantes.MINAS_FACIL;
            case Constantes.MEDIO:
                return Constantes.MINAS_MEDIO;
            case Constantes.DIFICIL:
                return Constantes.MINAS_DIFICIL;
            default:
                return Constantes.MINAS_FACIL;
        }
    }

    // Genera minas aleatoriamente en el tablero sin superponerlas.

    private void generarMinas() {
        minas = new boolean[filas][columnas];
        Random random = new Random();
        int colocarMinas = 0;

        while (colocarMinas < totalMinas) {
            int randomRow = random.nextInt(filas);
            int randomCol = random.nextInt(columnas);
            if (!minas[randomRow][randomCol]) {
                minas[randomRow][randomCol] = true;
                colocarMinas++;
            }
        }
    }

    // Comprueba si una celda específica contiene una mina.

    public boolean hayMina(int fila, int columna) {
        return minas[fila][columna];
    }

    // Cuenta el número de minas adyacentes a una celda específica.

    public int contarBombasCerca(int fila, int columna) {
        int contBombas = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int filaVecina = fila + i;
                int columnaVecina = columna + j;

                if (filaVecina >= 0 && filaVecina < filas && columnaVecina >= 0 && columnaVecina < columnas) {
                    if (minas[filaVecina][columnaVecina]) {
                        contBombas++;
                    }
                }
            }
        }
        return contBombas;
    }

    // Comprueba si una celda específica ha sido revelada.

    public boolean esRevelado(int fila, int columna) {
        return revelar[fila][columna];
    }

    // Coloca o quita una bandera en una celda específica.

    public boolean colocarBandera(int fila, int columna) {
        if (revelar[fila][columna]) {
            return false; // No se puede colocar una bandera en una celda ya revelada
        }
        return banderas[fila][columna];
    }

    // Revela una celda específica y, si no hay minas cercanas, revela de forma recursiva las celdas adyacentes.

    public void revelarCelda(int fila, int columna, List<CordenadasCelda> celdasReveladas) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || revelar[fila][columna] || banderas[fila][columna]) {
            return;
        }

        revelar[fila][columna] = true;
        celdasReveladas.add(new CordenadasCelda(fila, columna));
        if (!minas[fila][columna]) {
            if (contarBombasCerca(fila, columna) == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        revelarCelda(fila + i, columna + j, celdasReveladas);
                    }
                }
            }
        }
    }

    // Comprueba si el jugador ha ganado

    public boolean verificarVictoria() {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (minas[fila][columna] && !banderas[fila][columna]) {
                    return false;
                }
                if (banderas[fila][columna] && !minas[fila][columna]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Devuelve si hay una bandera en la celda.

    public boolean hayBandera(int fila, int columna) {
        return banderas[fila][columna];
    }
}





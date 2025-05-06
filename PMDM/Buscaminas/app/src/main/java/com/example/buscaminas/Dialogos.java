package com.example.buscaminas;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//Clase auxiliar para manejar la creación y gestión de cuadros de diálogo.

public class Dialogos {

    //Interfaz para notificar cuando se ha seleccionado una dificultad.

    public interface DificultadSeleccionadaListener {
        void onDificultadSeleccionada(int dificultad);
    }
    //Muestra un diálogo para que el usuario seleccione la dificultad del juego.
    public static void mostrarDialogoCambioDificultad(Context context, DificultadSeleccionadaListener listener) {
        RadioGroup opcionesDificultad = new RadioGroup(context);
        opcionesDificultad.setOrientation(RadioGroup.VERTICAL);

        RadioButton opcionFacil = new RadioButton(context);
        opcionFacil.setText("Fácil");
        opcionFacil.setTag(Constantes.FACIL);

        RadioButton opcionMedio = new RadioButton(context);
        opcionMedio.setText("Medio");
        opcionMedio.setTag(Constantes.MEDIO);

        RadioButton opcionDificil = new RadioButton(context);
        opcionDificil.setText("Difícil");
        opcionDificil.setTag(Constantes.DIFICIL);

        opcionesDificultad.addView(opcionFacil);
        opcionesDificultad.addView(opcionMedio);
        opcionesDificultad.addView(opcionDificil);

        new AlertDialog.Builder(context)
                .setTitle("Selecciona el tamaño del tablero")
                .setView(opcionesDificultad)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    int opcionElegidaId = opcionesDificultad.getCheckedRadioButtonId();
                    if (opcionElegidaId != -1) {
                        RadioButton seleccionarOpcion = opcionesDificultad.findViewById(opcionElegidaId);
                        int dificultad = (int) seleccionarOpcion.getTag();
                        listener.onDificultadSeleccionada(dificultad);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //Muestra un diálogo con las instrucciones del juego.

    public static void mostrarDialogoInstrucciones(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Instrucciones")
                .setMessage("Cuando pulsas en una casilla, sale un número que identifica cuántas minas hay alrededor. "
                        + "Ten cuidado porque si pulsas en una casilla que tenga una mina escondida, perderás. "
                        + "Si crees o tienes la certeza de que hay una mina, haz un click largo sobre la casilla para señalarla. "
                        + "No hagas un click largo en una casilla donde no hay una mina porque perderás. "
                        + "Ganas una vez hayas encontrado todas las minas.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    //Muestra un diálogo de derrota cuando el jugador pulsa una mina.

    public static void mostrarDialogoDerrota(Context context, Runnable reiniciarJuego) {
        new AlertDialog.Builder(context)
                .setTitle("Has perdido")
                .setMessage("Has cometido un error. ¿Quieres volver a jugar?")
                .setPositiveButton("Reiniciar", (dialog, which) -> reiniciarJuego.run())
                .setCancelable(false)
                .show();
    }

    //Muestra un diálogo de victoria cuando el jugador ha revelado todas las celdas sin minas.

    public static void mostrarDialogoVictoria(Context context, Runnable reiniciarJuego) {
        new AlertDialog.Builder(context)
                .setTitle("¡Enhorabuena!")
                .setMessage("¡Has ganado! ¿Quieres reiniciar el juego?")
                .setPositiveButton("Reiniciar", (dialog, which) -> reiniciarJuego.run())
                .setCancelable(false)
                .show();
    }
}

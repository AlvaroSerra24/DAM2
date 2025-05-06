package com.example.buscaminas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Clase que representa una celda individual en el tablero de Buscaminas.

public class Celdas {
    private static final String TAG = "Cell";

    private TextView textView;
    private int fila;
    private int columna;
    private OnCellRevealedListener listener;
    private boolean tieneBandera = false; // Indica si la celda tiene una bandera

    // Interfaz para notificar cuando una celda ha sido revelada.

    public interface OnCellRevealedListener {
        void onCellRevealed();
    }

    // Constructor que inicializa la celda con sus coordenadas y tamaño.

    public Celdas(Context context, int fila, int columna, int ancho, int alto, int margin) {
        this.fila = fila;
        this.columna = columna;
        this.textView = new TextView(context);
        configurarTextView(ancho, alto, margin);
        configurarEventos(context);
    }

    // Configura las propiedades visuales del TextView que representa la celda.

    private void configurarTextView(int ancho, int alto, int margin) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(margin, margin, margin, margin);
        params.width = ancho;
        params.height = alto;
        params.rowSpec = GridLayout.spec(fila);
        params.columnSpec = GridLayout.spec(columna);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(Color.LTGRAY);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
    }

    // Configura los listeners para los eventos de clic y clic largo en la celda.

    private void configurarEventos(Context context) {
        textView.setOnClickListener(v -> manejarClick(context));
        textView.setOnLongClickListener(v -> {
            manejarClickLargo(context);
            return true;
        });
    }

    // Maneja el evento de clic normal en la celda.

    private void manejarClick(Context context) {
        Log.d(TAG, "Click en celda: (" + fila + ", " + columna + ")");
        MainActivity activity = (MainActivity) context;
        Logica logica = activity.getMinefield();

        if (tieneBandera) {
            Toast.makeText(context, "Esta celda tiene una bandera.", Toast.LENGTH_SHORT).show();
            return; // No permitir revelar una celda con bandera
        }

        if (logica.hayMina(fila, columna)) {
            textView.setText("💣");
            textView.setBackgroundColor(Color.RED);
            Dialogos.mostrarDialogoDerrota(activity, activity::iniciarJuego);
        } else {
            List<CordenadasCelda> celdasReveladas = new ArrayList<>();
            logica.revelarCelda(fila, columna, celdasReveladas);
            activity.actualizarCeldas(celdasReveladas);
            if (listener != null) {
                listener.onCellRevealed();
            }
        }
    }

    // Maneja el evento de clic largo en la celda para marcar o desmarcar una mina.

    private void manejarClickLargo(Context context) {
        Log.d(TAG, "Click largo en celda: (" + fila + ", " + columna + ")");
        MainActivity activity = (MainActivity) context;
        Logica logica = activity.getMinefield();

        if (logica.esRevelado(fila, columna)) {
            Toast.makeText(context, "No se puede marcar una celda revelada.", Toast.LENGTH_SHORT).show();
            return; // No permitir colocar una bandera en una celda revelada
        }

        if (tieneBandera) {
            // Quitar la bandera
            textView.setText("");
            textView.setBackgroundColor(Color.LTGRAY);
            tieneBandera = false;
            logica.colocarBandera(fila, columna); // Actualizar la lógica para quitar la bandera
        } else {
            // Colocar la bandera
            textView.setText("🚩");
            textView.setBackgroundColor(Color.WHITE);
            tieneBandera = true;
            logica.colocarBandera(fila, columna); // Actualizar la lógica para colocar la bandera

            if (!logica.hayMina(fila, columna)) {
                // Si la bandera está incorrectamente colocada, mostrar derrota
                textView.setBackgroundColor(Color.RED);
                Dialogos.mostrarDialogoDerrota(activity, activity::iniciarJuego);
            } else {
                Toast.makeText(context, "Bandera correctamente colocada.", Toast.LENGTH_SHORT).show();
            }
        }

        if (listener != null) {
            listener.onCellRevealed();
        }
    }

    // Obtiene el TextView que representa la celda en la UI.

    public TextView getTextView() {
        return textView;
    }

    // Establece un listener para notificar cuando una celda ha sido revelada.

    public void setOnCellRevealedListener(OnCellRevealedListener listener) {
        this.listener = listener;
    }
}

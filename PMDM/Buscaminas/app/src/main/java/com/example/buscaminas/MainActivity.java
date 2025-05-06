package com.example.buscaminas;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

// Actividad principal que gestiona la interfaz de usuario y las interacciones del juego.

public class MainActivity extends AppCompatActivity {
    private Logica logica;
    private GridLayout gridLayout;
    private int filas = Constantes.FACIL;
    private int columnas = Constantes.FACIL;

    // Método llamado al crear la actividad.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = findViewById(R.id.gridLayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        iniciarJuego();
    }

    // Infla el menú de opciones en la barra de acciones.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Maneja las selecciones de ítems en el menú de opciones.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int opcion = item.getItemId();
        if (opcion == R.id.action_change_size) {
            Dialogos.mostrarDialogoCambioDificultad(this, dificultad -> {
                filas = dificultad;
                columnas = dificultad;
                iniciarJuego();
            });
            return true;
        } else if (opcion == R.id.action_restart) {
            iniciarJuego();
            return true;
        } else if (opcion == R.id.action_instructions) {
            Dialogos.mostrarDialogoInstrucciones(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Inicia o reinicia el juego configurando el campo de minas y el tablero en la UI.

    public void iniciarJuego() {
        logica = new Logica(filas, columnas);
        configurarGridLayout();
    }

    // Obtiene la instancia actual de Minefield.

    public Logica getMinefield() {
        return logica;
    }

    // Configura el GridLayout creando y añadiendo las celdas al tablero.

    public void configurarGridLayout() {
        gridLayout.removeAllViews();
        gridLayout.setRowCount(filas);
        gridLayout.setColumnCount(columnas);
        gridLayout.setBackgroundColor(Color.BLUE);

        Log.d("MainActivity", "Configurando GridLayout con filas: " + filas + ", columnas: " + columnas);

        gridLayout.post(() -> {
            int anchoGrid = gridLayout.getWidth();
            int altoGrid = gridLayout.getHeight();
            int margin = 2;
            int anchoMargenTotal = margin * (columnas + 1);
            int altoMargenTotal = margin * (filas + 1);
            int anchoCelda = (anchoGrid - anchoMargenTotal) / columnas;
            int altoCelda = (altoGrid - altoMargenTotal) / filas;

            Log.d("MainActivity", "Ancho de celda: " + anchoCelda + ", Alto de celda: " + altoCelda);

            for (int fila = 0; fila < filas; fila++) {
                for (int columna = 0; columna < columnas; columna++) {
                    Celdas celda = new Celdas(this, fila, columna, anchoCelda, altoCelda, margin);
                    celda.setOnCellRevealedListener(this::verificarVictoria);
                    gridLayout.addView(celda.getTextView());
                    Log.d("MainActivity", "Añadida celda: (" + fila + ", " + columna + ")");
                }
            }
        });
    }

    // Actualiza la apariencia de una lista de celdas reveladas en la UI.

    public void actualizarCeldas(List<CordenadasCelda> celdasReveladas) {
        for (CordenadasCelda coord : celdasReveladas) {
            actualizarCelda(coord.fila, coord.columna);
        }
    }

    // Actualiza la apariencia visual de una celda específica en la UI basada en su estado en Minefield.

    public void actualizarCelda(int fila, int columna) {
        int index = fila * columnas + columna;
        if (index >= 0 && index < gridLayout.getChildCount()) {
            View view = gridLayout.getChildAt(index);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                boolean esRevelado = logica.esRevelado(fila, columna);
                boolean hayMina = logica.hayMina(fila, columna);
                boolean hayBandera = logica.hayBandera(fila, columna);

                if (esRevelado) {
                    textView.setBackgroundColor(Color.WHITE);
                    if (hayMina) {
                        textView.setText("💣");
                    } else {
                        int bombasCerca = logica.contarBombasCerca(fila, columna);
                        textView.setText(bombasCerca > 0 ? String.valueOf(bombasCerca) : "");
                    }
                    textView.setEnabled(false);
                } else if (hayBandera) {
                    textView.setText("🚩");
                    textView.setBackgroundColor(Color.WHITE);
                } else {
                    textView.setText("");
                    textView.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    // Verifica si el jugador ha ganado y si es así se muestra el mensaje de victoria.

    private void verificarVictoria() {
        if (logica.verificarVictoria()) {
            Dialogos.mostrarDialogoVictoria(this, this::iniciarJuego);
        }
    }
}

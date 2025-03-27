package com.example.practicatabata;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageButton start;
    private EditText Series, Trabajo, Descanso;
    private TextView cronometro, estado, left;
    private ConstraintLayout main;
    private CountDownTimer countDownTimer;
    private MediaPlayer beep, gong;
    private int timeTrabajo, LeftSeries, timeDescanso;
    private boolean isRunning = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarBotonInicio();
    }

    private void iniciarComponentes() {
        start=findViewById(R.id.ButtonStart);
        main=findViewById(R.id.main);
        Series=findViewById(R.id.editTextSeries);
        Trabajo=findViewById(R.id.editTextTrabajo);
        Descanso=findViewById(R.id.editTextDescanso);
        cronometro=findViewById(R.id.textNumber);
        estado=findViewById(R.id.textWorkRest);
        left=findViewById(R.id.textSeriesLeft);
    }

    private void configurarBotonInicio(){
        start.setOnClickListener(view -> {
            if(!isRunning){
                IniciarTabata();
            }
        });
    }
    private void IniciarTabata(){
        try{
            timeTrabajo=Integer.parseInt(Trabajo.getText().toString());
            LeftSeries=Integer.parseInt(Series.getText().toString());
            timeDescanso=Integer.parseInt(Descanso.getText().toString());

            if(LeftSeries <= 0 || timeTrabajo <= 0 || timeDescanso <= 0) {
                throw new NumberFormatException("Valores deben ser mayores a 0");
            }else{
                isRunning=true;
                CuentaTrabajo(timeTrabajo, LeftSeries);
            }
        }catch (NumberFormatException e){
            mostrarError("Por favor ingrese valores válidos en los campos");
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void CuentaTrabajo(int time, int LeftSeries) {
        reproducirSonido(R.raw.beep);
        actualizarUI("WORK",Color.GREEN, LeftSeries);
        countDownTimer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long l) {
                long tiempo= l/1000;
                cronometro.setText(String.valueOf(tiempo));
            }

            @Override
            public void onFinish() {
                CuentaDescanso(timeDescanso,LeftSeries-1);
            }
        }.start();
    }
    private void CuentaDescanso(int timeDescanso, int Left) {
        reproducirSonido(R.raw.beep);
        actualizarUI("REST",Color.RED,Left);
        timeTrabajo = Integer.parseInt(Trabajo.getText().toString());

        countDownTimer = new CountDownTimer(timeDescanso*1000, 1000) {
            @Override
            public void onTick(long l) {
                long tiempo= l/1000;
                cronometro.setText(String.valueOf(tiempo));

            }

            @Override
            public void onFinish() {
                if(Left!=0){
                    CuentaTrabajo(timeTrabajo, Left);
                }else{
                    reproducirSonido(R.raw.gong);
                    actualizarUI("FINISH",Color.GRAY,Left);
                    isRunning = false;
                }

            }
        }.start();
    }
    private void reproducirSonido(int sonido) {
        if (beep != null) {
            beep.release();
        }
        beep = MediaPlayer.create(this, sonido);
        beep.start();
    }
    private void actualizarUI(String estadoTexto, int color, int seriesRestantes) {
        estado.setText(estadoTexto);
        main.setBackgroundColor(color);
        left.setText("Series Left: " + seriesRestantes);
    }

}
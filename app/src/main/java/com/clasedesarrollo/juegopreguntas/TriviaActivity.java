package com.clasedesarrollo.juegopreguntas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import database.AppDatabase;
import model.Pregunta;
import model.Usuario;

public class TriviaActivity extends AppCompatActivity {
    private TextView tvPregunta, tvPuntaje;
    private RadioGroup radioGroup;
    private Button button;
    private int currentQuestionIndex = 0;
    private int puntaje = 0;

    // Lista de preguntas y respuestas (ejemplo)
    private List<Pregunta> listaPreguntas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        // Inicializar vistas
        tvPregunta = findViewById(R.id.tvQuestion);
        tvPuntaje = findViewById(R.id.tvScore);
        radioGroup = findViewById(R.id.rgAnswers);
        button = findViewById(R.id.btnAnswer);


        // Cargar preguntas en el array (puedes reemplazar esto con tus preguntas)
        cargarPreguntas();

        // Mostrar la primera pregunta
        mostrarPregunta();

        // Listener del botón de responder
        button.setOnClickListener(v -> {
            // Comprobar qué opción está seleccionada
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                // No hay ninguna opción seleccionada
                FancyToast.makeText(this, "Debes seleccionar una respuesta",FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                // Validar respuesta
                RadioButton radioButtonSeleccionado = findViewById(selectedId);
                validarRespuesta(radioButtonSeleccionado.getText().toString());

                // Cargar siguiente pregunta o finalizar
                currentQuestionIndex++;
                if (currentQuestionIndex < listaPreguntas.size()) {
                    mostrarPregunta();
                } else {
                    // No hay más preguntas, pasar a la cuarta Activity
                    finalizarTrivia();
                }
            }
        });
    }

    // Método para cargar preguntas y respuestas
    private void cargarPreguntas(){
        int numNivel = getIntent().getIntExtra("nivel", 1);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        listaPreguntas = appDatabase.daoPregunta().buscarPreguntasPorNivel(numNivel);
        AppDatabase.destroyInstance();
    }

    // Método para mostrar la pregunta actual
    private void mostrarPregunta() {
        // Limpiar selección anterior
        radioGroup.clearCheck();

        // Obtener la pregunta actual
        Pregunta preguntaActual = listaPreguntas.get(currentQuestionIndex);
        if (preguntaActual != null) {
            // Actualizar el texto de la pregunta y las respuestas
            tvPregunta.setText(preguntaActual.getPregunta());
            ((RadioButton) radioGroup.getChildAt(0)).setText(preguntaActual.getOpcion1());
            ((RadioButton) radioGroup.getChildAt(1)).setText(preguntaActual.getOpcion2());
            ((RadioButton) radioGroup.getChildAt(2)).setText(preguntaActual.getOpcion3());
            ((RadioButton) radioGroup.getChildAt(3)).setText(preguntaActual.getOpcion4());

            // Actualizar el puntaje
            tvPuntaje.setText("Puntaje: " + puntaje);
            // ... rest of your code to set text
        } else {

            // Handle the case where there's no question at this index
            // e.g., Log an error, show a default message, etc.
        }


    }

    // Método para validar la respuesta seleccionada
    private void validarRespuesta(String respuestaSeleccionada) {
        Pregunta preguntaActual = listaPreguntas.get(currentQuestionIndex);
        if (preguntaActual.getRespuesta().equals(respuestaSeleccionada)) {
            puntaje += preguntaActual.getPuntaje();  // Asignar puntos por respuesta correcta
            FancyToast.makeText(this,"Respuesta Correcta !", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
        } else {
            FancyToast.makeText(this,"Respuesta Incorrecta !",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }
    }

    // Método para finalizar la trivia y pasar a la cuarta Activity
    private void finalizarTrivia() {
        // Guardar el puntaje en la base de datos
        String dniCurrentPlayer = getIntent().getStringExtra("dni");
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        Usuario player = appDatabase.daoUsuario().buscarUsuarioPorDni(dniCurrentPlayer);
        player.setPuntaje(puntaje);
        appDatabase.daoUsuario().actualizarUsuario(player);
        AppDatabase.destroyInstance();

        // Pasar a la cuarta Activity y pasar el puntaje como extra
        Intent ranking = new Intent(this, RankingActivity.class);
        ranking.putExtra("PUNTAJE_FINAL", puntaje);
        ranking.putExtra("dni", dniCurrentPlayer);
        startActivity(ranking);
        finish();  // Finalizar esta actividad para que no se pueda volver atrás
    }
}
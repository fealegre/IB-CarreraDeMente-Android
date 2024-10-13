package com.clasedesarrollo.juegopreguntas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import database.AppDatabase;
import model.Pregunta;
import model.Usuario;

public class MainActivity extends AppCompatActivity {

    // METODOS PARA LEER EL JSON,
    // RECIBE EL CONTEXTO DEL ACTIVITY Y EL NOMBRE DEL ARCHIVO JSON
    // EL ARCHIVO JSON SE ENCUENTRA EN LA CARPETA ASSETS DEL PROYECTO
    // DEVUELVE UN STRING CON EL CONTENIDO DEL JSON
    // SE UTILIZA LA CLASES Y LIBRERIAS DE LECTURAS DE ARCHIVOS

    public String readJsonFromAssets(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    // METODO PARA PARSEAR EL JSON, CONVERTIRLO AL MODELO DE LA CLASE "PREGUNTA"
    // RECIBE EL JSON PARA PARSEAR
    // DEVUELVE UNA LISTA DE PREGUNTAS
    // SE UTILIZA LA LIBRERIA GSON PARA PARSEAR EL JSON

    public List<Pregunta> parseJsonToModel(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, new TypeToken<List<Pregunta>>() {}.getType());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // LEER EL JSON
        // SE UTILIZA EL METODO readJsonFromAssets PARA LEER EL JSON
        // SE UTILIZA EL METODO parseJsonToModel PARA PARSEAR EL JSON


        String jsonString;
        try {
            jsonString = readJsonFromAssets(this, "preguntas.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // INSERCION DE LAS PREGUNTAS EN LA BASE DE DATOS
        // SE UTILIZA LA CLASE AppDatabase PARA ACCEDER A LA BASE DE DATOS
        // SE UTILIZA LA CLASE PreguntaDAO PARA ACCEDER A LA TABLA DE PREGUNTAS
        // SE UTILIZA EL METODO insertarPregunta PARA INSERTAR LAS PREGUNTAS EN LA BASE DE DATOS
        // SE UTILIZA EL METODO getInstance PARA ACCEDER A LA BASE DE DATOS
        // SE UTILIZA EL METODO readJsonFromAssets PARA LEER EL JSON

        List<Pregunta> preguntas = parseJsonToModel(jsonString);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());

        // LIMPIAR LA TABLA PREGUNTAS
        appDatabase.daoPregunta().eliminarPreguntas(appDatabase.daoPregunta().listarPreguntas());
        appDatabase.daoPregunta().eliminarPrimaryKeyIndex();

        for (Pregunta pregunta : preguntas) {
            appDatabase.daoPregunta().insertarPregunta(pregunta);
        }

        TextView tvName = findViewById(R.id.tvName);
        TextView tvDni = findViewById(R.id.tvDni);
        Button btnStart = findViewById(R.id.btnAnswer);
        btnStart.setOnClickListener(v -> {
            if (tvName.getText().toString().isEmpty()) {
                tvName.setError("Campo requerido");
                return;
            }
            if (tvDni.getText().toString().isEmpty()){
                tvDni.setError("Campo requerido");
                return;
            }
            FancyToast.makeText(this, "Bienvenido " + tvName.getText().toString(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            btnStart.setEnabled(false);

            appDatabase.daoUsuario().insertarUsuario(new Usuario(tvName.getText().toString(), tvDni.getText().toString()));
            String dniCurrentPlayer = tvDni.getText().toString();
            tvName.setText("");
            tvDni.setText("");
            Intent start = new Intent(this, LectorQR.class);
            start.putExtra("dni", dniCurrentPlayer);
            startActivity(start);
            AppDatabase.destroyInstance();
            finish();
        });
    }
}
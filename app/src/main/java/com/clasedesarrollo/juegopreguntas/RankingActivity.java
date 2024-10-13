package com.clasedesarrollo.juegopreguntas;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import database.AppDatabase;

public class RankingActivity extends AppCompatActivity {

    TextView tvRanking;
    TextView tvPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);
        tvRanking = findViewById(R.id.tvRanking);
        tvPlayer = findViewById(R.id.tvPlayer);
        int puntajeFinal = getIntent().getIntExtra("PUNTAJE_FINAL", 0);
        String dniCurrentPlayer = getIntent().getStringExtra("dni");
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        String nombre = appDatabase.daoUsuario().buscarUsuarioPorDni(dniCurrentPlayer).getNombre();
        AppDatabase.destroyInstance();
        tvPlayer.setText(nombre);
        tvRanking.setText("Puntaje Final\n" + puntajeFinal);
    }
}

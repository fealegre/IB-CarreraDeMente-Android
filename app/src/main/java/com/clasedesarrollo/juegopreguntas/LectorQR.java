package com.clasedesarrollo.juegopreguntas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.shashank.sony.fancytoastlib.FancyToast;


public class LectorQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lector_qr);

        // Configurar el botón para iniciar el escaneo
        Button btnScanQR = findViewById(R.id.btn_scan_qr);
        btnScanQR.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Escaneando código QR");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivity.class);
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);  // Solo QR codes
            barcodeLauncher.launch(options);
        });
    }

    // Manejar el resultado del escaneo
    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Toast.makeText(LectorQR.this, "Resultado: " + result.getContents(), Toast.LENGTH_LONG).show();
                    finalizarEscaneo(result);
                } else {
                    Toast.makeText(LectorQR.this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Finalizar el escaneo y pasar a la siguiente actividad
    private void finalizarEscaneo(ScanIntentResult result) {
        String qrResult = result.getContents();
        if (qrResult != null && !qrResult.isEmpty()) {
            try {
                int nivel = Integer.parseInt(qrResult);
                String dniCurrentPlayer = getIntent().getStringExtra("dni");
                Intent trivia = new Intent(this, TriviaActivity.class);
                trivia.putExtra("nivel", nivel);
                trivia.putExtra("dni", dniCurrentPlayer);
                startActivity(trivia);
                finish();
            } catch (NumberFormatException e) {
                FancyToast.makeText(this, "El código QR no es válido", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        } else {
            FancyToast.makeText(this, "No se pudo escanear el código QR", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }
}
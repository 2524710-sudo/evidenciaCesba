package com.example.evidenciacesba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private ImageView imgFoto;
    private EditText edtTitulo;
    private EditText edtDescripcion;
    private Button btnTomarFoto;
    private Button btnGuardar;
    private Button btnLimpiar;
    private Button btnGaleria;
    private TextView txtEstado;

    private Bitmap fotografiaBitmap = null;

    // CÁMARA
    private final ActivityResultLauncher<Intent> launcherCamara =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    resultado -> {

                        if (resultado.getResultCode() == RESULT_OK) {

                            Intent datos = resultado.getData();

                            if (datos != null && datos.getExtras() != null) {

                                fotografiaBitmap =
                                        (Bitmap) datos.getExtras().get("data");

                                imgFoto.setImageBitmap(fotografiaBitmap);

                                txtEstado.setText(
                                        "Fotografía tomada correctamente"
                                );

                                btnTomarFoto.setText(
                                        "✓ Fotografía tomada"
                                );

                                Toast.makeText(
                                        MainActivity.this,
                                        "Fotografía recibida",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {

                            txtEstado.setText(
                                    "La fotografía fue cancelada"
                            );

                            btnTomarFoto.setText(
                                    "Tomar fotografía"
                            );

                            Toast.makeText(
                                    MainActivity.this,
                                    "Operación cancelada",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // CONECTAR JAVA CON XML
        imgFoto = findViewById(R.id.imgFoto);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);

        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnGaleria = findViewById(R.id.btnGaleria);

        txtEstado = findViewById(R.id.txtEstado);

        // BOTÓN TOMAR FOTOGRAFÍA
        btnTomarFoto.setOnClickListener(v -> abrirCamara());

        // BOTÓN GUARDAR
        btnGuardar.setOnClickListener(v -> guardarEvidencia());

        // BOTÓN LIMPIAR
        btnLimpiar.setOnClickListener(v -> limpiarEvidencia());

        // BOTÓN GALERÍA
        btnGaleria.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    GaleriaActivity.class
            );

            startActivity(intent);
        });
    }

    // ABRIR CÁMARA
    private void abrirCamara() {

        Intent intentCamara =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intentCamara.resolveActivity(getPackageManager()) != null) {

            launcherCamara.launch(intentCamara);

        } else {

            Toast.makeText(
                    this,
                    "No se encontró una aplicación de cámara",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // LIMPIAR FORMULARIO
    private void limpiarEvidencia() {

        edtTitulo.setText("");
        edtDescripcion.setText("");

        fotografiaBitmap = null;

        imgFoto.setImageResource(
                android.R.drawable.ic_menu_camera
        );

        txtEstado.setText("Sin fotografía");

        btnTomarFoto.setText("Tomar fotografía");

        Toast.makeText(
                this,
                "Evidencia limpiada",
                Toast.LENGTH_SHORT
        ).show();
    }

    // GUARDAR EVIDENCIA
    private void    guardarEvidencia() {

        String titulo =
                edtTitulo.getText().toString().trim();

        String descripcion =
                edtDescripcion.getText().toString().trim();

        // VALIDAR TÍTULO
        if (titulo.isEmpty()) {

            edtTitulo.setError("Escribe un título");
            edtTitulo.requestFocus();
            return;
        }

        // VALIDAR DESCRIPCIÓN
        if (descripcion.isEmpty()) {

            edtDescripcion.setError(
                    "Escribe una descripción"
            );

            edtDescripcion.requestFocus();
            return;
        }

        // VALIDAR FOTOGRAFÍA
        if (fotografiaBitmap == null) {

            Toast.makeText(
                    this,
                    "Por favor toma una fotografía antes de guardar",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // CREAR UN NOMBRE ÚNICO
        String nombreArchivo =
                "evidencia_" + System.currentTimeMillis();

        try {

            // GUARDAR LA FOTOGRAFÍA
            File archivoFoto =
                    new File(
                            getFilesDir(),
                            nombreArchivo + ".jpg"
                    );

            FileOutputStream salida =
                    new FileOutputStream(archivoFoto);

            fotografiaBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    salida
            );

            salida.close();

            // GUARDAR TÍTULO Y DESCRIPCIÓN
            File archivoTexto =
                    new File(
                            getFilesDir(),
                            nombreArchivo + ".txt"
                    );

            FileOutputStream salidaTexto =
                    new FileOutputStream(archivoTexto);

            OutputStreamWriter escritor =
                    new OutputStreamWriter(salidaTexto);

            PrintWriter writer =
                    new PrintWriter(escritor);

            writer.println(titulo);
            writer.println(descripcion);

            writer.close();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al guardar la evidencia",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // EVIDENCIA GUARDADA
        txtEstado.setText(
                "Evidencia guardada correctamente"
        );

        Toast.makeText(
                this,
                "Evidencia guardada",
                Toast.LENGTH_LONG
        ).show();

        // LIMPIAR EL FORMULARIO
        edtTitulo.setText("");
        edtDescripcion.setText("");

        fotografiaBitmap = null;

        imgFoto.setImageResource(
                android.R.drawable.ic_menu_camera
        );

        btnTomarFoto.setText(
                "Tomar fotografía"
        );
    }
}
package com.example.evidenciacesba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GaleriaActivity extends AppCompatActivity {

    private LinearLayout contenedorGaleria;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_galeria);

        // CONECTAR JAVA CON XML
        contenedorGaleria =
                findViewById(R.id.contenedorGaleria);

        btnRegresar =
                findViewById(R.id.btnRegresar);

        // CARGAR LAS FOTOGRAFÍAS
        cargarFotos();

        // BOTÓN REGRESAR
        btnRegresar.setOnClickListener(v -> {
            finish();
        });
    }

    private void cargarFotos() {

        File carpeta = getFilesDir();

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            return;
        }

        boolean hayFotos = false;

        for (File archivo : archivos) {

            if (archivo.getName().startsWith("evidencia_")
                    && archivo.getName().endsWith(".jpg")) {

                hayFotos = true;

                // OBTENER EL NOMBRE BASE
                String nombreBase =
                        archivo.getName()
                                .replace(".jpg", "");

                // CARGAR FOTO
                Bitmap bitmap =
                        BitmapFactory.decodeFile(
                                archivo.getAbsolutePath()
                        );

                if (bitmap != null) {

                    // CREAR IMAGEN
                    ImageView imagen =
                            new ImageView(this);

                    imagen.setImageBitmap(bitmap);

                    imagen.setScaleType(
                            ImageView.ScaleType.CENTER_CROP
                    );

                    LinearLayout.LayoutParams parametrosImagen =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    400
                            );

                    parametrosImagen.setMargins(
                            0,
                            0,
                            0,
                            10
                    );

                    imagen.setLayoutParams(
                            parametrosImagen
                    );

                    contenedorGaleria.addView(imagen);

                    // OBTENER TÍTULO Y DESCRIPCIÓN
                    String titulo = "";
                    String descripcion = "";

                    File archivoTexto =
                            new File(
                                    getFilesDir(),
                                    nombreBase + ".txt"
                            );

                    if (archivoTexto.exists()) {

                        try {

                            BufferedReader lector =
                                    new BufferedReader(
                                            new FileReader(
                                                    archivoTexto
                                            )
                                    );

                            titulo = lector.readLine();
                            descripcion = lector.readLine();

                            lector.close();

                        } catch (Exception e) {

                            titulo = "Sin título";
                            descripcion = "Sin descripción";
                        }
                    }

                    // TÍTULO
                    TextView textoTitulo =
                            new TextView(this);

                    textoTitulo.setText(
                            "Título: " + titulo
                    );

                    textoTitulo.setTextColor(
                            android.graphics.Color.WHITE
                    );

                    textoTitulo.setTextSize(22);

                    textoTitulo.setGravity(
                            Gravity.CENTER
                    );

                    textoTitulo.setPadding(
                            10,
                            5,
                            10,
                            5
                    );

                    textoTitulo.setTypeface(
                            null,
                            android.graphics.Typeface.BOLD
                    );

                    contenedorGaleria.addView(
                            textoTitulo
                    );

                    // DESCRIPCIÓN
                    TextView textoDescripcion =
                            new TextView(this);

                    textoDescripcion.setText(
                            "Descripción: " + descripcion
                    );

                    textoDescripcion.setTextColor(
                            android.graphics.Color.WHITE
                    );

                    textoDescripcion.setTextSize(17);

                    textoDescripcion.setGravity(
                            Gravity.CENTER
                    );

                    textoDescripcion.setPadding(
                            10,
                            5,
                            10,
                            25
                    );

                    contenedorGaleria.addView(
                            textoDescripcion
                    );
                }
            }
        }
    }
}
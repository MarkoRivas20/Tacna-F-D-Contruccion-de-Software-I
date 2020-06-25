package com.example.tacnafdbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RecuperarContrasena_IngresarCodigo extends AppCompatActivity {

    Button Btnverificar;
    EditText Txtcodigo;
    String Clave_Recover = "";
    String Id_Usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena__ingresar_codigo);
        getSupportActionBar().hide();


        Bundle datos = this.getIntent().getExtras();
        Clave_Recover = datos.getString("claverecover");
        Id_Usuario = datos.getString("idusuario");

        Btnverificar = (Button) findViewById(R.id.btnverificar);
        Txtcodigo = (EditText) findViewById(R.id.txtcodigo);


        Btnverificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtcodigo.length() == 0)
                {

                    Txtcodigo.setError("Campo requerido");

                }
                else
                {

                    if (Txtcodigo.getText().toString().equals(Clave_Recover)){


                        Intent intent = new Intent(getApplicationContext(), RecuperarContrasena_NuevaContrasena.class);
                        intent.putExtra("idusuario", Id_Usuario);
                        startActivity(intent);

                    }
                    else
                    {
                        Txtcodigo.setError("Codigo Invalido");
                    }
                }

            }
        });
    }
}

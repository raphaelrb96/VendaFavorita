package com.rapha.vendafavorita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class Secretaria extends AppCompatActivity {

    int num = 0;
    TextInputEditText login, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretaria);
        login = (TextInputEditText) findViewById(R.id.et_login);
        senha = (TextInputEditText) findViewById(R.id.et_senha);
    }

    public void fechar(View view) {
        onBackPressed();
    }

    public void continuar(View view) {
        if(num == 10 && login.getText().toString().equals("0senha") && senha.getText().toString().equals("0login")) {
            startActivity(new Intent(this, AdmActivity.class));
        }
        num++;
    }
}
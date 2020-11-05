package com.rapha.vendafavorita;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class MensagemActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_msg);
//        setSupportActionBar(toolbar);
    }

}

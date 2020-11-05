package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterCliente;
import com.rapha.vendafavorita.objects.Usuario;
import com.rapha.vendafavorita.objects.UsuarioParcelable;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class ClientesActivity extends AppCompatActivity implements AdapterCliente.ClienteListener {

    private FirebaseFirestore firestore;
    private RecyclerView rv;

    private AdapterCliente adapterCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        rv = (RecyclerView) findViewById(R.id.rv_clientes);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        firestore = FirebaseFirestore.getInstance();


        firestore.collection("Usuario").orderBy("ultimoLogin", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) return;
                ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                    Usuario u = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                    usuarioArrayList.add(u);
                }
                adapterCliente = new AdapterCliente(usuarioArrayList,ClientesActivity.this, ClientesActivity.this);
                rv.setAdapter(adapterCliente);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void detalhesCliente(Usuario usuario) {
        UsuarioParcelable up = new UsuarioParcelable(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm());
        Intent intent = new Intent(ClientesActivity.this, ClienteDetalhesActivity.class);
        intent.putExtra("user", up);
        startActivity(intent);
    }
}

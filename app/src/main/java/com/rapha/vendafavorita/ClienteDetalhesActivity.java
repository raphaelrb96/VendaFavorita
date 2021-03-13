package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.adapter.AdapterCarrinhoAddUserAnalyics;
import com.rapha.vendafavorita.adapter.AdapterEnviarProduto;
import com.rapha.vendafavorita.adapter.AdapterTermos;
import com.rapha.vendafavorita.objects.ProdutoCartUserAnalyics;
import com.rapha.vendafavorita.objects.TermosDePesquisa;
import com.rapha.vendafavorita.objects.UsuarioParcelable;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.user;


public class ClienteDetalhesActivity extends AppCompatActivity {

    private View bt_voltar_cliente, bt_enviar_mensagem_cliente;
    private ImageView img_perfil_cliente;
    private TextView tv_toolbar_nome_cliente, tv_ultimo_login_cliente,
            tv_primeiro_login_cliente, tv_provedor_login_cliente, tv_email_cliente;
    private TextInputEditText et_mensagem_cliente;
    private LinearLayout ll_bt_mensagem_tela_cliente, ll_bt_cupom_tela_cliente,
            ll_bt_produtos_tela_cliente, container_carrinho_analyics_cliente,
            container_termos_pesquisa_cliente, container_enviar_produto_cliente,
            ll_bt_financa_tela_cliente;

    private EditText et_pesquisar_produto_enviar_cliente;
    private ImageButton bt_pesquisar_produto_enviar_cliente;

    private Switch switch_carrinho_analyics_cliente, switch_termos_pesquisa_cliente;

    private RecyclerView rv_produtos_cart_add_analyics, rv_termo_pesquisa_user, rv_enviar_produtos_cliente;

    private FirebaseFirestore firestore;

    private UsuarioParcelable usuarioParcelable;

    private AdapterCarrinhoAddUserAnalyics adapterCarrinhoAnalyics;
    private AdapterTermos adapterTermos;
    private AdapterEnviarProduto adapterEnviarProduto;

    private ArrayList<ProdutoCartUserAnalyics> prods;

    private ProgressBar pb;
    private ArrayList<ProdObj> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalhes);
        bt_voltar_cliente = (View) findViewById(R.id.bt_voltar_cliente);
        bt_enviar_mensagem_cliente = (View) findViewById(R.id.bt_enviar_mensagem_cliente);

        img_perfil_cliente = (ImageView) findViewById(R.id.img_perfil_cliente);

        bt_pesquisar_produto_enviar_cliente = (ImageButton) findViewById(R.id.bt_pesquisar_produto_enviar_cliente);

        pb = (ProgressBar) findViewById(R.id.pb_cliente_detalhe);

        rv_produtos_cart_add_analyics = (RecyclerView) findViewById(R.id.rv_produtos_cart_add_analyics);
        rv_termo_pesquisa_user = (RecyclerView) findViewById(R.id.rv_termo_pesquisa_user);
        rv_enviar_produtos_cliente = (RecyclerView) findViewById(R.id.rv_enviar_produtos_cliente);

        switch_carrinho_analyics_cliente = (Switch) findViewById(R.id.switch_carrinho_analyics_cliente);
        switch_termos_pesquisa_cliente = (Switch) findViewById(R.id.switch_termos_pesquisa_cliente);

        tv_toolbar_nome_cliente = (TextView) findViewById(R.id.tv_toolbar_nome_cliente);
        tv_ultimo_login_cliente = (TextView) findViewById(R.id.tv_ultimo_login_cliente);
        tv_primeiro_login_cliente = (TextView) findViewById(R.id.tv_primeiro_login_cliente);
        tv_provedor_login_cliente = (TextView) findViewById(R.id.tv_provedor_login_cliente);
        tv_email_cliente = (TextView) findViewById(R.id.tv_email_cliente);

        et_mensagem_cliente = (TextInputEditText) findViewById(R.id.et_mensagem_cliente);

        et_pesquisar_produto_enviar_cliente = (EditText) findViewById(R.id.et_pesquisar_produto_enviar_cliente);

        ll_bt_mensagem_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_mensagem_tela_cliente);
        ll_bt_financa_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_financa_tela_cliente);
        ll_bt_cupom_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_cupom_tela_cliente);
        ll_bt_produtos_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_produtos_tela_cliente);
        container_carrinho_analyics_cliente = (LinearLayout) findViewById(R.id.container_carrinho_analyics_cliente);
        container_termos_pesquisa_cliente = (LinearLayout) findViewById(R.id.container_termos_pesquisa_cliente);
        container_enviar_produto_cliente = (LinearLayout) findViewById(R.id.container_enviar_produto_cliente);

        firestore = FirebaseFirestore.getInstance();

        if (getIntent().getParcelableExtra("user") == null) {
            finish();
        }

        rv_produtos_cart_add_analyics.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this, RecyclerView.HORIZONTAL, false));
        rv_termo_pesquisa_user.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this));
        rv_enviar_produtos_cliente.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this, RecyclerView.HORIZONTAL, false));

        usuarioParcelable = getIntent().getParcelableExtra("user");
        Glide.with(this).load(usuarioParcelable.getPathFoto()).into(img_perfil_cliente);
        tv_toolbar_nome_cliente.setText(usuarioParcelable.getNome());
        tv_ultimo_login_cliente.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuarioParcelable.getUltimoLogin()), new Date()));
        tv_primeiro_login_cliente.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuarioParcelable.getPrimeiroLogin()), new Date()));
        tv_provedor_login_cliente.setText(usuarioParcelable.getProvedor());
        tv_email_cliente.setText(usuarioParcelable.getEmail());

        listeners();


    }

    private void listeners() {
        bt_voltar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_bt_financa_tela_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteDetalhesActivity.this, ComissoesActivity.class);

                intent.putExtra("id", usuarioParcelable.getUid());
                intent.putExtra("nome", usuarioParcelable.getNome());
                intent.putExtra("path", usuarioParcelable.getPathFoto());
                intent.putExtra("zap", usuarioParcelable.getCelular());
                intent.putExtra("time", usuarioParcelable.getUltimoLogin());

                startActivity(intent);
            }
        });

        switch_carrinho_analyics_cliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (prods == null || prods.size() < 1) {
                    return;
                }

                if (isChecked) {
                    adapterCarrinhoAnalyics = new AdapterCarrinhoAddUserAnalyics(ClienteDetalhesActivity.this, bubbleSortCartAnalyicsTopAdd(prods));
                    rv_produtos_cart_add_analyics.setAdapter(adapterCarrinhoAnalyics);
                } else {
                    adapterCarrinhoAnalyics = new AdapterCarrinhoAddUserAnalyics(ClienteDetalhesActivity.this, bubbleSortCartAnalyicsRecentes(prods));
                    rv_produtos_cart_add_analyics.setAdapter(adapterCarrinhoAnalyics);
                }
            }
        });

        bt_enviar_mensagem_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_mensagem_cliente.getText().toString().length() < 1) return;

                String str = et_mensagem_cliente.getText().toString();

                CollectionReference collection = firestore.collection("centralMensagens");
                CollectionReference collectionMensagens = firestore.collection("mensagens").document("ativas").collection(usuarioParcelable.getUid());
                WriteBatch batch = firestore.batch();
                MensagemObject mensagemObject = new MensagemObject(System.currentTimeMillis(), user.getUid(), 1, "", null, str);

                CentralMensagens centralMensagens = new CentralMensagens(new Date(), System.currentTimeMillis(), usuarioParcelable.getUid(), usuarioParcelable.getPathFoto(), mensagemObject.getMenssagemText(), 0, 0, usuarioParcelable.getNome());

                batch.set(collection.document(usuarioParcelable.getUid()), centralMensagens);
                batch.set(collectionMensagens.document(), mensagemObject);
                batch.commit();
                et_mensagem_cliente.clearFocus();
                et_mensagem_cliente.setText("");

            }
        });

        ll_bt_produtos_tela_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteDetalhesActivity.this, InventarioActivity.class);
                startActivity(intent);
            }
        });

        ll_bt_mensagem_tela_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteDetalhesActivity.this, MensagemActivity.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<ProdutoCartUserAnalyics> bubbleSortCartAnalyicsRecentes(ArrayList<ProdutoCartUserAnalyics> array) {
        for (int i = 1; i < array.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (array.get(i).getUltimaVezAdicionadoAoCart() > array.get(j).getUltimaVezAdicionadoAoCart()) {
                    ProdutoCartUserAnalyics temp = array.get(i);
                    array.add(i, array.get(j));
                    array.add(j, temp);
                }
            }
        }

        return array;
    }

    private ArrayList<ProdutoCartUserAnalyics> bubbleSortCartAnalyicsTopAdd(ArrayList<ProdutoCartUserAnalyics> array) {
        for (int i = 1; i < array.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (array.get(i).getNumeroAddCart() > array.get(j).getNumeroAddCart()) {
                    ProdutoCartUserAnalyics temp = array.get(i);
                    array.add(i, array.get(j));
                    array.add(j, temp);
                }
            }
        }

        return array;
    }


}

package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.adapter.CoresAdapterRevenda;
import com.rapha.vendafavorita.adapter.FotosDetalheProdutosAdapter;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;

public class ProdutoRevendaActivity extends AppCompatActivity implements CoresAdapterRevenda.CoresListenerRevenda, FotosDetalheProdutosAdapter.FotoDetalheListener {

    private ImageView imageView;
    private TextView tv_detalhe_prod_revenda_nome, tv_detalhe_prod_revenda_descricao, comissao_produto_detalhe_revenda, total_produto_detalhe_revenda;
    private LinearLayout efab_prod_detalhe_revenda;
    private CardView card5;
    private TextView exemplo_comissao_produto_revenda, comissao_produto_revenda_card, titulo_cores_produto_revenda;

    private RecyclerView rv_cores_produto_revenda, rv_fotos_detalhes;
    private int totalComissao, totalProduto;
    private ProdObjParcelable prodObjParcelable;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private String tituloString = "";


    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_produto_revenda);
        imageView = (ImageView) findViewById(R.id.img_capa_prod_revenda_detalhe);
        tv_detalhe_prod_revenda_nome = (TextView) findViewById(R.id.tv_detalhe_prod_revenda_nome);
        rv_cores_produto_revenda = (RecyclerView) findViewById(R.id.rv_cores_produto_revenda);
        rv_fotos_detalhes = (RecyclerView) findViewById(R.id.rv_fotos_detalhes);
        titulo_cores_produto_revenda = (TextView) findViewById(R.id.titulo_cores_produto_revenda);
        comissao_produto_revenda_card = (TextView) findViewById(R.id.comissao_produto_revenda_card);
        exemplo_comissao_produto_revenda = (TextView) findViewById(R.id.exemplo_comissao_produto_revenda);
        tv_detalhe_prod_revenda_descricao = (TextView) findViewById(R.id.tv_detalhe_prod_revenda_descricao);
        comissao_produto_detalhe_revenda = (TextView) findViewById(R.id.comissao_produto_detalhe_revenda);
        total_produto_detalhe_revenda = (TextView) findViewById(R.id.total_produto_detalhe_revenda);
        efab_prod_detalhe_revenda = (LinearLayout) findViewById(R.id.efab_prod_detalhe_revenda);
        card5 = (CardView) findViewById(R.id.card_5);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        prodObjParcelable = getIntent().getParcelableExtra("prod");
        if (prodObjParcelable == null) {
            finish();
            return;
        }

        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);


        if (!ADMINISTRADOR) {
            analitycsFacebook.logProdutoVizualizadoPeloRevendedorEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
            analitycsGoogle.logProdutoVizualizadoPeloRevendadorEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
        }

        Glide.with(this).load(prodObjParcelable.getImgCapa()).into(imageView);

        if (prodObjParcelable.getImagens() == null) {
            rv_fotos_detalhes.setVisibility(View.GONE);
        } else {
            if (prodObjParcelable.getImagens().size() > 1) {
                rv_fotos_detalhes.setVisibility(View.VISIBLE);
                FotosDetalheProdutosAdapter adapterFotos = new FotosDetalheProdutosAdapter(this, prodObjParcelable.getImagens(), this);
                rv_fotos_detalhes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                rv_fotos_detalhes.setAdapter(adapterFotos);
            } else {
                rv_fotos_detalhes.setVisibility(View.GONE);
            }
        }

        if (prodObjParcelable.getCores() == null) {
            titulo_cores_produto_revenda.setVisibility(View.GONE);
            rv_cores_produto_revenda.setVisibility(View.GONE);
        } else {

            if (prodObjParcelable.getProd().getCores().size() > 0) {
                CoresAdapterRevenda coresAdapterRevenda = new CoresAdapterRevenda(this, prodObjParcelable.getProd().getCores(), this, -1);
                rv_cores_produto_revenda.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                rv_cores_produto_revenda.setAdapter(coresAdapterRevenda);
                titulo_cores_produto_revenda.setVisibility(View.VISIBLE);
                rv_cores_produto_revenda.setVisibility(View.VISIBLE);
            } else {
                titulo_cores_produto_revenda.setVisibility(View.GONE);
                rv_cores_produto_revenda.setVisibility(View.GONE);
            }


        }

        tv_detalhe_prod_revenda_nome.setText(prodObjParcelable.getProdName());
        tv_detalhe_prod_revenda_descricao.setText(prodObjParcelable.getDescr());
        totalComissao = prodObjParcelable.getProd().getComissao();
        totalProduto = (int) prodObjParcelable.getProdValor();

        if (totalComissao == 0) {
            totalComissao = 5;
        }

        atualizarPreco();


        String textoExemplo = "Você vai vender o produto por R$" + totalProduto + ",00 e ganhar R$ " + totalComissao + ",00 a cada item vendido";

        exemplo_comissao_produto_revenda.setText(textoExemplo);

        efab_prod_detalhe_revenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProdObj prodObj = prodObjParcelable.getProd();
                final String str = prodObj.getIdProduto();
                ObjProdutoRevenda objProdutoRevenda = new ObjProdutoRevenda(prodObj.getImgCapa(), totalComissao, totalComissao, str, prodObj.getFabricante(), prodObjParcelable.getProdName(), 1, (int)prodObj.getProdValor(), totalProduto, (int)prodObj.getProdValor(), totalProduto);
                DocumentReference reference = firebaseFirestore.collection("listaRevendas").document("usuario").collection(auth.getCurrentUser().getUid()).document(str);
                reference.set(objProdutoRevenda);
                if (!ADMINISTRADOR) {
                    analitycsFacebook.logRevenderProdutoEvent(prodObj.getProdName(), str, 1, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.getProdValor());
                    analitycsGoogle.logARevenderProdutoEvent(prodObj.getProdName(), str, 1, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.getProdValor());
                }
                Intent intent = new Intent(ProdutoRevendaActivity.this, ListaRevendaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void atualizarPreco() {
        total_produto_detalhe_revenda.setText("R$ " + totalProduto + ",00");
        comissao_produto_detalhe_revenda.setText(totalComissao + ",00");
        comissao_produto_revenda_card.setText(totalComissao + ",00");
    }

    private void fotoSelecionada(String path) {
        Glide.with(this).load(path).into(imageView);
    }

    public void fecha(View view) {
        finish();
    }


    @Override
    public void escolherCor(String cor, int pos) {
        tituloString = prodObjParcelable.getProdName() + "( " + cor + " ) ";
        CoresAdapterRevenda coresAdapterRevenda = new CoresAdapterRevenda(this, prodObjParcelable.getProd().getCores(), this, pos);
        rv_cores_produto_revenda.setAdapter(coresAdapterRevenda);
    }

    @Override
    public void selecionado(String path) {
        fotoSelecionada(path);
    }
}

package com.rapha.vendafavorita;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.adapter.FotosDetalheProdutosAdapter;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;
import static com.rapha.vendafavorita.MainActivity.ids;

public class ProdutoDetalheActivity extends AppCompatActivity implements AdapterProdutos.ClickProdutoCliente, FotosDetalheProdutosAdapter.FotoDetalheListener {

    private TextView nomeProd, valor, descricao;
    private ImageView imagem;
    private FirebaseFirestore firebaseFirestore;
    private ProdObjParcelable prodObjParcelable;
    private Toast mToast;
    private LinearLayout efab;
    private ProgressBar pb;
    private RecyclerView rvRelacionados, rv_fotos_detalhe_prod;
    private LinearLayout titulo_produtos_relacionados;
    private NestedScrollView nestedscrollview;

    private ArrayList<ProdObj> produtosRelacionados;

    private View bt_voltar_produto_detalhe;

    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;
    private TextView tv_bt_revender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_produto_detalhe);
        nestedscrollview = (NestedScrollView) findViewById(R.id.nestedscrollview);
        produtosRelacionados = new ArrayList<>();
        nomeProd = (TextView) findViewById(R.id.tv_detalhe_prod_nome);
        titulo_produtos_relacionados = (LinearLayout) findViewById(R.id.titulo_produtos_relacionados);
        descricao = (TextView) findViewById(R.id.tv_detalhe_prod_descricao);
        rvRelacionados = (RecyclerView) findViewById(R.id.rv_produtos_relacionados);
        rv_fotos_detalhe_prod = (RecyclerView) findViewById(R.id.rv_fotos_detalhe_prod);
        valor = (TextView) findViewById(R.id.tv_detalhe_prod_valor);
        imagem = (ImageView) findViewById(R.id.img_pod_detalhe);
        pb = (ProgressBar) findViewById(R.id.pb_prod_detalhe);
        efab = (LinearLayout) findViewById(R.id.efab_prod_detalhe);
        bt_voltar_produto_detalhe = (View) findViewById(R.id.bt_voltar_produto_detalhe);
        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);
        prodObjParcelable = getIntent().getParcelableExtra("prod");
        if (prodObjParcelable == null) {
            finish();
            return;
        }



        ArrayList<ProdObjParcelable> objParcelables = getIntent().getParcelableArrayListExtra("relacionados");

        if (objParcelables != null) {

            for (int i = 0; i < objParcelables.size(); i++) {
                produtosRelacionados.add(objParcelables.get(i).getProd());
            }


        }


        Glide.with(this).load(prodObjParcelable.getImgCapa()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                nestedscrollview.smoothScrollTo(0,0);
                return false;
            }
        }).into(imagem);


        if (prodObjParcelable.getImagens() == null) {
            rv_fotos_detalhe_prod.setVisibility(View.GONE);
        } else {
            if (prodObjParcelable.getImagens().size() > 1) {
                rv_fotos_detalhe_prod.setVisibility(View.VISIBLE);
                FotosDetalheProdutosAdapter adapterFotos = new FotosDetalheProdutosAdapter(this, prodObjParcelable.getImagens(), this);
                rv_fotos_detalhe_prod.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                rv_fotos_detalhe_prod.setAdapter(adapterFotos);
            } else {
                rv_fotos_detalhe_prod.setVisibility(View.GONE);
            }
        }


        valor.setText(String.valueOf((int) prodObjParcelable.getProdValor()) + ",00");
        nomeProd.setText(prodObjParcelable.getProdName());
        descricao.setText(prodObjParcelable.getDescr());
        if (prodObjParcelable.getValorAntigo() > 0) {
            int vAntigoInt = (int)prodObjParcelable.getValorAntigo();
            int vAtualInt = (int)prodObjParcelable.getProdValor();
            int economiaInt = vAntigoInt - vAtualInt;
        } else {
        }

        if (produtosRelacionados.size() > 0) {
            titulo_produtos_relacionados.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rvRelacionados.setLayoutManager(layoutManager);
            AdapterProdutos mAdapter = new AdapterProdutos(ProdutoDetalheActivity.this, ProdutoDetalheActivity.this, produtosRelacionados, false);
            rvRelacionados.setAdapter(mAdapter);

        }

        firebaseFirestore = FirebaseFirestore.getInstance();


        bt_voltar_produto_detalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nestedscrollview.scrollTo(0, 0);

    }

    public void voltarDetalhe(View view) {
        onBackPressed();
    }

    public void abrirCarrinho() {
        Intent intent = new Intent(ProdutoDetalheActivity.this, CartActivity.class);
        startActivity(intent);
    }

    public void comprarProdDetalhe(View view) {

        Intent intent = new Intent(ProdutoDetalheActivity.this, ProdutoRevendaActivity.class);

        intent.putExtra("prod", prodObjParcelable);

        startActivity(intent);

        /*
        if (user.isAnonymous() || user == null) {
            mToast = Toast.makeText(this, "Faça Login para poder efutuar sua compra", Toast.LENGTH_LONG);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast.show();
        } else {
            final ProdObj prodObj = prodObjParcelable.getProd();
            final String str = prodObj.getIdProduto();
            CarComprasActivy carComprasActivy = new CarComprasActivy(str, prodObj.getProdName(), prodObj.getFabricante(), prodObj.getImgCapa(), prodObj.getProdValor(), 1, ((float) 1) * prodObj.getProdValor());
            DocumentReference reference = firebaseFirestore.collection("carComprasActivy").document("usuario").collection(user.getUid()).document(str);
            if (!ids.contains(str)) {

                if (!ADMINISTRADOR) {
                    analitycsFacebook.logAdicionarAoCarrinhoEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.prodValor);
                    analitycsGoogle.logAdicionarAoCarrinhoEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.prodValor);

                    analitycsFacebook.logUsuarioAdicionaProdutoAoCartEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), user.getDisplayName(), user.getUid(), user.getEmail(), pathFotoUser, prodObj.prodValor);
                    analitycsGoogle.logUsuarioAdicionaProdutoAoCartEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), user.getDisplayName(), user.getUid(), user.getEmail(), pathFotoUser, prodObj.prodValor);
                }

                pb.setVisibility(View.VISIBLE);
                efab.setVisibility(View.GONE);
                reference.set(carComprasActivy).addOnSuccessListener(ProdutoDetalheActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ids.add(str);
                        abrirCarrinho();
                        finish();
                    }
                }).addOnFailureListener(ProdutoDetalheActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mToast = Toast.makeText(ProdutoDetalheActivity.this, "Erro ao Adicionar " + prodObj.getProdName() + " ao carrinho ", Toast.LENGTH_LONG);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast.show();
                    }
                });

            } else {
                abrirCarrinho();
                finish();
            }
        }

         */
    }


    @Override
    protected void onStart() {
        super.onStart();
        analitycsGoogle.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
        analitycsFacebook.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
    }

    @Override
    public void openDetalhe(ProdObj obj) {
        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores());
        Intent intent = new Intent(ProdutoDetalheActivity.this, ProdutoDetalheActivity.class);
        intent.putExtra("prod", objParcelable);
        startActivity(intent);
    }

    @Override
    public void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj) {

    }

    @Override
    public void categoria(int categ) {

    }

    @Override
    public void adm() {

    }

    @Override
    public void openChat() {

    }

    @Override
    public void selecionado(String path) {
        Glide.with(this).load(path).into(imagem);
    }
}

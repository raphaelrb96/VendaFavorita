package com.rapha.vendafavorita;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.adapter.AdapterParcelamento;
import com.rapha.vendafavorita.adapter.CoresAdapterRevenda;
import com.rapha.vendafavorita.adapter.FotosDetalheProdutosAdapter;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.VariantePrecificacao;
import com.rapha.vendafavorita.util.Calculos;
import com.rapha.vendafavorita.util.Pagamentos;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;

public class ProdutoRevendaActivity extends AppCompatActivity implements CoresAdapterRevenda.CoresListenerRevenda, FotosDetalheProdutosAdapter.FotoDetalheListener {

    private ImageView imageView;
    private TextView tv_detalhe_prod_revenda_nome, tv_detalhe_prod_revenda_descricao, comissao_produto_detalhe_revenda;
    private MaterialButton efab_prod_detalhe_revenda;
    private TextView titulo_cores_produto_revenda;

    private RecyclerView rv_cores_produto_revenda, rv_fotos_detalhes, rv_parcelamento_detalhes;
    private int totalComissao, totalProduto;
    private ProdObjParcelable prodObjParcelable;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private String tituloString = "";
    private MaterialCardView bt_modelo_precificacao;
    private TickerView text_bt_modelo_precificacao, text_comissao, tv_valor_variante_produto;


    private AnalitycsGoogle analitycsGoogle;
    private LinearLayout container_variantes_produto_revenda;
    private NestedScrollView conteudo_produto_revenda;
    private ProgressBar pb_produto_revenda;
    private TextView valor_dinheiro_produto_detalhe_revenda;
    private TextView valor_cartao_produto_detalhe_revenda;
    private CoresAdapterRevenda coresAdapterRevenda;
    private TextView indisponivel_produto_detalhe_revenda;
    private String idProd;
    private Button bt_link_site, bt_link_app;
    private int modoDePrecificacao = 0;
    private VariantePrecificacao varianteAtual;
    private String fotoPrincipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_produto_revenda);
        imageView = (ImageView) findViewById(R.id.img_capa_prod_revenda_detalhe);
        bt_modelo_precificacao = (MaterialCardView) findViewById(R.id.bt_modelo_precificacao);
        tv_detalhe_prod_revenda_nome = (TextView) findViewById(R.id.tv_detalhe_prod_revenda_nome);

        rv_cores_produto_revenda = (RecyclerView) findViewById(R.id.rv_cores_produto_revenda);
        rv_fotos_detalhes = (RecyclerView) findViewById(R.id.rv_fotos_detalhes);
        rv_parcelamento_detalhes = (RecyclerView) findViewById(R.id.rv_parcelamento_detalhes);
        titulo_cores_produto_revenda = (TextView) findViewById(R.id.titulo_cores_produto_revenda);
        tv_detalhe_prod_revenda_descricao = (TextView) findViewById(R.id.tv_detalhe_prod_revenda_descricao);
        comissao_produto_detalhe_revenda = (TextView) findViewById(R.id.comissao_produto_detalhe_revenda);
        valor_cartao_produto_detalhe_revenda = (TextView) findViewById(R.id.valor_cartao_produto_detalhe_revenda);
        valor_dinheiro_produto_detalhe_revenda = (TextView) findViewById(R.id.valor_dinheiro_produto_detalhe_revenda);
        indisponivel_produto_detalhe_revenda = (TextView) findViewById(R.id.indisponivel_produto_detalhe_revenda);
        bt_link_site = (Button) findViewById(R.id.bt_link_site);
        bt_link_app = (Button) findViewById(R.id.bt_link_app);

        efab_prod_detalhe_revenda = (MaterialButton) findViewById(R.id.efab_prod_detalhe_revenda);
        container_variantes_produto_revenda = (LinearLayout) findViewById(R.id.container_variantes_produto_revenda);
        conteudo_produto_revenda = (NestedScrollView) findViewById(R.id.conteudo_produto_revenda);
        pb_produto_revenda = (ProgressBar) findViewById(R.id.pb_produto_revenda);

        text_comissao = (TickerView) findViewById(R.id.text_comissao);
        text_bt_modelo_precificacao = (TickerView) findViewById(R.id.text_bt_modelo_precificacao);
        tv_valor_variante_produto = (TickerView) findViewById(R.id.tv_valor_variante_produto);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        prodObjParcelable = getIntent().getParcelableExtra("prod");

        if (prodObjParcelable == null) {

            conteudo_produto_revenda.setVisibility(View.GONE);
            pb_produto_revenda.setVisibility(View.VISIBLE);

            idProd = getIntent().getStringExtra("id");

            firebaseFirestore.collection("produtos").document(idProd).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot == null) {
                        Toast.makeText(ProdutoRevendaActivity.this, "Erro inesperado", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        ProdObj obj = documentSnapshot.toObject(ProdObj.class);
                        prodObjParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores(), obj.getProdValorPromocional(), obj.getProdValorAtacarejo(), obj.getProdValorAtacado());
                        fotoPrincipal = prodObjParcelable.getImgCapa();
                        conteudo_produto_revenda.setVisibility(View.VISIBLE);
                        pb_produto_revenda.setVisibility(View.GONE);
                        ArrayList<VariantePrecificacao> modelosDePreco = Calculos.getListaPrecificacao(prodObjParcelable.getComissao(), prodObjParcelable.getProdValor());
                        VariantePrecificacao variantePreco = modelosDePreco.get(modoDePrecificacao);
                        atualizarInterface(variantePreco);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProdutoRevendaActivity.this, "Erro inesperado", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        } else {
            idProd = prodObjParcelable.getIdProduto();
            fotoPrincipal = prodObjParcelable.getImgCapa();
            conteudo_produto_revenda.setVisibility(View.VISIBLE);
            pb_produto_revenda.setVisibility(View.GONE);
            ArrayList<VariantePrecificacao> modelosDePreco = Calculos.getListaPrecificacao(prodObjParcelable.getComissao(), prodObjParcelable.getProdValor());
            VariantePrecificacao variantePreco = modelosDePreco.get(modoDePrecificacao);
            atualizarInterface(variantePreco);
        }


    }


    private void atualizarInterface (VariantePrecificacao variante) {

        varianteAtual = variante;

        analitycsGoogle = new AnalitycsGoogle(this);

        if (!ADMINISTRADOR) {
            analitycsGoogle.logProdutoVizualizadoPeloRevendadorEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
        }


        totalProduto = (int) variante.getValor();
        totalComissao = variante.getComissao();

        tituloString = prodObjParcelable.getProdName();


        Glide.with(this).load(fotoPrincipal).into(imageView);
        valor_dinheiro_produto_detalhe_revenda.setText("R$ " + totalProduto + ",00");
        tv_valor_variante_produto.setText("R$" + totalProduto);
        text_comissao.setText("R$" + totalComissao, true);
        text_bt_modelo_precificacao.setText(variante.getNome(), true);
        tv_detalhe_prod_revenda_nome.setText(tituloString);
        tv_detalhe_prod_revenda_descricao.setText(prodObjParcelable.getDescr());




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
            container_variantes_produto_revenda.setVisibility(View.GONE);
        } else {

            if (prodObjParcelable.getProd().getCores().size() > 0) {
                coresAdapterRevenda = new CoresAdapterRevenda(this, prodObjParcelable.getProd().getCores(), this, -1);
                rv_cores_produto_revenda.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                rv_cores_produto_revenda.setAdapter(coresAdapterRevenda);
                titulo_cores_produto_revenda.setVisibility(View.VISIBLE);
                rv_cores_produto_revenda.setVisibility(View.VISIBLE);
            } else {
                titulo_cores_produto_revenda.setVisibility(View.GONE);
                rv_cores_produto_revenda.setVisibility(View.GONE);
                container_variantes_produto_revenda.setVisibility(View.GONE);
            }


        }





        rv_parcelamento_detalhes.setVisibility(View.VISIBLE);
        AdapterParcelamento adapterParcelamento = new AdapterParcelamento(Pagamentos.simularParcelamento(totalProduto));
        rv_parcelamento_detalhes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_parcelamento_detalhes.setAdapter(adapterParcelamento);






        bt_modelo_precificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetPro bottomSheetPro = BottomSheetPro.newInstance(prodObjParcelable.getComissao(), prodObjParcelable.getProdValor());
                bottomSheetPro.setModo(modoDePrecificacao);
                bottomSheetPro.setListener(new BottomSheetPro.ListenerBottomSheetPro() {
                    @Override
                    public void clickBottomSheetPro(String s, int pos, VariantePrecificacao varianteDePreco) {
                        VariantePrecificacao novaVariante = varianteDePreco;
                        if(varianteDePreco == null) {
                            ArrayList<VariantePrecificacao> modelosDePreco = Calculos.getListaPrecificacao(prodObjParcelable.getComissao(), prodObjParcelable.getProdValor());
                            novaVariante = modelosDePreco.get(modoDePrecificacao);

                        }
                        atualizarInterface(novaVariante);
                        modoDePrecificacao = pos;
                        bottomSheetPro.dismiss();
                        conteudo_produto_revenda.smoothScrollTo(0, 0, 1500);
                    }
                });
                bottomSheetPro.show(getSupportFragmentManager(), "BottomSheet");
            }
        });

        bt_link_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharLinkSite();
            }
        });

        bt_link_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharDynamicLink();
            }
        });

        if (prodObjParcelable.isDisponivel()) {


            efab_prod_detalhe_revenda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProdObj prodObj = prodObjParcelable.getProd();
                    final String str = prodObj.getIdProduto();
                    ObjProdutoRevenda objProdutoRevenda = new ObjProdutoRevenda(prodObj.getImgCapa(), (totalComissao * variante.getQuantidadeMinima()), totalComissao, str, prodObj.getFabricante(), tituloString, variante.getQuantidadeMinima(), (totalProduto * variante.getQuantidadeMinima()), (totalProduto * variante.getQuantidadeMinima()), totalProduto, totalProduto, varianteAtual.getId(), varianteAtual.getNome(), variante.getQuantidadeMinima());
                    DocumentReference reference = firebaseFirestore.collection("listaRevendas").document("usuario").collection(auth.getCurrentUser().getUid()).document(str);
                    reference.set(objProdutoRevenda);
                    if (!ADMINISTRADOR) {
                        analitycsGoogle.logARevenderProdutoEvent(prodObj.getProdName(), str, 1, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.getProdValor());
                    }
                    Intent intent = new Intent(ProdutoRevendaActivity.this, ListaRevendaActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        } else {

            efab_prod_detalhe_revenda.setVisibility(View.GONE);
            container_variantes_produto_revenda.setVisibility(View.GONE);
            indisponivel_produto_detalhe_revenda.setVisibility(View.VISIBLE);

        }
    }

    private void atualizarPreco() {
        comissao_produto_detalhe_revenda.setText("R$ " + totalComissao + ",00");
    }

    private void compartilharDynamicLink() {
        if(idProd == null) return;
        String stringUrl = "https://vendafavorita.web.app/produto/?id=" + idProd;


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(stringUrl))
                .setDomainUriPrefix("https://vendafavorita.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicLink.getUri().toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "Link do Produto");
                            i.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            startActivity(Intent.createChooser(i, "Compartilhar Produto"));
                        } else {
                            // Error
                            // ...
                        }
                    }
                });

        Uri dynamicLinkUri = dynamicLink.getUri();


    }

    private void compartilharLinkSite() {
        if(idProd == null) return;
        String stringUrl = "https://vendafavorita.web.app/produto/?id=" + idProd;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Link do Produto");
        i.putExtra(Intent.EXTRA_TEXT, stringUrl);
        startActivity(Intent.createChooser(i, "Compartilhar Produto"));
    }

    private void fotoSelecionada(String path) {
        Glide.with(this).load(path).into(imageView);
        conteudo_produto_revenda.smoothScrollTo(0, 0, 1000);
        fotoPrincipal = path;

    }

    public void fecha(View view) {
        finish();
    }


    @Override
    public void escolherCor(String cor, int pos) {
        tituloString = prodObjParcelable.getProdName() + " ( " + cor + " ) ";
        coresAdapterRevenda.trocarCor(pos);
        //coresAdapterRevenda.notify();

    }

    @Override
    public void selecionado(String path) {
        fotoSelecionada(path);
    }


    public void copiarValores(View view) {
        if(prodObjParcelable == null) return;


        String modelosDePreco = Calculos.getCopyValores(prodObjParcelable.getComissao(), prodObjParcelable.getProdValor(), prodObjParcelable.getProdName());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Valores", modelosDePreco));

        AlertDialog.Builder dialogOffline = new AlertDialog.Builder(this);
        dialogOffline.setTitle("Valores Copiados");
        dialogOffline.setMessage("Compartilhe ou cole usando area de transferencia");

        AlertDialog alertDialogOff = dialogOffline.create();
        alertDialogOff.show();
    }


    public void copiarDetalhes(View view) {
        if(prodObjParcelable == null) return;

        String texto = "*" + prodObjParcelable.getProdName() + "*" + " \n\n" + prodObjParcelable.getDescr();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Detalhes", texto));

        AlertDialog.Builder dialogOffline = new AlertDialog.Builder(this);
        dialogOffline.setTitle("Detalhes Copiados");
        dialogOffline.setMessage("Compartilhe ou cole usando area de transferencia");

        AlertDialog alertDialogOff = dialogOffline.create();
        alertDialogOff.show();
    }
}

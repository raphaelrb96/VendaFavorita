package com.rapha.vendafavorita;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rapha.vendafavorita.adapter.AdapterCadastroFotos;
import com.rapha.vendafavorita.adapter.AdapterCategoriasCadastro;
import com.rapha.vendafavorita.adapter.AdapterCoresCadastro;
import com.rapha.vendafavorita.objects.ImagemPreUpload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CadastroDeProdutoActivity extends AppCompatActivity implements AdapterCategoriasCadastro.MudancaDeCategoria, AdapterCadastroFotos.CadastroFotosListener, AdapterCoresCadastro.CoresListener {

    private static final int REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO = 821;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Uri uriFotoProduto = null;

    private Button bt_add_fornecedor;

    private Switch switch_disponibilidade_cadastro_de_produto;

    private CheckBox cb_cadastro_de_produto_promocional;

    private TextInputLayout inputTags;
    private TextInputEditText etTags, et_cadastro_de_produto_nome, et_cadastro_de_produto_labo,
            et_cadastro_de_produto_descricao, et_cadastro_de_produto_preco_consumidor,
            et_cadastro_de_produto_fornecedores, et_cadastro_de_produto_fornecedores_precos,
            et_cadastro_de_produto_preco_antigo, et_cadastro_de_produto_comissao,
            et_cadastro_de_produto_cores;

    private TextView tv_switch_disponibilidade_cadastro_de_produto, fornecedores_e_precos;

    private FrameLayout loadding;

    private CardView btAddTag, bt_add_cores_cadastro_de_produto;
    private LinearLayout containerTags, container_fornecedor;
    private View btClearTags;
    private TextView tvListTags;

    private View volar_cadastro_de_produtos;

    private Toast mToast = null;

    private ExtendedFloatingActionButton bt_cadastro_de_produto_salvar, efabExcluir;

    private ArrayList<String> tagList = new ArrayList<>();
    public static ArrayList<String> categoriaList = new ArrayList<>();


    public static ArrayList<String> pathsCadProd = new ArrayList<>();
    public static ArrayList<ImagemPreUpload> preImagens = new ArrayList<>();

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private Bundle extras;
    private ProdObj prod = null;

    private RecyclerView recyclerCategorias, rv_fotos_cadastro, rv_cores_cadastro;

    private Map<String, Boolean> CATEGORIA;
    private Map<String, Double> FORNECEDORES;
    private String fornecedoresString;
    private TextInputEditText et_cadastro_de_produto_quantidade;
    private AdapterCadastroFotos adapterCadastroFotos;

    private int QUANTIDADE = 0;
    private int COMISSAO = 0;

    private ArrayList<String> CORES;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uriFotoProduto = data.getData();
                exibirFotoEscolhida(uriFotoProduto);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_po);
        volar_cadastro_de_produtos = (View) findViewById(R.id.volar_cadastro_de_produtos);
        volar_cadastro_de_produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViews();
        if (prod == null) {
            getProdIntent();
        }
        clicksListeners();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProdIntent() {
        extras = getIntent().getExtras();
        ProdObjParcelable prodObjParcelable = getIntent().getParcelableExtra("prod");
        FORNECEDORES = new HashMap<>();
        CATEGORIA = new HashMap<>();
        CORES = new ArrayList<>();


        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);

        rv_fotos_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapterCadastroFotos = new AdapterCadastroFotos(this, preImagens, this);
        rv_fotos_cadastro.setAdapter(adapterCadastroFotos);

        if (extras == null || prodObjParcelable == null) {
            container_fornecedor.setVisibility(View.GONE);

            tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
            return;
        }

        prod = prodObjParcelable.getProd();

        CATEGORIA = prodObjParcelable.getCategorias();

        tagList = extras.getStringArrayList("t");
        categoriaList = extras.getStringArrayList("c");

        CORES = extras.getStringArrayList("cor");

        QUANTIDADE = extras.getInt("quant", 0);
        COMISSAO = extras.getInt("comis", 0);



        pathsCadProd = prod.getImagens();

        if (prod.getFornecedores() != null) {
            FORNECEDORES = prod.getFornecedores();
            container_fornecedor.setVisibility(View.VISIBLE);
            atulizarFornecedor(FORNECEDORES);
        }

        preencherInterface(prod);
    }

    private void preencherInterface(ProdObj prod) {

        preImagens = new ArrayList<>();

        if (prod.getImgCapa() != null || prod.getImgCapa().length() > 0) {

            ImagemPreUpload imgprincipal = new ImagemPreUpload(prod.getImgCapa(), null, true, true);
            preImagens.add(imgprincipal);

            if (prod.getImagens() != null) {

                for (int x = 0; x < prod.getImagens().size(); x++) {

                    Log.d("TesteImagemCadastro", "path: " + prod.getImagens().get(x));

                    if (!prod.getImagens().get(x).equals(prod.getImgCapa())) {
                        ImagemPreUpload imgExtras = new ImagemPreUpload(prod.getImagens().get(x), null, true, false);
                        preImagens.add(imgExtras);
                    }
                }

            }

        }

        rv_fotos_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapterCadastroFotos = new AdapterCadastroFotos(this, preImagens, this);
        rv_fotos_cadastro.setAdapter(adapterCadastroFotos);

        et_cadastro_de_produto_nome.setText(prod.getProdName());
        et_cadastro_de_produto_descricao.setText(prod.getDescr());
        et_cadastro_de_produto_labo.setText(prod.getFabricante());

        String sprecoantigo = String.valueOf((int) prod.getValorAntigo());
        String svenda = String.valueOf((int) prod.getProdValor());
        String sQuant = String.valueOf(QUANTIDADE);
        String sComissao = String.valueOf(COMISSAO);
        switch_disponibilidade_cadastro_de_produto.setChecked(prod.isDisponivel());
        if (prod.isDisponivel()) {
            tv_switch_disponibilidade_cadastro_de_produto.setText("Disponivel");
        } else {
            tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
        }
        et_cadastro_de_produto_quantidade.setText(sQuant);
        et_cadastro_de_produto_comissao.setText(sComissao);
        et_cadastro_de_produto_preco_antigo.setText(sprecoantigo);
        et_cadastro_de_produto_preco_consumidor.setText(svenda);
        atualizarListaTags();
        if (prod.isPromocional()) {
            cb_cadastro_de_produto_promocional.setChecked(true);
        }

        ArrayList<String> categs = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.categorias)));
        ArrayList<String> selecionadas = new ArrayList<>();
        for (int i = 0; i < categs.size(); i++) {
            if (prod.getCategorias() != null && prod != null) {
                if (prod.getCategorias().containsKey(String.valueOf(i))) {
                    selecionadas.add(String.valueOf(i));
                }
            }
        }

        AdapterCoresCadastro adapterCoresCadastro = new AdapterCoresCadastro(CORES, this, this);
        rv_cores_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_cores_cadastro.setAdapter(adapterCoresCadastro);


        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);
    }


    private void findViews() {
        inputTags = (TextInputLayout) findViewById(R.id.text_input_cadastro_de_produto_tags);
        etTags = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_tags);
        btAddTag = (CardView) findViewById(R.id.bt_add_tag_cadastro_de_produto);
        bt_add_cores_cadastro_de_produto = (CardView) findViewById(R.id.bt_add_cores_cadastro_de_prod);
        btClearTags = (View) findViewById(R.id.bt_limpar_tags_cadastro_de_produto);
        containerTags = (LinearLayout) findViewById(R.id.container_tags_cadastro_de_produto);
        container_fornecedor = (LinearLayout) findViewById(R.id.container_fornecedor);
        bt_add_fornecedor = (Button) findViewById(R.id.bt_add_fornecedor);
        tvListTags = (TextView) findViewById(R.id.textview_tags_cadastro_de_produto);
        fornecedores_e_precos = (TextView) findViewById(R.id.fornecedores_e_precos);
        tv_switch_disponibilidade_cadastro_de_produto = (TextView) findViewById(R.id.tv_switch_disponibilidade_cadastro_de_produto);
        efabExcluir = (ExtendedFloatingActionButton) findViewById(R.id.bt_cadastro_de_produto_excluir);
        et_cadastro_de_produto_nome = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_nome);
        et_cadastro_de_produto_comissao = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_comissao);
        et_cadastro_de_produto_fornecedores = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_fornecedores);
        et_cadastro_de_produto_labo = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_labo);
        et_cadastro_de_produto_descricao = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_descricao);
        et_cadastro_de_produto_fornecedores_precos = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_fornecedores_precos);
        et_cadastro_de_produto_preco_consumidor = (TextInputEditText)findViewById(R.id.et_cadastro_de_produto_preco_consumidor);
        et_cadastro_de_produto_cores = (TextInputEditText)findViewById(R.id.et_cadastro_de_produto_cores);
        et_cadastro_de_produto_preco_antigo = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_preco_antigo);
        et_cadastro_de_produto_quantidade = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_quantidade);
        cb_cadastro_de_produto_promocional = (CheckBox) findViewById(R.id.cb_cadastro_de_produto_promocional);
        switch_disponibilidade_cadastro_de_produto = (Switch) findViewById(R.id.switch_disponibilidade_cadastro_de_produto);
        bt_cadastro_de_produto_salvar = (ExtendedFloatingActionButton) findViewById(R.id.bt_cadastro_de_produto_salvar);
        loadding = (FrameLayout) findViewById(R.id.tela_loading_cadastro_produto);
        recyclerCategorias = (RecyclerView) findViewById(R.id.rv_categorias_cadastro);
        rv_fotos_cadastro = (RecyclerView) findViewById(R.id.rv_fotos_cadastro);
        rv_cores_cadastro = (RecyclerView) findViewById(R.id.rv_cores_cadastro);
    }

    private void clicksListeners() {
        btClearTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparTags();
            }
        });

        btAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = pegarTexto(etTags);
                if (tag != null) {
                    tagList.add(tag);
                    etTags.setText("");
                    atualizarListaTags();
                }
            }
        });


        bt_add_cores_cadastro_de_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tx = pegarTexto(et_cadastro_de_produto_cores);
                if (tx.length() > 0) {
                    if (CORES == null) {
                        CORES = new ArrayList<>();
                    }
                    CORES.add(tx);
                    refreshAdapterCores();
                    et_cadastro_de_produto_cores.setText("");
                }

            }
        });

        recyclerCategorias.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CATEGORIA = null;
                AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(CadastroDeProdutoActivity.this, CadastroDeProdutoActivity.this);
                recyclerCategorias.setLayoutManager(new LinearLayoutManager(CadastroDeProdutoActivity.this, RecyclerView.HORIZONTAL, false));
                recyclerCategorias.setAdapter(adapterCategoriasCadastro);
                return false;
            }
        });

        switch_disponibilidade_cadastro_de_produto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_switch_disponibilidade_cadastro_de_produto.setText("Disponivel");
                } else {
                    tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
                }
            }
        });

        bt_add_fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cadastro_de_produto_fornecedores.getText().toString().length() > 0 && et_cadastro_de_produto_fornecedores_precos.getText().toString().length() > 0) {
                    FORNECEDORES.put(et_cadastro_de_produto_fornecedores.getText().toString(), Double.valueOf(et_cadastro_de_produto_fornecedores_precos.getText().toString()));
                    et_cadastro_de_produto_fornecedores_precos.setText("");
                    et_cadastro_de_produto_fornecedores_precos.clearFocus();
                    et_cadastro_de_produto_fornecedores.setText("");
                    et_cadastro_de_produto_fornecedores.clearFocus();
                    atulizarFornecedor(FORNECEDORES);
                }
            }
        });



        bt_cadastro_de_produto_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadding.setVisibility(View.VISIBLE);
                bt_cadastro_de_produto_salvar.setVisibility(View.GONE);

                if (prod != null) {
                    ProdObj prodObj = atualizarProduto(prod);
                    salvarAtualizacao(prodObj);
                    return;
                }

                ProdObj obj = verificarDados();
                if (obj == null) {
                    loadding.setVisibility(View.GONE);
                    bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                    return;
                }

                salvarDadosEmFirestore(obj.getImgCapa(), obj);


            }
        });

        efabExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prod == null) {
                    return;
                }

                loadding.setVisibility(View.VISIBLE);
                bt_cadastro_de_produto_salvar.setVisibility(View.GONE);

                DocumentReference documentReference = firestore.collection("produtos").document(prod.getIdProduto());
                documentReference.delete().addOnSuccessListener(CadastroDeProdutoActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        loadding.setVisibility(View.GONE);
                        bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


    }

    private void salvarAtualizacao(final ProdObj obj) {
        DocumentReference documentReference = firestore.collection("produtos").document(obj.getIdProduto());
        documentReference.set(obj).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                loadding.setVisibility(View.GONE);
                bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                Toast.makeText(CadastroDeProdutoActivity.this, "Erro ao atualizar produto", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        limparFormulario();
    }

    private void atulizarFornecedor(Map<String, Double> fornecedores) {

        fornecedoresString = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fornecedores.forEach(new BiConsumer<String, Double>() {
                @Override
                public void accept(String s, Double aDouble) {
                    fornecedoresString = fornecedoresString + s + " = " + String.valueOf(aDouble) + "\n";
                }
            });

            fornecedores_e_precos.setText(fornecedoresString);
        }
    }

    private ProdObj verificarDados() {

//        if (uriFotoProduto == null && prod == null) {
//            toast("Insira uma foto");
//            return null;
//        }

        if (preImagens == null || preImagens.size() == 0) {
            toast("Insira uma foto");
            return null;
        }

        String nome = pegarTexto(et_cadastro_de_produto_nome);

        if (nome == null) {
            toast("Insira um nome para o produto");
            return null;
        }

        String labo = pegarTexto(et_cadastro_de_produto_labo);

        if (labo == null) {
            toast("Insira o fabricante");
            return null;
        }

        String descricao = pegarTexto(et_cadastro_de_produto_descricao);

        if (descricao == null) {
            toast("adicione uma descricao");
            return null;
        }

        String quantidadeString = pegarTexto(et_cadastro_de_produto_quantidade);

        if (quantidadeString == null) {
            toast("Insira a quantidade do produto em estoque");
            return null;
        }

        int quantidadeInt = Integer.valueOf(quantidadeString);


        String comissaoString = pegarTexto(et_cadastro_de_produto_comissao);

        if (quantidadeString == null) {
            toast("Insira a comissão por item vendido");
            return null;
        }

        int comissaoInt = Integer.valueOf(comissaoString);


        String stringPrecoVenda = pegarTexto(et_cadastro_de_produto_preco_consumidor);

        if (stringPrecoVenda == null) {
            toast("Insira o preço de venda");
            return null;
        }
        float precoVenda = Float.valueOf(stringPrecoVenda);

        String stringPrecoAntigo = pegarTexto(et_cadastro_de_produto_preco_antigo);

        if (stringPrecoAntigo == null) {
            toast("Insira o preço de compra");
            return null;
        }
        float precoAntigo = Float.valueOf(stringPrecoAntigo);

        palavraChave(nome);

        if (tagList.size() < 3) {
            int faltamXTags = 3 - tagList.size();
            toast("Insira mais " + faltamXTags + " Palavra chave (Tag)");
            return null;
        }

        Map<String, Boolean> tagMap = new HashMap<>();
        for (int i = 0; i < tagList.size(); i++) {
            tagMap.put(tagList.get(i).toLowerCase(), true);
        }

        if (categoriaList.size() < 1) {
            toast("Insira uma categoria ");
            return null;
        }

        Map<String, Boolean> catMap = new HashMap<>();
        for (int i = 0; i < categoriaList.size(); i++) {
            catMap.put(categoriaList.get(i).toLowerCase(), true);
        }

        String pathFotoPrincipal = "";
        ArrayList<String> photos = new ArrayList<>();
        for (int i = 0; i < preImagens.size(); i++) {
            photos.add(preImagens.get(i).getPath());
            if (preImagens.get(i).isMain()) {
                pathFotoPrincipal = preImagens.get(i).getPath();
            }
        }

        ProdObj prodObj = new ProdObj(catMap, descricao, switch_disponibilidade_cadastro_de_produto.isChecked(), "null", pathFotoPrincipal, photos, labo, 7, nome, precoVenda,precoAntigo,  cb_cadastro_de_produto_promocional.isChecked(), tagMap, FORNECEDORES, quantidadeInt, System.currentTimeMillis(), comissaoInt, CORES);

        return prodObj;
    }

    private ProdObj atualizarProduto(ProdObj obj) {
        if (preImagens == null || preImagens.size() == 0) {
            toast("Insira uma foto");
            return null;
        }

        String nome = pegarTexto(et_cadastro_de_produto_nome);

        if (nome == null) {
            toast("Insira um nome para o produto");
            return null;
        }

        String labo = pegarTexto(et_cadastro_de_produto_labo);

        if (labo == null) {
            toast("Insira o fabricante");
            return null;
        }

        String descricao = pegarTexto(et_cadastro_de_produto_descricao);

        if (descricao == null) {
            toast("adicione uma descricao");
            return null;
        }

        String quantidadeString = pegarTexto(et_cadastro_de_produto_quantidade);

        if (quantidadeString == null) {
            toast("Insira a quantidade do produto em estoque");
            return null;
        }

        int quantidadeInt = Integer.valueOf(quantidadeString);


        String comissaoString = pegarTexto(et_cadastro_de_produto_comissao);

        if (quantidadeString == null) {
            toast("Insira a comissão por item vendido");
            return null;
        }

        int comissaoInt = Integer.valueOf(comissaoString);


        String stringPrecoVenda = pegarTexto(et_cadastro_de_produto_preco_consumidor);

        if (stringPrecoVenda == null) {
            toast("Insira o preço de venda");
            return null;
        }
        float precoVenda = Float.valueOf(stringPrecoVenda);

        String stringPrecoAntigo = pegarTexto(et_cadastro_de_produto_preco_antigo);

        if (stringPrecoAntigo == null) {
            toast("Insira o preço de compra");
            return null;
        }
        float precoAntigo = Float.valueOf(stringPrecoAntigo);


        palavraChave(nome);

        if (tagList.size() < 3) {
            int faltamXTags = 3 - tagList.size();
            toast("Insira mais " + faltamXTags + " Palavra chave (Tag)");
            return null;
        }

        Map<String, Boolean> tagMap = new HashMap<>();
        for (int i = 0; i < tagList.size(); i++) {
            tagMap.put(tagList.get(i), true);
        }

        if (categoriaList.size() < 1) {
            toast("Insira uma categoria ");
            return null;
        }

        Map<String, Boolean> catMap = new HashMap<>();
        for (int i = 0; i < categoriaList.size(); i++) {
            catMap.put(categoriaList.get(i).toLowerCase(), true);
        }

        String pathFotoPrincipal = "";
        ArrayList<String> photos = new ArrayList<>();

        for (int i = 0; i < preImagens.size(); i++) {
            photos.add(preImagens.get(i).getPath());
            Log.d("TesteImagemCadastro", "path: " + preImagens.get(i).getPath());
            if (preImagens.get(i).isMain()) {
                pathFotoPrincipal = preImagens.get(i).getPath();
            }
        }

        Log.d("TesteImagemCadastro", "tamanho lista: " + photos.size());

        ProdObj prodObj = new ProdObj(catMap, descricao, switch_disponibilidade_cadastro_de_produto.isChecked(), obj.getIdProduto(), pathFotoPrincipal, photos, labo, 7, nome, precoVenda, precoAntigo, cb_cadastro_de_produto_promocional.isChecked(), tagMap, FORNECEDORES, quantidadeInt, System.currentTimeMillis(), comissaoInt, CORES);

        return prodObj;
    }

    private void limparFormulario(){
        uriFotoProduto = null;
        preImagens = new ArrayList<>();

        rv_fotos_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapterCadastroFotos = new AdapterCadastroFotos(this, preImagens, this);
        rv_fotos_cadastro.setAdapter(adapterCadastroFotos);

        pathsCadProd = null;
        et_cadastro_de_produto_nome.setText("");
        et_cadastro_de_produto_nome.requestFocus();
        et_cadastro_de_produto_labo.setText("");
        et_cadastro_de_produto_labo.clearFocus();
        et_cadastro_de_produto_descricao.setText("");
        et_cadastro_de_produto_descricao.clearFocus();
        et_cadastro_de_produto_preco_consumidor.setText("");
        et_cadastro_de_produto_preco_consumidor.clearFocus();
        et_cadastro_de_produto_preco_antigo.setText("");
        et_cadastro_de_produto_preco_antigo.clearFocus();
        etTags.setText("");
        etTags.clearFocus();
        containerTags.setVisibility(View.GONE);
        CATEGORIA = null;
        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);
        cb_cadastro_de_produto_promocional.setChecked(false);
        loadding.setVisibility(View.GONE);
        bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
        limparTags();
        if (prod != null) {
            finish();
        }
    }

    private void palavraChave (String titulo) {
        int tamanho = titulo.length();

        StringBuilder palavraFracionada = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {

            String letraAtual = String.valueOf(titulo.charAt(i));
            palavraFracionada.append(letraAtual);
            Log.d("PalavraChave", palavraFracionada.toString());
            String pString = palavraFracionada.toString().toLowerCase();
            tagList.add(pString);

        }
    }

    private void toast(String s) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void atualizarListaTags() {

        String str = "";
        int tamanho = tagList.size();

        for (int i = 0; i < tamanho; i++) {
            if (i != (tamanho - 1)) {
                str = str + tagList.get(i) + ", ";
            } else {
                str = str + tagList.get(i);
            }
        }

        if (tamanho > 0) {
            containerTags.setVisibility(View.VISIBLE);
        }

        tvListTags.setText(str);
    }

    private String pegarTexto(TextInputEditText inputEditText) {
        String t = inputEditText.getText().toString();
        if (t.length() > 0) {
            return t;
        } else {
            return null;
        }
    }

    private void limparTags() {
        limparTagsArrayList();
        tvListTags.setText("");
        containerTags.setVisibility(View.GONE);
    }

    private void limparTagsArrayList() {
        tagList.clear();
        tagList = new ArrayList<>();
        categoriaList.clear();
        categoriaList = new ArrayList<>();
    }

    private void exibirFotoEscolhida(Uri uriFotoProduto) {
        ImagemPreUpload imagemPreUpload = new ImagemPreUpload(uriFotoProduto.getPath(),  uriFotoProduto, false, false);;
        if (preImagens.size() == 0) {
            imagemPreUpload.setMain(true);
        }
        preImagens.add(imagemPreUpload);
        adapterCadastroFotos = new AdapterCadastroFotos(this, preImagens, this);
        rv_fotos_cadastro.setAdapter(adapterCadastroFotos);
        salvarUmaFotoEmStorage(imagemPreUpload, preImagens.size() - 1);
    }

    private void escolherFoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO);
        } else {
            Toast.makeText(this, "Não é possivel escolher uma foto atualmente do seu aparelho", Toast.LENGTH_SHORT).show();
        }
    }

    private String getNomeImagem(String s) {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append("_");
        stringBuilder.append(s);
        stringBuilder.append(".jpeg");
        return stringBuilder.toString();
    }

    private void salvarUmaFotoEmStorage(final ImagemPreUpload imagemPreUpload, final int position) {
        StorageReference child = this.storageReference.child("produto");

        final StorageReference child2 = child.child(imagemPreUpload.getUri().getLastPathSegment());

        child2.putFile(imagemPreUpload.getUri()).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return child2.getDownloadUrl();
                }
                throw task.getException();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {

                    //salvarDadosEmFirestore(((Uri) task.getResult()).toString(), obj);
                    Uri uriImg = (Uri) task.getResult();
                    preImagens.get(position).setPath(((Uri) task.getResult()).toString());
                    preImagens.get(position).setLiberada(true);
                    preImagens.get(position).setUri(uriImg);

                    adapterCadastroFotos = new AdapterCadastroFotos(CadastroDeProdutoActivity.this, preImagens, CadastroDeProdutoActivity.this);
                    rv_fotos_cadastro.setAdapter(adapterCadastroFotos);

                    return;
                }
                Toast.makeText(CadastroDeProdutoActivity.this, "Erro ao salvar Foto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarFotoEmStorage(final ProdObj obj) {
        StorageReference child = this.storageReference.child("produto");

        final StorageReference child2 = child.child(getNomeImagem(obj.prodName));

        child2.putFile(uriFotoProduto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return child2.getDownloadUrl();
                }
                throw task.getException();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {

                    salvarDadosEmFirestore(((Uri) task.getResult()).toString(), obj);
                    return;
                }
                loadding.setVisibility(View.GONE);
                bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                Toast.makeText(CadastroDeProdutoActivity.this, "Erro ao salvar Foto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarDadosEmFirestore(String pathFoto, final ProdObj obj) {



        DocumentReference documentReference = firestore.collection("produtos").document();
        final String prodId = documentReference.getId();

        ProdObj newProd = new ProdObj(obj.getCategorias(), obj.getDescr(), obj.isDisponivel(), prodId, pathFoto, obj.getImagens(), obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), System.currentTimeMillis(), obj.getComissao(), obj.getCores());

        documentReference.set(newProd).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                limparFormulario();
                /*
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String texto = "https://amacompras.com.br/produto/?id=" + prodId;
                ClipData clip = ClipData.newPlainText("produto", texto);
                clipboard.setPrimaryClip(clip);


                 */
                //Toast.makeText(CadastroDeProdutoActivity.this, "Link copiado", Toast.LENGTH_LONG).show();
//                23:06   23-04
            }
        });

    }

    private void salvarProdutoNoFeed(String pathFoto, final ProdObj obj) {

        WriteBatch batch = firestore.batch();

        DocumentReference feedMain = firestore.collection("Feed").document("main");

        DocumentReference documentReference = firestore.collection("produtos").document();

        final String prodId = documentReference.getId();

        ProdObj newProd = new ProdObj(obj.getCategorias(), obj.getDescr(), obj.isDisponivel(), prodId, pathFoto, obj.getImagens(), obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), System.currentTimeMillis(), obj.getComissao(), obj.getCores());

    }

    @Override
    public void mudanca(String id, int pos, boolean adicionar) {

        if (categoriaList == null) {
            categoriaList = new ArrayList<>();
        }

        if (adicionar) {
            if (!categoriaList.contains(String.valueOf(pos))) {
                categoriaList.add(String.valueOf(pos));
            }
        } else {
            if (categoriaList.contains(String.valueOf(pos))) {
                categoriaList.remove(String.valueOf(pos));
            }
        }

        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);
    }

    @Override
    public void remover(int pos, String path) {

        ArrayList<ImagemPreUpload> preImagensNova = new ArrayList<>();

        ImagemPreUpload imupRemocao = preImagens.get(pos);

        preImagens.remove(pos);

        for (int i = 0; i < preImagens.size(); i++) {
            ImagemPreUpload imup = preImagens.get(i);
            if (imup == null || imup.getPath().length() == 0) {

            } else {
                preImagensNova.add(imup);
            }
        }

        if (imupRemocao.isMain() && preImagensNova.size() > 0) {
            preImagensNova.get(0).setMain(true);
        }

        preImagens = preImagensNova;

        refreshAdapterFotos();
    }

    @Override
    public void adicionar() {
        escolherFoto();

    }

    @Override
    public void setPrincipal(int pos) {


        for (int i = 0; i < preImagens.size(); i++) {
            if (preImagens.get(i).isMain()) {
                preImagens.get(i).setMain(false);
            }
        }

        preImagens.get(pos).setMain(true);
        refreshAdapterFotos();
    }

    private void refreshAdapterFotos() {
        rv_fotos_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapterCadastroFotos = new AdapterCadastroFotos(this, preImagens, this);
        rv_fotos_cadastro.setAdapter(adapterCadastroFotos);
    }

    private void refreshAdapterCores() {
        AdapterCoresCadastro adapterCoresCadastro = new AdapterCoresCadastro(CORES, this, this);
        rv_cores_cadastro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_cores_cadastro.setAdapter(adapterCoresCadastro);
    }

    @Override
    public void remov(int pos) {

        CORES.remove(pos);

        refreshAdapterCores();
    }

}

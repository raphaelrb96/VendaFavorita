package com.rapha.vendafavorita;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.adapter.AdapterListaRevenda;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.CompraFinalParcelable;
import com.rapha.vendafavorita.objects.EntregaObj;
import com.rapha.vendafavorita.objects.GarantiaObj;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.PagamentosObj;
import com.rapha.vendafavorita.objects.ParcelamentoObj;
import com.rapha.vendafavorita.util.FormatoString;
import com.rapha.vendafavorita.util.Listas;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.BottomSheetOptions.TYPE_ENTREGA;
import static com.rapha.vendafavorita.BottomSheetOptions.TYPE_GARANTIA;
import static com.rapha.vendafavorita.BottomSheetOptions.TYPE_PAGAMENTO;
import static com.rapha.vendafavorita.BottomSheetOptions.TYPE_PARCELAMENTO;
import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;

public class ListaRevendaActivity extends AppCompatActivity implements AdapterListaRevenda.RevendaListaner, BottomSheetOptions.ListenerBottomSheetOption {


    private QuerySnapshot querySnapshot;
    private ArrayList<ObjProdutoRevenda> objProdutoRevendas;
    private String detalhePagamento = "";
    private CompraFinalParcelable cfp = null;
    private String nomeCliente = "";
    private int somo, totalVenda;
    private String observacoes = "";
    private String telefoneMain = "";
    private String ruaMain = "";
    private String bairroMain = "";
    private String nCasaMain = "";
    private String nCidadeMain = "Manaus";
    private String nCepMain = "69000-000";
    private String nEstadoMain = "Amazonas";
    private GarantiaObj garantiaFinal = Listas.getListOptionsGarantia(totalVenda).get(0);
    private EntregaObj entregaFinal = Listas.getListOptionsEntregas().get(0);
    private PagamentosObj pagamentoFinal = Listas.getListOptionsPagamentos().get(0);
    private ParcelamentoObj parcelaFinal = Listas.getListOptionsParcelamento(totalVenda).get(0);



    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private CollectionReference listaRevenda;

    private RecyclerView rv;
    private AdapterListaRevenda adapter;

    private ProgressBar pb;
    private TickerView totalComissaoTV;
    private TickerView total_venda_lista_revenda;
    private TextView title_rv_minhas_ultimas_revendas;
    private TextView title_garantia_pedido, desc_garantia_pedido, valor_garantia_pedido;
    private TextView title_entrega_pedido, desc_entrega_pedido, valor_entrega_pedido;
    private TextView title_pag_pedido, desc_pag_pedido, valor_pag_pedido;
    private TextView title_parcela_pedido, desc_parcela_pedido, valor_parcela_pedido;
    private TextInputEditText et_celular_conf_revenda, et_nome_conf_revenda, et_bairro_conf_revenda, et_rua_conf_revenda, et_obs_conf_revenda, et_cep_conf_revenda, et_cidade_conf_revenda, et_estado_conf_revenda;
    private View voltar;
    private CardView option_garantia_pedido, option_frete_pedido, option_pag_form_pedido, option_pag_parcelamento_pedido;
    private Toast mToast = null;
    private AnalitycsGoogle analitycsGoogle;
    private BottomSheetOptions bottomSheetPro;
    private LinearLayout container_parcelamento_pedido;
    private TextView tv_error_pedido_vazio;
    private MaterialButton btn_confirmar_pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_revenda);
        pb = (ProgressBar) findViewById(R.id.pb_lista_revenda);
        rv = (RecyclerView) findViewById(R.id.rv_lista_revenda);
        voltar = (View) findViewById(R.id.voltar_lista_revendas);
        et_celular_conf_revenda = (TextInputEditText) findViewById(R.id.et_celular_conf_revenda);
        et_nome_conf_revenda = (TextInputEditText) findViewById(R.id.et_nome_conf_revenda);
        et_bairro_conf_revenda = (TextInputEditText) findViewById(R.id.et_bairro_conf_revenda);
        et_rua_conf_revenda = (TextInputEditText) findViewById(R.id.et_rua_conf_revenda);
        et_obs_conf_revenda = (TextInputEditText) findViewById(R.id.et_obs_conf_revenda);
        et_cep_conf_revenda = (TextInputEditText) findViewById(R.id.et_cep_conf_revenda);
        et_cidade_conf_revenda = (TextInputEditText) findViewById(R.id.et_cidade_conf_revenda);
        et_estado_conf_revenda = (TextInputEditText) findViewById(R.id.et_estado_conf_revenda);

        option_garantia_pedido = (CardView) findViewById(R.id.option_garantia_pedido);
        option_frete_pedido = (CardView) findViewById(R.id.option_frete_pedido);
        option_pag_form_pedido = (CardView) findViewById(R.id.option_pag_form_pedido);
        option_pag_parcelamento_pedido = (CardView) findViewById(R.id.option_pag_parcelamento_pedido);

        totalComissaoTV = (TickerView) findViewById(R.id.total_lista_revenda);
        total_venda_lista_revenda = (TickerView) findViewById(R.id.total_venda_lista_revenda);
        title_rv_minhas_ultimas_revendas = (TextView) findViewById(R.id.title_rv_minhas_ultimas_revendas);
        title_garantia_pedido = (TextView) findViewById(R.id.title_garantia_pedido);
        desc_garantia_pedido = (TextView) findViewById(R.id.desc_garantia_pedido);
        valor_garantia_pedido = (TextView) findViewById(R.id.valor_garantia_pedido);
        title_entrega_pedido = (TextView) findViewById(R.id.title_entrega_pedido);
        desc_entrega_pedido = (TextView) findViewById(R.id.desc_entrega_pedido);
        valor_entrega_pedido = (TextView) findViewById(R.id.valor_entrega_pedido);
        title_pag_pedido = (TextView) findViewById(R.id.title_pag_pedido);
        desc_pag_pedido = (TextView) findViewById(R.id.desc_pag_pedido);
        valor_pag_pedido = (TextView) findViewById(R.id.valor_pag_pedido);
        title_parcela_pedido = (TextView) findViewById(R.id.title_parcela_pedido);
        desc_parcela_pedido = (TextView) findViewById(R.id.desc_parcela_pedido);
        valor_parcela_pedido = (TextView) findViewById(R.id.valor_parcela_pedido);
        tv_error_pedido_vazio = (TextView) findViewById(R.id.tv_error_pedido_vazio);


        container_parcelamento_pedido = (LinearLayout) findViewById(R.id.container_parcelamento_pedido);
        btn_confirmar_pedido = (MaterialButton) findViewById(R.id.btn_confirmar_pedido);

        firestore = FirebaseFirestore.getInstance();
        total_venda_lista_revenda.setCharacterLists(TickerUtils.provideNumberList());
        auth = FirebaseAuth.getInstance();

        if (user == null) {
            user = auth.getCurrentUser();
        }

        if (documentoPrincipalDoUsuario == null || documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {
            Intent intent = new Intent(ListaRevendaActivity.this, MeuPerfilActivity.class);
            intent.putExtra("alert", 2);
            startActivity(intent);
            finish();
        }

        analitycsGoogle = new AnalitycsGoogle(this);

        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listaRevenda = firestore.collection("listaRevendas").document("usuario").collection(user.getUid());


        editTextFunctions();

        botoesListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

       Log.d("TestePagamento", "onresume");

    }

    private void botoesListener () {

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        option_frete_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = -1;
                if(entregaFinal != null) {
                    id = entregaFinal.getId();
                }
                showBottomSheetOption("Entrega", TYPE_ENTREGA, id);
            }
        });

        option_garantia_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = -1;
                if(garantiaFinal != null) {
                    id = garantiaFinal.getId();
                }
                showBottomSheetOption("Garantia", TYPE_GARANTIA, id);
            }
        });

        option_pag_form_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = -1;
                if(pagamentoFinal != null) {
                    id = pagamentoFinal.getId();
                }
                showBottomSheetOption("Pagamento", TYPE_PAGAMENTO, id);
            }
        });

        option_pag_parcelamento_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = -1;
                if(parcelaFinal != null) {
                    id = parcelaFinal.getId();
                }
                showBottomSheetOption("Parcelamento", TYPE_PARCELAMENTO, id);
            }
        });

        btn_confirmar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (objProdutoRevendas.size() == 0) return;

                telefoneMain = et_celular_conf_revenda.getText().toString();
                bairroMain = et_bairro_conf_revenda.getText().toString();
                ruaMain = et_rua_conf_revenda.getText().toString();
                nomeCliente = et_nome_conf_revenda.getText().toString();

                final ObjectRevenda objectRevenda = getDadosCompra();
                if (objectRevenda == null) {
                    return;
                }



                Log.d("TesteCadastroAfiliados", "Produto: " + objectRevenda.getListaDeProdutos().get(0).getProdutoName());

                showDialogCompra(1, "Você deseja concluir sua venda ?", objectRevenda);
            }
        });

    }

    private void showBottomSheetOption(String tag, int type, int id) {
        int ttal = getTotal();
        if(type == TYPE_GARANTIA) {
            ttal = getTotalItens();
        }
        bottomSheetPro = BottomSheetOptions.newInstance(type, ttal, id);
        bottomSheetPro.setListener(this);
        bottomSheetPro.show(getSupportFragmentManager(), tag);
    }

    private void analizarFrete() {
        if(entregaFinal.getId() == 4) {
            nCidadeMain = "";
            nCepMain = "";
            nEstadoMain = "";
        } else {
            nCidadeMain = "Manaus";
            nCepMain = "69000-000";
            nEstadoMain = "Amazonas";
        }
        editTextUpdateView();
    }

    private void editTextUpdateView() {
        et_cep_conf_revenda.setText(nCepMain);
        et_cidade_conf_revenda.setText(nCidadeMain);
        et_estado_conf_revenda.setText(nEstadoMain);
    }

    private void editTextFunctions() {

        editTextUpdateView();

        et_celular_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null) {
                    telefoneMain = s.toString();
                } else {
                    telefoneMain = "";
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_nome_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomeCliente = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_bairro_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bairroMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_rua_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ruaMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_obs_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null) {
                    observacoes = s.toString();
                } else {
                    observacoes = "";
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_cep_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nCepMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_cidade_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nCidadeMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_estado_conf_revenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nEstadoMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (documentoPrincipalDoUsuario == null || documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {
            Intent intent = new Intent(ListaRevendaActivity.this, MeuPerfilActivity.class);
            intent.putExtra("alert", 2);
            startActivity(intent);
            finish();
        }

        Log.d("TestePagamento", "onstart");

        pb.setVisibility(View.VISIBLE);

        if (!ADMINISTRADOR) {

            analitycsGoogle.logViewCartRevenda(user.getDisplayName(), user.getUid(), pathFotoUser);

        }

        listaRevenda.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                querySnapshot = snapshot;
                updateScreen();

            }
        });
    }

    private int getTotal() {
        int ttVenda = 0;
        for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
            ObjProdutoRevenda carComprasActivy = (ObjProdutoRevenda) querySnapshot.getDocuments().get(i).toObject(ObjProdutoRevenda.class);
            ttVenda = ttVenda + (carComprasActivy.getValorUni() * carComprasActivy.getQuantidade());
        }
        ttVenda = ttVenda + garantiaFinal.getValor();
        ttVenda = ttVenda + entregaFinal.getValor();
        return ttVenda;
    }

    private int getTotalItens() {
        int ttVenda = 0;
        for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
            ObjProdutoRevenda carComprasActivy = (ObjProdutoRevenda) querySnapshot.getDocuments().get(i).toObject(ObjProdutoRevenda.class);
            ttVenda = ttVenda + (carComprasActivy.getValorUni() * carComprasActivy.getQuantidade());
        }
        return ttVenda;
    }

    private void emptyScreen() {
        title_rv_minhas_ultimas_revendas.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        tv_error_pedido_vazio.setVisibility(View.VISIBLE);
        totalComissaoTV.setText("R$ " + 0 + ",00");
        total_venda_lista_revenda.setText("R$ " + 0 + ",00");
    }

    private void updateListPedido() {
        if (adapter == null) {
            adapter = new AdapterListaRevenda(ListaRevendaActivity.this, objProdutoRevendas, ListaRevendaActivity.this);
            rv.setAdapter(adapter);
        } else {
            adapter.atualizarLista(objProdutoRevendas);
        }
    }

    private void showContent() {
        updateListPedido();

        title_rv_minhas_ultimas_revendas.setVisibility(View.VISIBLE);
        rv.setVisibility(View.VISIBLE);
        tv_error_pedido_vazio.setVisibility(View.GONE);

        totalComissaoTV.setText(FormatoString.formartarPreco(somo));
        total_venda_lista_revenda.setText(FormatoString.formartarPreco(totalVenda));

        title_garantia_pedido.setText(garantiaFinal.getTitulo());
        valor_garantia_pedido.setText(garantiaFinal.getValorString());
        desc_garantia_pedido.setText(garantiaFinal.getDescricao());

        title_entrega_pedido.setText(entregaFinal.getTitulo());
        desc_entrega_pedido.setText(entregaFinal.getDescricao());
        valor_entrega_pedido.setText(entregaFinal.getValorString());

        title_pag_pedido.setText(pagamentoFinal.getTitulo());
        desc_pag_pedido.setText(pagamentoFinal.getDescricao());
        valor_pag_pedido.setText(pagamentoFinal.getValorString());

        title_parcela_pedido.setText(parcelaFinal.getTitulo());
        desc_parcela_pedido.setText(parcelaFinal.getDescricao());
        valor_parcela_pedido.setText(parcelaFinal.getValorString());


    }

    private void updateScreen() {

        pb.setVisibility(View.GONE);

        somo = 0;
        totalVenda = 0;
        objProdutoRevendas = new ArrayList<>();

        if (querySnapshot == null) {
            emptyScreen();
            return;
        }

        if (querySnapshot.getDocuments().size() == 0) {
            emptyScreen();
            return;
        }

        for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
            ObjProdutoRevenda carComprasActivy = (ObjProdutoRevenda) querySnapshot.getDocuments().get(i).toObject(ObjProdutoRevenda.class);
            somo = somo + (carComprasActivy.getComissaoUnidade() * carComprasActivy.getQuantidade());
            totalVenda = totalVenda + (carComprasActivy.getValorUni() * carComprasActivy.getQuantidade());
            objProdutoRevendas.add(carComprasActivy);
        }

        if(garantiaFinal == null) {
            garantiaFinal = Listas.getListOptionsGarantia(getTotalItens()).get(0);
        } else {
            garantiaFinal = Listas.getListOptionsGarantia(getTotalItens()).get(garantiaFinal.getId());
        }

        totalVenda = totalVenda + garantiaFinal.getValor();
        totalVenda = totalVenda + entregaFinal.getValor();

        if(pagamentoFinal.getId() == 2 || pagamentoFinal.getId() == 6) {
            if(parcelaFinal == null) {
                parcelaFinal = Listas.getListOptionsParcelamento(totalVenda).get(0);
            } else {
                parcelaFinal = Listas.getListOptionsParcelamento(totalVenda).get(parcelaFinal.getId()-1);
            }
            totalVenda = parcelaFinal.getTotal();

            container_parcelamento_pedido.setVisibility(View.VISIBLE);
        } else {
            container_parcelamento_pedido.setVisibility(View.GONE);
        }

        showContent();


    }

    private void esconderTeclado(TextInputEditText et) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    private ObjectRevenda getDadosCompra() {

        String enderecoCompacto = "";

        observacoes = "Teste teste teste";
        telefoneMain = "1234567890";
        ruaMain = "Teste teste teste";
        bairroMain = "Teste teste teste";
        nomeCliente = "Teste teste teste";

        if (nomeCliente.length() < 2) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira o nome da pessoa que vai receber o pedido", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (telefoneMain.length() < 8) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira os 8 digitos do seu número", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (bairroMain.length() < 1) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira seu bairro", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (ruaMain.length() < 1) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira sua rua", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        int valorTotalVenda = totalVenda;

        ObjectRevenda objectRevenda = new ObjectRevenda(ruaMain, somo, bairroMain, valorTotalVenda, detalhePagamento, pagamentoFinal.getId(), entregaFinal.getId(), 0, null, 0, objProdutoRevendas, 0, nomeCliente, false, auth.getCurrentUser().getPhotoUrl().getPath(), telefoneMain, 1, 0, auth.getUid(), auth.getCurrentUser().getDisplayName(), totalVenda, false, documentoPrincipalDoUsuario.isAdmConfirmado(), documentoPrincipalDoUsuario.getUidAdm(), 0, null, garantiaFinal, entregaFinal, pagamentoFinal, parcelaFinal, nCepMain, nCidadeMain, nEstadoMain, observacoes);

        return objectRevenda;
    }

    private void showDialogCompra(int tipo, String msg, final ObjectRevenda objectRevenda) {

        Log.d("TesteCadastroAfiliados", "Produto 1 - showDialog: " + objectRevenda.getListaDeProdutos().get(0).getProdutoName());

        switch (tipo) {
            case 1:
                //R.style.AppCompatAlertDialog
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(this)
                        .setTitle("Confirmar Compra")
                        .setMessage(msg)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                concluirPedidoDeCompra(objectRevenda);
                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 2:
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(this)
                        .setTitle("Confirmar Compra")
                        .setMessage("Encerramos nossas entregas por hoje, se confirmar o pedido será amanhã pela manhã.\nConfirma compra assim mesmo ?\n\n" + msg)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                concluirPedidoDeCompra(objectRevenda);
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Não foi possivel concluir sua compra agora. Tente novamente mais tarde")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 4:
                //alerta de confirmação
                AlertDialog.Builder dialogConclusao = new AlertDialog.Builder(this)
                        .setTitle("Parabéns")
                        .setMessage("Sua venda foi um sucesso. Obrigado por fazer parte da nossa equipe ! Em breve seu cliente receberá a encomenda")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ListaRevendaActivity.this, PainelRevendedorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog alertDialogConclusao = dialogConclusao.create();
                alertDialogConclusao.show();
                alertDialogConclusao.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorSecondaryDark));
                break;
            default:
                break;
        }
    }

    private void concluirPedidoDeCompra(final ObjectRevenda objRev) {

        btn_confirmar_pedido.setEnabled(false);
        pb.setVisibility(View.VISIBLE);


        WriteBatch batch = firestore.batch();
        DocumentReference revendaRef = firestore.collection("Revendas").document();

        String idRevenda = revendaRef.getId();
        boolean possuiComissaoAfiliados = false;

        DocumentReference minhaRevendaRef = firestore.collection("MinhasRevendas").document("Usuario").collection(auth.getCurrentUser().getUid()).document(idRevenda);

        if (documentoPrincipalDoUsuario.isAdmConfirmado()) {

            DocumentReference comissaoRef = firestore.collection("ComissoesAfiliados").document(idRevenda);
            DocumentReference minhaComissaoRef = firestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(documentoPrincipalDoUsuario.getUidAdm()).document(idRevenda);

            possuiComissaoAfiliados = true;

            String tituloComissao = "Venda da @" + documentoPrincipalDoUsuario.getUserName();

            String produtoAtual = "";

            for (int i = 0; i < objRev.getListaDeProdutos().size(); i++) {

                String prodAt = objRev.getListaDeProdutos().get(i).getQuantidade() + " " + objRev.getListaDeProdutos().get(i).getProdutoName();

                if (i > 0) {
                    produtoAtual = produtoAtual + ", " + prodAt;
                } else {
                    produtoAtual = prodAt;
                }

            }

            String descricaoComissao = "De " + produtoAtual;

            ComissaoAfiliados comissaoAfiliados = new ComissaoAfiliados(idRevenda, 5, tituloComissao, descricaoComissao, System.currentTimeMillis(), documentoPrincipalDoUsuario.getUid(), documentoPrincipalDoUsuario.getNome(), documentoPrincipalDoUsuario.getPathFoto(), false, objRev.getStatusCompra());
            batch.set(minhaComissaoRef, comissaoAfiliados);
            batch.set(comissaoRef, comissaoAfiliados);

        }

        //colocar referencia a minhas revendas

        final ObjectRevenda novaCompra = new ObjectRevenda(objRev.getAdress(), objRev.getComissaoTotal(), objRev.getComplemento(), objRev.getCompraValor(), objRev.getDetalhePag(), objRev.getFormaDePagar(), objRev.getFrete(), System.currentTimeMillis(), revendaRef.getId(), objRev.getLat(), objRev.getListaDeProdutos(), objRev.getLng(), objRev.getNomeCliente(), objRev.isPagamentoRecebido(), objRev.getPathFotoUserRevenda(), objRev.getPhoneCliente(), objRev.getStatusCompra(), objRev.getTipoDeEntrega(), objRev.getUidUserRevendedor(), objRev.getUserNomeRevendedor(), objRev.getValorTotal(), objRev.isVendaConcluida(), possuiComissaoAfiliados, documentoPrincipalDoUsuario.getUidAdm(), objRev.getIdCancelamento(), objRev.getDetalheCancelamento(), objRev.getGarantiaFinal(), objRev.getEntregaFinal(), objRev.getPagamentoFinal(), objRev.getParcelaFinal(), objRev.getCep(), objRev.getCidade(), objRev.getEstado(), objRev.getObs());


        batch.set(revendaRef, novaCompra);
        batch.set(minhaRevendaRef, novaCompra);


        for (int i = 0; i < novaCompra.getListaDeProdutos().size(); i++) {
            batch.delete(listaRevenda.document(novaCompra.getListaDeProdutos().get(i).getIdProdut()));
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if (!ADMINISTRADOR) {

                    analitycsGoogle.logVenda(novaCompra);

                    analitycsGoogle.logRevenda(user.getDisplayName(), user.getUid(), pathFotoUser);


                }

                Intent intent = new Intent(ListaRevendaActivity.this, PainelRevendedorActivity.class);
                Toast.makeText(ListaRevendaActivity.this, "Parabens, Sua venda foi um sucesso.", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                btn_confirmar_pedido.setEnabled(true);
                pb.setVisibility(View.GONE);
                showDialogCompra(3, "", null);
            }
        });
    }

    @Override
    public void alteracao(ObjProdutoRevenda prod) {
        listaRevenda.document(prod.getIdProdut()).set(prod);
    }

    @Override
    public void deletar(ObjProdutoRevenda prod) {
        listaRevenda.document(prod.getIdProdut()).delete();
    }

    @Override
    public void editar(ObjProdutoRevenda obj) {

    }

    @Override
    public void clickBottomSheetOption(String s, int pos, int type, int id) {
        switch (type) {
            case TYPE_GARANTIA:
                garantiaFinal = Listas.getListOptionsGarantia(getTotalItens()).get(pos);
                break;
            case TYPE_ENTREGA:
                entregaFinal = Listas.getListOptionsEntregas().get(pos);
                analizarFrete();
                break;
            case TYPE_PAGAMENTO:
                pagamentoFinal = Listas.getListOptionsPagamentos().get(pos);
                break;
            case TYPE_PARCELAMENTO:
                parcelaFinal = Listas.getListOptionsParcelamento(totalVenda).get(pos);
                break;
        }

        bottomSheetPro.dismiss();
        updateScreen();
    }


}

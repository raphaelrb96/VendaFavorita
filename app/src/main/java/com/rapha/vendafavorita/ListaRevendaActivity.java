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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.CompraFinalParcelable;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;

public class ListaRevendaActivity extends AppCompatActivity implements AdapterListaRevenda.RevendaListaner {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private CollectionReference listaRevenda;
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView totalComissaoTV;
    private TickerView total_venda_lista_revenda;
    private TextView title_rv_minhas_ultimas_revendas;
    private AdapterListaRevenda adapter;
    private TextInputEditText et_celular_conf_revenda, et_nome_conf_revenda, et_bairro_conf_revenda, et_rua_conf_revenda;
    private CheckBox cbDinheiro;
    private CheckBox cbDebito;
    private CheckBox cbCredito;
    private CheckBox cbAlimentacao;
    private CheckBox cbSemTroco;
    private TextView tvDebito;
    private TextView tvCredito;
    private TextView tvAlimentacao;
    private TextInputEditText etTroco;
    private String detalhePagamento = "";
    private int tipoDePagamento = 0, tipoEntrega = 1;

    private LinearLayout efab_concluir_revenda;

    private String[] cDebito = {
            "ELO",
            "VISA",
            "MASTERCARD",
            "CABAL"
    };

    private String[] cCredito = {
            "ELO",
            "VISA",
            "MASTERCARD",
            "CABAL",
            "HIPER",
            "HIPERCARD"
    };

    private String[] cAlimentacao = {
            "ALELO",
            "VR",
            "TICKET",
            "SODEXO"
    };
    private CompraFinalParcelable cfp = null;
    private String telefoneMain = "";
    private String ruaMain = "";
    private String bairroMain = "";
    private String nCasaMain = "";
    private Toast mToast;

    private View voltar;
    private String nomeCliente;
    private int somo, totalVenda;
    private ArrayList<ObjProdutoRevenda> objProdutoRevendas;
    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;


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
        etTroco = (TextInputEditText) findViewById(R.id.text_input_troco_conf_revenda);
        totalComissaoTV = (TextView) findViewById(R.id.total_lista_revenda);
        total_venda_lista_revenda = (TickerView) findViewById(R.id.total_venda_lista_revenda);
        title_rv_minhas_ultimas_revendas = (TextView) findViewById(R.id.title_rv_minhas_ultimas_revendas);

        cbDinheiro = (CheckBox) findViewById(R.id.cb_dinheiro);
        cbDebito = (CheckBox) findViewById(R.id.cb_debito);
        cbCredito = (CheckBox) findViewById(R.id.cb_credito);
        cbAlimentacao = (CheckBox) findViewById(R.id.cb_alimentacao);
        cbSemTroco = (CheckBox) findViewById(R.id.cb_nao_precisa_troco);

        tvDebito = (TextView) findViewById(R.id.tv_debito);
        tvCredito = (TextView) findViewById(R.id.tv_credito);
        tvAlimentacao = (TextView) findViewById(R.id.tv_alimentacao);

        efab_concluir_revenda = (LinearLayout) findViewById(R.id.efab_concluir_revenda);

        firestore = FirebaseFirestore.getInstance();
        total_venda_lista_revenda.setCharacterList(TickerUtils.getDefaultNumberList());
        auth = FirebaseAuth.getInstance();

        if (user == null) {
            user = auth.getCurrentUser();
        }

        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);

        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listaRevenda = firestore.collection("listaRevendas").document("usuario").collection(user.getUid());


        etListener();

        botoesListener();

        cbListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

       Log.d("TestePagamento", "onresume");

    }

    private void cbListener() {

        cbDinheiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etTroco.requestFocus();
                    cbAlimentacao.setChecked(false);
                    cbDebito.setChecked(false);
                    cbCredito.setChecked(false);
                    tvAlimentacao.setText("");
                    tvCredito.setText("");
                    tvDebito.setText("");
                    totalComissaoTV.setText("R$ " + somo + ",00");
                    total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");

                    tipoDePagamento = 4;
                    totalComissaoTV.setText("R$ " + somo + ",00");
                    total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");


                } else {
                    cbSemTroco.setChecked(false);
                    etTroco.setText("");
                }

            }
        });

        cbAlimentacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAlimentacao.isChecked()) {
                    tipoDePagamento = 3;
                    int juros = (totalVenda * 10) / 100;
                    int somaComJuros = juros + totalVenda;

                    total_venda_lista_revenda.setText("R$ " + somaComJuros + ",00");
                    myShowDialog(3);
                } else {
                    tvAlimentacao.setText("");
                    detalhePagamento = "";
                }

            }
        });

        cbCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbCredito.isChecked()) {
                    tipoDePagamento = 2;
                    int juros = (totalVenda * 10) / 100;
                    int somaComJuros = juros + totalVenda;

                    total_venda_lista_revenda.setText("R$ " + somaComJuros + ",00");
                    myShowDialog(2);
                } else {
                    tvDebito.setText("");
                    detalhePagamento = "";
                }

            }
        });

        cbDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbDebito.isChecked()) {
                    tipoDePagamento = 1;
                    int juros = (totalVenda * 10) / 100;
                    int somaComJuros = juros + totalVenda;

                    total_venda_lista_revenda.setText("R$ " + somaComJuros + ",00");


                    myShowDialog(1);
                } else {
                    tvDebito.setText("");
                    detalhePagamento = "";
                }

            }
        });

        cbSemTroco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    detalhePagamento = "está trocado";
                    etTroco.clearFocus();
                    cbDinheiro.setChecked(true);
                    esconderTeclado(etTroco);
                } else {
                    detalhePagamento = "";
                    etTroco.requestFocus();
                }
            }
        });

    }

    private void botoesListener () {

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        efab_concluir_revenda.setOnClickListener(new View.OnClickListener() {
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

                efab_concluir_revenda.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);

                Log.d("TesteCadastroAfiliados", "Produto: " + objectRevenda.getListaDeProdutos().get(0).getProdutoName());

                showDialogCompra(1, "Você deseja concluir sua venda ?", objectRevenda);
            }
        });

    }

    private void etListener () {
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

        etTroco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detalhePagamento = etTroco.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            et_celular_conf_revenda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        esconderTeclado(et_celular_conf_revenda);
                        et_celular_conf_revenda.clearFocus();
                    }
                    return false;
                }
            });
        }

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("TestePagamento", "onstart");

        pb.setVisibility(View.VISIBLE);

        if (!ADMINISTRADOR) {

            analitycsFacebook.logViewCartRevendaEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.logViewCartRevenda(user.getDisplayName(), user.getUid(), pathFotoUser);

        }

        listaRevenda.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                pb.setVisibility(View.GONE);

                objProdutoRevendas = new ArrayList<>();



                if (querySnapshot == null) {

                    somo = 0;
                    totalVenda = 0;

                    return;
                }

                if (querySnapshot.getDocuments().size() == 0) {

                    somo = 0;
                    totalVenda = 0;

                    title_rv_minhas_ultimas_revendas.setText("Nenhum produto na lista");
                    totalComissaoTV.setText("R$ " + 0 + ",00");
                    total_venda_lista_revenda.setText("R$ " + 0 + ",00");

                    if (adapter == null) {
                        adapter = new AdapterListaRevenda(ListaRevendaActivity.this, objProdutoRevendas, ListaRevendaActivity.this);
                        rv.setAdapter(adapter);
                    } else {
                        adapter.atualizarLista(objProdutoRevendas);
                    }

                    return;

                }

                somo = 0;
                totalVenda = 0;


                for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                    ObjProdutoRevenda carComprasActivy = (ObjProdutoRevenda) querySnapshot.getDocuments().get(i).toObject(ObjProdutoRevenda.class);

                    Log.d("TesteCadastroAfiliados", "Produto: " + carComprasActivy.getProdutoName());

                    somo = somo + carComprasActivy.getComissaoTotal();
                    totalVenda = totalVenda + carComprasActivy.getValorTotalComComissao();
                    objProdutoRevendas.add(carComprasActivy);
                }


                if (adapter == null) {
                    adapter = new AdapterListaRevenda(ListaRevendaActivity.this, objProdutoRevendas, ListaRevendaActivity.this);
                    rv.setAdapter(adapter);
                } else {
                    adapter.atualizarLista(objProdutoRevendas);
                }


                if (tipoDePagamento != 0 && tipoDePagamento != 4) {

                    int juros = (totalVenda * 10) / 100;
                    int tt = juros + totalVenda;

                    totalComissaoTV.setText("R$ " + somo + ",00");
                    total_venda_lista_revenda.setText("R$ " + tt + ",00");

                    if (cbDinheiro.isChecked()) {
                        totalComissaoTV.setText("R$ " + somo + ",00");
                        total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");
                    }


                } else {
                    totalComissaoTV.setText("R$ " + somo + ",00");
                    total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");
                }

            }
        });
    }

    private void esconderTeclado(TextInputEditText et) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    private void myShowDialog(int tipo) {

        int juros = (totalVenda * 10) / 100;
        int tt = juros + totalVenda;

        switch (tipo) {

            case 1:

                cbAlimentacao.setChecked(false);
                cbDebito.setChecked(true);
                cbCredito.setChecked(false);
                cbDinheiro.setChecked(false);

                totalComissaoTV.setText("R$ " + somo + ",00");
                total_venda_lista_revenda.setText("R$ " + tt + ",00");

                break;
            case 2:

                cbAlimentacao.setChecked(false);
                cbDebito.setChecked(false);
                cbCredito.setChecked(true);
                cbDinheiro.setChecked(false);

                totalComissaoTV.setText("R$ " + somo + ",00");
                total_venda_lista_revenda.setText("R$ " + tt + ",00");
                break;
            case 3:
                cbAlimentacao.setChecked(true);
                cbDebito.setChecked(false);
                cbCredito.setChecked(false);
                cbDinheiro.setChecked(false);

                totalComissaoTV.setText("R$ " + somo + ",00");
                total_venda_lista_revenda.setText("R$ " + tt + ",00");
                break;
            case 4:
                cbAlimentacao.setChecked(false);
                cbDebito.setChecked(false);
                cbCredito.setChecked(false);
                cbDinheiro.setChecked(true);

                totalComissaoTV.setText("R$ " + somo + ",00");
                total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");
                break;
            default:
                cbAlimentacao.setChecked(false);
                cbDebito.setChecked(false);
                cbCredito.setChecked(false);
                cbDinheiro.setChecked(false);
                totalComissaoTV.setText("R$ " + somo + ",00");
                total_venda_lista_revenda.setText("R$ " + totalVenda + ",00");
                break;

        }

        switch (tipo) {
            case 1:
                //debito
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(this, R.style.AppCompatAlertDialog)
                        .setTitle("Débito")
                        .setItems(cDebito, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cb == debito + cartao
                                cbDebito.setChecked(true);
                                cbAlimentacao.setChecked(false);
                                cbCredito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvAlimentacao.setText("");
                                tvCredito.setText("");
                                tvDebito.setText(cDebito[which]);
                                detalhePagamento = cDebito[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                //cbDebito.setChecked(false);
                break;
            case 2:
                //credito
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(this)
                        .setTitle("Crédito")
                        .setItems(cCredito, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cbCredito.setChecked(true);
                                cbAlimentacao.setChecked(false);
                                cbDebito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvAlimentacao.setText("");
                                tvDebito.setText("");
                                tvCredito.setText(cCredito[which]);
                                detalhePagamento = cCredito[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                //cbCredito.setChecked(false);
                break;
            case 3:
                //refeicao
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Alimentação")
                        .setItems(cAlimentacao, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cbAlimentacao.setChecked(true);
                                cbDebito.setChecked(false);
                                cbCredito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvDebito.setText("");
                                tvCredito.setText("");
                                tvAlimentacao.setText(cAlimentacao[which]);
                                detalhePagamento = cAlimentacao[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                //cbAlimentacao.setChecked(false);
                break;
            default:
                break;
        }
    }

    private ObjectRevenda getDadosCompra() {

        String enderecoCompacto = "";

        if (tipoDePagamento == 0) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira uma forma de pagamento", Toast.LENGTH_LONG);
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

        if (tipoDePagamento != 0 && tipoDePagamento != 4) {

            int juros = (totalVenda * 10) / 100;
            totalVenda = juros + totalVenda;

        }

        ObjectRevenda objectRevenda = new ObjectRevenda(ruaMain, somo, bairroMain, totalVenda, detalhePagamento, tipoDePagamento, tipoEntrega, 0, null, 0, objProdutoRevendas, 0, nomeCliente, false, auth.getCurrentUser().getPhotoUrl().getPath(), telefoneMain, 1, 0, auth.getUid(), auth.getCurrentUser().getDisplayName(), totalVenda, false, documentoPrincipalDoUsuario.isAdmConfirmado(), documentoPrincipalDoUsuario.getUidAdm());

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
                        .setMessage("Encerramos nossas entregas por hoje, se confirmar a compra você vai receber seu pedido amanhã pela manhã.\nConfirma compra assim mesmo ?\n\n" + msg)
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

        efab_concluir_revenda.setVisibility(View.GONE);
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

                Log.d("TesteCadastroAfiliados", "Produto: " + objRev.getListaDeProdutos().get(i).getProdutoName());

                String prodAt = objRev.getListaDeProdutos().get(i).getQuantidade() + " " + objRev.getListaDeProdutos().get(i).getProdutoName();

                if (i > 0) {
                    produtoAtual = produtoAtual + ", " + prodAt;
                } else {
                    produtoAtual = prodAt;
                }

            }

            String descricaoComissao = "De " + produtoAtual;

            Log.d("TesteCadastroAfiliados", "Descricao Comissao: " + descricaoComissao);

            ComissaoAfiliados comissaoAfiliados = new ComissaoAfiliados(idRevenda, 5, tituloComissao, descricaoComissao, System.currentTimeMillis(), documentoPrincipalDoUsuario.getUid(), documentoPrincipalDoUsuario.getNome(), documentoPrincipalDoUsuario.getPathFoto(), false, objRev.getStatusCompra());
            batch.set(minhaComissaoRef, comissaoAfiliados);
            batch.set(comissaoRef, comissaoAfiliados);

        }

        //colocar referencia a minhas revendas

        final ObjectRevenda novaCompra = new ObjectRevenda(objRev.getAdress(), objRev.getComissaoTotal(), objRev.getComplemento(), objRev.getCompraValor(), objRev.getDetalhePag(), objRev.getFormaDePagar(), objRev.getFrete(), System.currentTimeMillis(), revendaRef.getId(), objRev.getLat(), objRev.getListaDeProdutos(), objRev.getLng(), objRev.getNomeCliente(), objRev.isPagamentoRecebido(), objRev.getPathFotoUserRevenda(), objRev.getPhoneCliente(), objRev.getStatusCompra(), objRev.getTipoDeEntrega(), objRev.getUidUserRevendedor(), objRev.getUserNomeRevendedor(), objRev.getValorTotal(), objRev.isVendaConcluida(), possuiComissaoAfiliados, documentoPrincipalDoUsuario.getUidAdm());


        batch.set(revendaRef, novaCompra);
        batch.set(minhaRevendaRef, novaCompra);


        for (int i = 0; i < novaCompra.getListaDeProdutos().size(); i++) {
            batch.delete(listaRevenda.document(novaCompra.getListaDeProdutos().get(i).getIdProdut()));
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if (!ADMINISTRADOR) {

                    analitycsFacebook.logRevenda(user.getDisplayName(), user.getUid(), pathFotoUser);
                    analitycsGoogle.logRevenda(user.getDisplayName(), user.getUid(), pathFotoUser);
                    analitycsGoogle.logVenda(novaCompra);
                    analitycsFacebook.logVenda(novaCompra);

                }

                Intent intent = new Intent(ListaRevendaActivity.this, PainelRevendedorActivity.class);
                Toast.makeText(ListaRevendaActivity.this, "Parabens, Sua venda foi um sucesso.", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
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
}

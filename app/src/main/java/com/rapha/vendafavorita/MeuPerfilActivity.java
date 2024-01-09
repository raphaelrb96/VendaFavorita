package com.rapha.vendafavorita;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.SolicitacaoRevendedor;
import com.rapha.vendafavorita.objects.Usuario;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.MainActivity.getUidShareLink;

public class MeuPerfilActivity extends AppCompatActivity {

    private TextView status_solicitacao_meu_perfil, email_usuario_meu_perfil;
    private TextView bt_solicitacao_adm_meu_perfil;
    private TextView tv_nome_adm_meu_perfil, nome_usuario_meu_perfil;
    private CardView card_solicitacao_adm_meu_perfil;

    private FrameLayout container_adm_meu_perfil;
    private ImageView img_adm_meu_perfil, imagem_meu_perfil;

    private NestedScrollView scrol_meu_perfil;

    private ProgressBar pb_meu_perfil;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private LinearLayout bt_salvar_dados_meu_perfil, container_dados_adm_meu_perfil;

    private TextInputEditText et_username_usuario_meu_perfil, et_num_usuario_meu_perfil;

    private DocumentReference usuarioRef;

    private Usuario usuario;

    private boolean mModoEdicao = false;
    private View bt_voltar_meu_perfil;

    private LinearLayout ll_bt_sair, ll_bt_mensagem_menu;
    private AnalitycsGoogle analitycsGoogle;
    private String intentExtraAdm;
    private Usuario usuarioAdm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        status_solicitacao_meu_perfil = (TextView) findViewById(R.id.status_solicitacao_meu_perfil);
        bt_solicitacao_adm_meu_perfil = (TextView) findViewById(R.id.bt_solicitacao_adm_meu_perfil);
        card_solicitacao_adm_meu_perfil = (CardView) findViewById(R.id.card_solicitacao_adm_meu_perfil);

        nome_usuario_meu_perfil = (TextView) findViewById(R.id.nome_usuario_meu_perfil);
        email_usuario_meu_perfil = (TextView) findViewById(R.id.email_usuario_meu_perfil);

        container_adm_meu_perfil = (FrameLayout) findViewById(R.id.container_adm_meu_perfil);
        bt_salvar_dados_meu_perfil = (LinearLayout) findViewById(R.id.bt_salvar_dados_meu_perfil);
        container_dados_adm_meu_perfil = (LinearLayout) findViewById(R.id.container_dados_adm_meu_perfil);
        img_adm_meu_perfil = (ImageView) findViewById(R.id.img_adm_meu_perfil);
        imagem_meu_perfil = (ImageView) findViewById(R.id.imagem_meu_perfil);
        tv_nome_adm_meu_perfil = (TextView) findViewById(R.id.tv_nome_adm_meu_perfil);

        bt_voltar_meu_perfil = (View) findViewById(R.id.bt_voltar_meu_perfil);

        ll_bt_mensagem_menu = (LinearLayout) findViewById(R.id.ll_bt_mensagem_menu);
        ll_bt_sair = (LinearLayout) findViewById(R.id.ll_bt_sair);

        pb_meu_perfil = (ProgressBar) findViewById(R.id.pb_meu_perfil);
        scrol_meu_perfil = (NestedScrollView) findViewById(R.id.scrol_meu_perfil);

        et_username_usuario_meu_perfil = (TextInputEditText) findViewById(R.id.et_username_usuario_meu_perfil);
        et_num_usuario_meu_perfil = (TextInputEditText) findViewById(R.id.et_num_usuario_meu_perfil);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("adm");
            //The key argument here must match that used in the other activity
            if(value != null) {
                intentExtraAdm = value;
                Log.d("TesteLogin", "Perfil activity Extras: " + value);
            } else {
                //intent null
                intentExtraAdm = getUidShareLink();
                if(intentExtraAdm != null) {
                    Log.d("TesteLogin", "Perfil activity Extras: getUidShareLink()");
                } else {

                }
            }

        }


        user = auth.getCurrentUser();

        usuarioRef = firestore.collection("Usuario").document(user.getUid());

        analitycsGoogle = new AnalitycsGoogle(this);

        bt_voltar_meu_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ll_bt_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
            }
        });

        if (ADMINISTRADOR) {
            ll_bt_mensagem_menu.setVisibility(View.GONE);
        }

        ll_bt_mensagem_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ADMINISTRADOR) {
                    return;
                }



                startActivity(new Intent(MeuPerfilActivity.this, MensagemDetalheActivity.class));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            et_num_usuario_meu_perfil.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        esconderTeclado(et_num_usuario_meu_perfil);
                        et_num_usuario_meu_perfil.clearFocus();
                    }
                    return false;
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            et_username_usuario_meu_perfil.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        esconderTeclado(et_username_usuario_meu_perfil);
                        et_username_usuario_meu_perfil.clearFocus();
                    }
                    return false;
                }
            });
        }

        bt_salvar_dados_meu_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apelidoBruto = et_username_usuario_meu_perfil.getText().toString().toLowerCase();
                String numero = et_num_usuario_meu_perfil.getText().toString();

                if (apelidoBruto.length() < 1) {
                    Toast.makeText(MeuPerfilActivity.this, "Insira um apelido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (numero.length() < 1) {
                    Toast.makeText(MeuPerfilActivity.this, "Insira um número pra contato", Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuilder nick = new StringBuilder();

                for (int i = 0; i < apelidoBruto.length(); i++) {

                    String ch = String.valueOf(apelidoBruto.charAt(i));

                    if (ch.equals(" ")) {
                        ch = "_";
                    }

                    nick.append(ch);

                }

                Log.d("ApelidoFormatado", nick.toString());

                firestore.collection("Usuario").whereEqualTo("userName", nick.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if(result.isEmpty()) {
                            salvarDadosFirebase(nick.toString(), numero);
                        } else {
                            Toast.makeText(MeuPerfilActivity.this, "Esse apelido que você escolheu ja está sendo usado por outro vendedor !", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });

        et_num_usuario_meu_perfil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_num_usuario_meu_perfil.setFocusable(true);
                et_num_usuario_meu_perfil.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_username_usuario_meu_perfil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_username_usuario_meu_perfil.setFocusable(true);
                et_username_usuario_meu_perfil.setFocusableInTouchMode(true);
                return false;
            }
        });

    }

    private void salvarDadosFirebase(String nick, String numero) {

        WriteBatch batch = firestore.batch();



        SolicitacaoRevendedor obj = new SolicitacaoRevendedor(user.getEmail(), pathFotoUser, nick, numero, "", user.getDisplayName(), System.currentTimeMillis(), user.getUid(), 0);

        DocumentReference doc = firestore.collection("Revendedores").document("amacompras").collection("ativos").document(user.getUid());

        batch.update(usuarioRef, "userName", nick);
        batch.update(usuarioRef, "celular", numero);


        batch.set(doc, obj);

        if (!ADMINISTRADOR) {

            analitycsGoogle.cadastroDeUsuario(nick, user.getUid(), pathFotoUser);

        }

        if(usuarioAdm != null) {
            batch.update(usuarioRef,
                    "admConfirmado", true,
                    "uidAdm", usuarioAdm.getUid(),
                    "pathFotoAdm", usuarioAdm.getPathFoto(),
                    "nomeAdm", usuarioAdm.getNome(),
                    "usernameAdm", usuarioAdm.getUserName()
            );
        }

        batch.commit().addOnSuccessListener(MeuPerfilActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                documentoPrincipalDoUsuario = usuario;


                usuarioInterface(true, true, mModoEdicao);
                Toast.makeText(MeuPerfilActivity.this, "Dados atualizados com sucesso", Toast.LENGTH_LONG).show();
                if (getIntent().getIntExtra("alert", 1) == 2) {
                    Intent intent = new Intent(MeuPerfilActivity.this, PainelRevendedorActivity.class);
                    intent.putExtra("alert", 2);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        }).addOnFailureListener(MeuPerfilActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pb_meu_perfil.setVisibility(View.GONE);
                scrol_meu_perfil.setVisibility(View.VISIBLE);
                bt_salvar_dados_meu_perfil.setVisibility(View.VISIBLE);
                Toast.makeText(MeuPerfilActivity.this, "Erro ao salvar", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void esconderTeclado(TextInputEditText et) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        pb_meu_perfil.setVisibility(View.VISIBLE);
        scrol_meu_perfil.setVisibility(View.GONE);

        usuarioRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                pb_meu_perfil.setVisibility(View.GONE);
                scrol_meu_perfil.setVisibility(View.VISIBLE);

                if (documentSnapshot != null) {
                    usuario = documentSnapshot.toObject(Usuario.class);

                    if (usuario != null) {
                        if (usuario.getUidAdm() == null) {

                            Log.d("TesteCadastroAfiliados", "Usuario sem adm");

                            // usuario sem adm e sem solicitacao
                            usuarioInterface(false, false, mModoEdicao);

                        } else {

                            if (usuario.getUidAdm().length() < 1) {
                                // usuario sem adm e sem solicitacão
                                usuarioInterface(false, false, mModoEdicao);
                                Log.d("TesteCadastroAfiliados", "Usuario sem adm");
                            } else {

                                if (!usuario.isAdmConfirmado()) {
                                    //usuario sem adm e com solicitacao
                                    usuarioInterface(true, false, mModoEdicao);
                                    Log.d("TesteCadastroAfiliados", "Usuario possui uma solicitacao");
                                } else {
                                    // usuario com adm autenticado
                                    usuarioInterface(true, true, mModoEdicao);
                                    Log.d("TesteCadastroAfiliados", "Usuario possui uma adm autenticado");
                                }

                            }

                        }
                    } else {


                        finish();

                    }

                }
            }
        });

    }

    private void usuarioInterface(boolean possuiAdm, boolean admVerificado, boolean modoEdicao) {

        nome_usuario_meu_perfil.setText(usuario.getNome());
        Glide.with(this).load(usuario.getPathFoto()).into(imagem_meu_perfil);
        email_usuario_meu_perfil.setText(usuario.getEmail());


        if (usuario.getUserName() != null && usuario.getUserName().length() > 0) {
            et_username_usuario_meu_perfil.setEnabled(false);
            et_username_usuario_meu_perfil.setFocusable(false);
            et_username_usuario_meu_perfil.setFocusableInTouchMode(false);

        }

        if (usuario.getCelular() != null && usuario.getCelular().length() > 0) {
            et_num_usuario_meu_perfil.setEnabled(false);
            et_num_usuario_meu_perfil.setFocusable(false);
        }

        et_username_usuario_meu_perfil.setEnabled(!modoEdicao);
        et_num_usuario_meu_perfil.setEnabled(!modoEdicao);

        if (possuiAdm && admVerificado) {
            //adm confirmado
            card_solicitacao_adm_meu_perfil.setVisibility(View.GONE);
            container_dados_adm_meu_perfil.setVisibility(View.VISIBLE);
            Glide.with(this).load(usuario.getPathFotoAdm()).into(img_adm_meu_perfil);
            tv_nome_adm_meu_perfil.setText("@" + usuario.getUsernameAdm());

        } else if (possuiAdm && !admVerificado) {
            //falta confirmar adm
            showCardConfirmacao(usuario.getNomeAdm(), usuario.getUsernameAdm(), null, null, true);

        } else {
            //nem tem adm
            if(intentExtraAdm != null && intentExtraAdm != "") {
                firestore.collection("Usuario").whereEqualTo("userName", intentExtraAdm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if(!result.isEmpty() && result.size() > 0) {
                            usuarioAdm = result.getDocuments().get(0).toObject(Usuario.class);
                            showCardConfirmacao(usuarioAdm.getNome(), usuarioAdm.getUserName(), usuarioAdm.getPathFoto(), usuarioAdm.getUid(), false);
                        }
                    }
                });
            } else {
                card_solicitacao_adm_meu_perfil.setVisibility(View.GONE);
                container_adm_meu_perfil.setVisibility(View.GONE);
            }

        }


        if (usuario.getUserName() == null) {

        } else {

            et_username_usuario_meu_perfil.setText(usuario.getUserName());


        }

        if (usuario.getCelular() == null) {

        } else {

            et_num_usuario_meu_perfil.setText(usuario.getCelular());


        }

        if (usuario.getCelular() == null || usuario.getUserName() == null) {
            bt_salvar_dados_meu_perfil.setVisibility(View.VISIBLE);

            if (getIntent().getIntExtra("alert", 1) == 2) {
                Toast.makeText(MeuPerfilActivity.this, "IMPORTANTE: Complete seu cadastro para poder revender", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MeuPerfilActivity.this, "IMPORTANTE: Preencha os campos acima", Toast.LENGTH_SHORT).show();
            }

        } else {


            if (usuario.getUserName().length() == 0 || usuario.getCelular().length() == 0) {

                bt_salvar_dados_meu_perfil.setVisibility(View.VISIBLE);

                if (getIntent().getIntExtra("alert", 1) == 2) {
                    Toast.makeText(MeuPerfilActivity.this, "IMPORTANTE: Complete seu cadastro para poder revender", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeuPerfilActivity.this, "IMPORTANTE: Preencha os campos acima", Toast.LENGTH_SHORT).show();
                }

            } else {
                bt_salvar_dados_meu_perfil.setVisibility(View.GONE);
            }
        }

    }

    private void showCardConfirmacao(String nomeAdm, String apelido, String photo, String uidAdm, boolean admCadastrado) {
        card_solicitacao_adm_meu_perfil.setVisibility(View.VISIBLE);
        container_adm_meu_perfil.setVisibility(View.VISIBLE);
        container_dados_adm_meu_perfil.setVisibility(View.GONE);
        status_solicitacao_meu_perfil.setText(nomeAdm + " deseja se tornar seu adm. Você aceita entrar para a equipe junto com @" + apelido + " ?");

        bt_solicitacao_adm_meu_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!admCadastrado) {
                    clickConfirmarAdm(
                            usuarioRef.update(
                                    "admConfirmado", true,
                                    "uidAdm", uidAdm,
                                    "pathFotoAdm", photo,
                                    "nomeAdm", nomeAdm,
                                    "usernameAdm", apelido
                            ), admCadastrado, nomeAdm, apelido, photo, uidAdm);
                } else {
                    clickConfirmarAdm(usuarioRef.update("admConfirmado", true), admCadastrado, nomeAdm, apelido, photo, uidAdm);
                }
            }
        });
    }

    private void clickConfirmarAdm(Task<Void> query, boolean admCadastrado, String nomeAdm, String apelido, String photo, String uidAdm) {
        query.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                usuarioInterface(true, true, false);

                if(admCadastrado) {
                    usuario = new Usuario(documentoPrincipalDoUsuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm(), usuario.getEndereco(), usuario.getUserName(), usuario.getUidAdm(), usuario.getUsernameAdm(), usuario.getNomeAdm(), usuario.getPathFotoAdm(), true);
                    documentoPrincipalDoUsuario = usuario;
                } else {
                    usuario = new Usuario(documentoPrincipalDoUsuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm(), usuario.getEndereco(), usuario.getUserName(), uidAdm, apelido, nomeAdm, photo, true);
                    documentoPrincipalDoUsuario = usuario;
                }


                if (!ADMINISTRADOR) {

                    analitycsGoogle.afiliacao(documentoPrincipalDoUsuario.getUsernameAdm(), documentoPrincipalDoUsuario.getUidAdm(), documentoPrincipalDoUsuario.getPathFotoAdm());

                }


                if (getIntent().getIntExtra("alert", 1) == 2) {
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("TesteCadastroAfiliados", "Erro ao salvar");

            }
        });

    }
}
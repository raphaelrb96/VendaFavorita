package com.rapha.vendafavorita;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterAfilidos;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.Usuario;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;

public class PainelDeAfiliados extends AppCompatActivity {

    private TextView tv_toolbar_painel_afiliados, tv_info_card_afiliados, bt_add_revendedor;
    private View bt_voltar_painel_afiliados;

    private ProgressBar pb_add_revendedor_afiliado, pb_meus_afiliados;
    private ImageView info_add_revendedor_afiliado;

    private Button bt_link_site_recrutamento, bt_link_app_recrutamento;

    private String nickUser = null;

    private TextInputEditText et_username_add_afiliado;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private TextView numero_de_analize, total_afiliados_autenticados, titulo_meus_revendedores;
    private Query colecaoMeusAfiliados;
    private RecyclerView rv_painel_afiliados;

    private boolean cadastroIniciado = false;
    private AnalitycsGoogle analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_de_afiliados);
        tv_toolbar_painel_afiliados = (TextView) findViewById(R.id.tv_toolbar_painel_afiliados);
        numero_de_analize = (TextView) findViewById(R.id.numero_de_analize);
        titulo_meus_revendedores = (TextView) findViewById(R.id.titulo_meus_revendedores);

        bt_link_site_recrutamento = (Button) findViewById(R.id.bt_link_site_recrutamento);
        bt_link_app_recrutamento = (Button) findViewById(R.id.bt_link_app_recrutamento);

        bt_voltar_painel_afiliados = (View) findViewById(R.id.bt_voltar_painel_afiliados);

        rv_painel_afiliados = (RecyclerView) findViewById(R.id.rv_painel_afiliados);

        bt_add_revendedor = (TextView) findViewById(R.id.bt_add_revendedor);
        total_afiliados_autenticados = (TextView) findViewById(R.id.total_afiliados_autenticados);

        et_username_add_afiliado = (TextInputEditText) findViewById(R.id.et_username_add_afiliado);

        pb_add_revendedor_afiliado = (ProgressBar) findViewById(R.id.pb_add_revendedor_afiliado);
        pb_meus_afiliados = (ProgressBar) findViewById(R.id.pb_meus_afiliados);
        info_add_revendedor_afiliado = (ImageView) findViewById(R.id.info_add_revendedor_afiliado);
        tv_info_card_afiliados = (TextView) findViewById(R.id.tv_info_card_afiliados);

        pb_meus_afiliados.setVisibility(View.VISIBLE);
        rv_painel_afiliados.setVisibility(View.GONE);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        verificarUser();

        analitycsGoogle = new AnalitycsGoogle(this);


        bt_link_app_recrutamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharLinkApp();
            }
        });

        bt_link_site_recrutamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharLinkSite();
            }
        });

        bt_voltar_painel_afiliados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void compartilharLinkApp() {
        if(nickUser == null) return;
        String prefixoUrl = "https://vendafavorita.web.app/cadastro/?adm="+nickUser;

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(prefixoUrl))
                .setDomainUriPrefix("https://vendafavorita.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
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
                            i.putExtra(Intent.EXTRA_SUBJECT, "Link de Recrutamento");
                            i.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            startActivity(Intent.createChooser(i, "Compartilhar Link"));
                        } else {

                        }
                    }
                });
    }

    private void compartilharLinkSite() {
        if(nickUser == null) return;
        String prefixoUrl = "https://vendafavorita.web.app/cadastro/?adm="+nickUser;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Link de Recrutamento");
        i.putExtra(Intent.EXTRA_TEXT, prefixoUrl);
        startActivity(Intent.createChooser(i, "Compartilhar Link"));
    }

    private void verMeusAfiliados() {
        colecaoMeusAfiliados = mFirestore.collection("Usuario").whereEqualTo("uidAdm", mAuth.getCurrentUser().getUid()).limit(400);



        colecaoMeusAfiliados.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                pb_meus_afiliados.setVisibility(View.GONE);
                rv_painel_afiliados.setVisibility(View.VISIBLE);

                if (queryDocumentSnapshots != null) {

                    ArrayList<Usuario> meusAfiliados = new ArrayList<>();

                    int numAutenticados = 0;
                    int numEmAnalise = 0;

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                        Usuario umAfiliado = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                        if (umAfiliado.isAdmConfirmado()) {
                            numAutenticados++;
                        } else {
                            numEmAnalise++;
                        }
                        meusAfiliados.add(umAfiliado);
                    }

                    total_afiliados_autenticados.setText(String.valueOf(numAutenticados));
                    numero_de_analize.setText(String.valueOf(numEmAnalise));

                    if (meusAfiliados.size() > 0) {
                        titulo_meus_revendedores.setVisibility(View.VISIBLE);
                    } else {
                        titulo_meus_revendedores.setVisibility(View.GONE);
                    }

                    AdapterAfilidos adapterAfilidos = new AdapterAfilidos(meusAfiliados,PainelDeAfiliados.this);
                    rv_painel_afiliados.setLayoutManager(new LinearLayoutManager(PainelDeAfiliados.this));
                    rv_painel_afiliados.setAdapter(adapterAfilidos);

                } else {
                    //nenhum afiliado ainda
                }
            }
        });


        bt_add_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cadastroIniciado) {
                    return;
                }

                String apelido = et_username_add_afiliado.getText().toString().toLowerCase();

                et_username_add_afiliado.setText(apelido);

                cadastroIniciado = true;


                if (apelido.length() > 0) {

                    if (documentoPrincipalDoUsuario == null) {
                        verificarUsuario(apelido);
                        return;
                    } else {

                        if (documentoPrincipalDoUsuario.getUid() == null || documentoPrincipalDoUsuario.getUid().length() == 0) {
                            verificarUsuario(apelido);
                            return;
                        }
                    }

                    verificarApelido(apelido);
                } else {
                    tv_info_card_afiliados.setText("Insira um apelido válido!");
                }
            }
        });

        infoPadraoRevendedor();

    }

    private void verificarUser() {


        DocumentReference usuarioRef = mFirestore.collection("Usuario").document(mAuth.getUid());
        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {


                if (userDoc.exists()) {

                    documentoPrincipalDoUsuario = userDoc.toObject(Usuario.class);

                    Log.d("TesteCadastroAfiliados", "VerificarUsuario() chamado, Uid Adm: " + documentoPrincipalDoUsuario.getUidAdm());

                    if (documentoPrincipalDoUsuario != null) {

                        if (documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {

                            Intent intent = new Intent(PainelDeAfiliados.this, MeuPerfilActivity.class);
                            startActivity(intent);

                        } else {
                            nickUser = documentoPrincipalDoUsuario.getUserName();
                            //tv_toolbar_painel_afiliados.setText("@" + nickUser);
                            verMeusAfiliados();
                        }

                    }

                } else {



                    Intent intent = new Intent(PainelDeAfiliados.this, MeuPerfilActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!ADMINISTRADOR) {

            analitycsGoogle.visitaAoPainelAfiliados(user.getDisplayName(), user.getUid(), pathFotoUser);

        }

    }

    private void verificarUsuario(final String apelido) {
        DocumentReference usuarioRef = mFirestore.collection("Usuario").document(mAuth.getUid());

        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {


                if (userDoc.exists()) {

                    Usuario usuarioObj = userDoc.toObject(Usuario.class);

                    documentoPrincipalDoUsuario = usuarioObj;

                    nickUser = documentoPrincipalDoUsuario.getUserName();

                    if (apelido != null) {
                        verificarApelido(apelido);
                    }

                } else {

                    cadastroIniciado = false;

                    tv_info_card_afiliados.setText("Nenhum usuario encontrado");
                    info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                    pb_add_revendedor_afiliado.setVisibility(View.GONE);

                    et_username_add_afiliado.setText("");

                }

            }
        });
    }

    private void cadastrar() {
        Intent intent = new Intent(PainelDeAfiliados.this, MeuPerfilActivity.class);
        intent.putExtra("alert", 2);
        startActivity(intent);
        finish();
    }

    private void verificarApelido(String apelido) {
        info_add_revendedor_afiliado.setVisibility(View.GONE);
        pb_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        addRevendedorLoading();

        mFirestore.collection("Usuario").whereEqualTo("userName", apelido).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        tv_info_card_afiliados.setText("Registrando afiliação");

                        Usuario usuario = queryDocumentSnapshots.getDocuments().get(0).toObject(Usuario.class);


                        if (usuario.getUid().equals(documentoPrincipalDoUsuario.getUid())) {
                            tv_info_card_afiliados.setText("Seu proprio apelido não é válido!");
                            info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                            pb_add_revendedor_afiliado.setVisibility(View.GONE);
                            cadastroIniciado = false;
                        } else {

                            if (usuario.isAdmConfirmado()) {
                                tv_info_card_afiliados.setText("Esse usuario ja possui um ADM");
                                info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                                pb_add_revendedor_afiliado.setVisibility(View.GONE);
                                cadastroIniciado = false;
                                return;
                            }

                            Usuario atualizado = new Usuario(usuario.isVipDiamante(), usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm(), usuario.getEndereco(), usuario.getUserName(), mAuth.getUid(), nickUser, mAuth.getCurrentUser().getDisplayName(), pathFotoUser, false);

                            Log.d("TesteCadastroAfiliados", "Uid Adm: " + atualizado.getUidAdm());
                            Log.d("TesteCadastroAfiliados", "Apelido Adm: " + atualizado.getUsernameAdm());

                            queryDocumentSnapshots.getDocuments().get(0).getReference().set(atualizado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    revendedorAdicionado();
                                    et_username_add_afiliado.setText("");
                                    cadastroIniciado = false;
                                }
                            });

                        }

                    } else {

                        tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                        pb_add_revendedor_afiliado.setVisibility(View.GONE);
                        cadastroIniciado = false;

                    }

                } else {
                    //apelido nao encontrado
                    tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                    info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                    pb_add_revendedor_afiliado.setVisibility(View.GONE);
                    cadastroIniciado = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                pb_add_revendedor_afiliado.setVisibility(View.GONE);
                cadastroIniciado = false;
            }
        });
    }

    private void addRevendedorLoading () {
        tv_info_card_afiliados.setText("Aguarde, estamos validando sua solicitação");
        info_add_revendedor_afiliado.setVisibility(View.GONE);
        pb_add_revendedor_afiliado.setVisibility(View.VISIBLE);
    }

    private void revendedorAdicionado () {
        tv_info_card_afiliados.setText("Solicitação enviada com sucesso, aguarde a confirmação do revendedor");
        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        pb_add_revendedor_afiliado.setVisibility(View.GONE);
        et_username_add_afiliado.setText("");
    }

    private void infoPadraoRevendedor () {
        tv_info_card_afiliados.setText("Insira o apelido do revendedor que você recrutou");
        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        pb_add_revendedor_afiliado.setVisibility(View.GONE);
    }

}
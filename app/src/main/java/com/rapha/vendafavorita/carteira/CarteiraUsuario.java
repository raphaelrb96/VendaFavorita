package com.rapha.vendafavorita.carteira;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class CarteiraUsuario extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private Query refRevenda, refComissoes;

    private AnalitycsGoogle analitycsGoogle;

    private ArrayList<ObjectRevenda> listaDeComissoes;
    private ArrayList<ComissaoAfiliados> listaDeComissaoAfiliados;

    private int total = 0;

    private int lucro30 = 0;
    private int lucro7 = 0;
    private int lucro1 = 0;

    private TextView data_comissoes_afiliados, total_comissoes_afiliados;
    private TextView data_comissoes_vendas, total_comissoes_vendas;
    private TextView total_a_receber_carteira, data_comissoes_gerais;
    private TextView total_do_dia, total_da_semana, total_do_mes;

    private ProgressBar pb_carteira;
    private LinearLayout container_carteira;

    private Calendar c30, c7, c1;

    private long x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carteira_usuario);

        data_comissoes_afiliados = (TextView) findViewById(R.id.data_comissoes_afiliados);
        total_comissoes_afiliados = (TextView) findViewById(R.id.total_comissoes_afiliados);

        data_comissoes_vendas = (TextView) findViewById(R.id.data_comissoes_vendas);
        total_comissoes_vendas = (TextView) findViewById(R.id.total_comissoes_vendas);

        total_a_receber_carteira = (TextView) findViewById(R.id.total_a_receber_carteira);
        data_comissoes_gerais = (TextView) findViewById(R.id.data_comissoes_gerais);

        total_do_dia = (TextView) findViewById(R.id.total_do_dia);
        total_da_semana = (TextView) findViewById(R.id.total_da_semana);
        total_do_mes = (TextView) findViewById(R.id.total_do_mes);

        pb_carteira = (ProgressBar) findViewById(R.id.pb_carteira);

        container_carteira = (LinearLayout) findViewById(R.id.container_carteira);

        firebaseFirestore = FirebaseFirestore.getInstance();

        analitycsGoogle = new AnalitycsGoogle(this);
        auth = FirebaseAuth.getInstance();

        c30 = new GregorianCalendar();
        c7 = new GregorianCalendar();
        c1 = new GregorianCalendar();
        c30.add(Calendar.DAY_OF_MONTH, -30);
        c7.add(Calendar.DAY_OF_MONTH, -7);
        c1.add(Calendar.HOUR_OF_DAY, -24);

        String uid = auth.getUid();

        refRevenda = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(uid);

        refComissoes = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(uid);


        refRevenda.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listaDeComissoes = new ArrayList<>();
                total = 0;
                if (queryDocumentSnapshots != null) {

                    if (queryDocumentSnapshots.getDocuments().size() > 0) {

                        ArrayList<ObjectRevenda> vendasPendentes = new ArrayList<>();
                        int totalVendasPendente = 0;

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                            ObjectRevenda obj = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);

                            if (obj.getStatusCompra() == 5) {
                                //listaDeComissoes.add(obj);
                                if (!obj.isPagamentoRecebido()) {
                                    total = total + obj.getComissaoTotal();
                                    totalVendasPendente = totalVendasPendente + obj.getComissaoTotal();
                                    vendasPendentes.add(obj);
                                }


                                if (obj.getHora() > c30.getTimeInMillis()) {

                                    lucro30 = lucro30 + obj.getComissaoTotal();

                                }

                                if (obj.getHora() > c7.getTimeInMillis()) {
                                    lucro7 = lucro7 + obj.getComissaoTotal();
                                }

                                if (obj.getHora() > c1.getTimeInMillis()) {
                                    lucro1 = lucro1 + obj.getComissaoTotal();
                                }

                            }

                            listaDeComissoes.add(obj);



                        }

                        if (vendasPendentes.size() > 0) {

                            String data = DateFormatacao.getDiaString(new Date(vendasPendentes.get(vendasPendentes.size() - 1).getHora()));
                            String v = "R$ " + totalVendasPendente + ",00";

                            total_comissoes_vendas.setText(v);
                            data_comissoes_vendas.setText("Desde " + data);

                            Collections.sort(vendasPendentes, new Comparator<ObjectRevenda>() {
                                @Override
                                public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                                    return Long.compare(objectRevenda.getHora(), t1.getHora());
                                }
                            });

                            Collections.reverse(vendasPendentes);

                            x = vendasPendentes.get(vendasPendentes.size() - 1).getHora();

                            String dtx = DateFormatacao.getDiaString(new Date(x));

                            data_comissoes_gerais.setText("Desde " + dtx);

                        } else {
                            data_comissoes_gerais.setText("Nenhum");
                            data_comissoes_vendas.setText("Nenhum");
                        }

                        getListComissoes();


                    } else {
                        data_comissoes_gerais.setText("Nenhum");
                        data_comissoes_vendas.setText("Nenhum");
                        getListComissoes();
                    }
                } else {
                    getListComissoes();
                    data_comissoes_gerais.setText("Nenhum");
                    data_comissoes_vendas.setText("Nenhum");
                }
            }
        });

    }


    private void getListComissoes() {

        refComissoes.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                listaDeComissaoAfiliados = new ArrayList<>();

                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {

                    int totalComissoesAfiliados = 0;

                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                        ComissaoAfiliados comissao = queryDocumentSnapshots.getDocuments().get(i).toObject(ComissaoAfiliados.class);

                        int comiss = comissao.getComissao();

                        if (comissao.getStatusComissao() == 5) {
                            //listaDeComissoes.add(obj);
                            if (!comissao.isPagamentoRecebido()) {
                                total = total + comiss;
                                totalComissoesAfiliados = totalComissoesAfiliados + comiss;

                                listaDeComissaoAfiliados.add(comissao);
                            }


                            if (comissao.getHora() > c30.getTimeInMillis()) {

                                lucro30 = lucro30 + comissao.getComissao();

                            }

                            if (comissao.getHora() > c7.getTimeInMillis()) {
                                lucro7 = lucro7 + comissao.getComissao();
                            }

                            if (comissao.getHora() > c1.getTimeInMillis()) {
                                lucro1 = lucro1 + comissao.getComissao();
                            }

                        }

                    }

                    //card_carteira_historico_comissoes.setVisibility(View.VISIBLE);
                    total_a_receber_carteira.setText("R$" + formatarNumeroEmReais(total) + ",00");

                    if (listaDeComissaoAfiliados.size() > 0) {

                        comComissoesAfiliados();
                    } else {
                        semComissoesAfiliados();
                    }

                } else {

                    semComissoesAfiliados();
                    total_a_receber_carteira.setText("R$" + formatarNumeroEmReais(total) + ",00");

                    if (x == 0) {

                        data_comissoes_gerais.setText("Nenhum");
                    }
                }

                total_do_dia.setText("R$ " + lucro1 + ",00");
                total_da_semana.setText("R$ " + lucro7 + ",00");



                total_do_mes.setText("R$ " + formatarNumeroEmReais(lucro30) + ",00");

                pb_carteira.setVisibility(View.GONE);
                container_carteira.setVisibility(View.VISIBLE);

            }
        });

    }

    private String formatarNumeroEmReais(int totalrs) {
        String totalFormat = String.valueOf(totalrs);

        if (String.valueOf(totalrs).length() == 4) {
            totalFormat = String.valueOf(totalrs).charAt(0) + "." + String.valueOf(totalrs).substring(1);
        } else if (String.valueOf(totalrs).length() == 5) {
            totalFormat = String.valueOf(totalrs).substring(0,2) + "." + String.valueOf(totalrs).substring(2);
        } else if (String.valueOf(totalrs).length() == 6) {
            totalFormat = String.valueOf(totalrs).substring(0,3) + "." + String.valueOf(totalrs).substring(3);
        } else if (String.valueOf(totalrs).length() == 7) {
            totalFormat = String.valueOf(totalrs).charAt(0) + "." + String.valueOf(totalrs).substring(1,4) + "." + String.valueOf(totalrs).substring(4);
        }

        return totalFormat;
    }

    private void semComissoesAfiliados() {
        data_comissoes_afiliados.setText("Nenhum");
        total_comissoes_afiliados.setText("R$ 0,00");
    }

    private void comComissoesAfiliados() {

        String dataDaPrimeiraRevendaPendente = "";
        int valorRevendaPendente = 0;

        for (int i = 0; i < listaDeComissaoAfiliados.size(); i++) {

            ComissaoAfiliados comissaoAfiliados = listaDeComissaoAfiliados.get(i);
            valorRevendaPendente = valorRevendaPendente + comissaoAfiliados.getComissao();

        }

        Collections.sort(listaDeComissaoAfiliados, new Comparator<ComissaoAfiliados>() {
            @Override
            public int compare(ComissaoAfiliados comissaoAfiliados, ComissaoAfiliados t1) {
                return Long.compare(comissaoAfiliados.getHora(), t1.getHora());
            }
        });

        Collections.reverse(listaDeComissaoAfiliados);

        long time = listaDeComissaoAfiliados.get(listaDeComissaoAfiliados.size() - 1).getHora();

        if (time < x) {
            x = time;
            String dtx = DateFormatacao.getDiaString(new Date(x));

            data_comissoes_gerais.setText("Desde " + dtx);
        }

        dataDaPrimeiraRevendaPendente = DateFormatacao.getDiaString(new Date(time));

        String valor = "R$ " + formatarNumeroEmReais(valorRevendaPendente) + ",00";

        data_comissoes_afiliados.setText("Desde " + dataDaPrimeiraRevendaPendente);
        total_comissoes_afiliados.setText(valor);

    }


}
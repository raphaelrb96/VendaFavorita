package com.rapha.vendafavorita.informacoes.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.ClienteDetalhesActivity;
import com.rapha.vendafavorita.ComissoesActivity;
import com.rapha.vendafavorita.InventarioActivity;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.adapter.AdapterTopProdutos;
import com.rapha.vendafavorita.adapter.AdapterTopRevendedores;
import com.rapha.vendafavorita.objectfeed.ProdutoObj;
import com.rapha.vendafavorita.objectfeed.RevendedorObj;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.TopRevendedores;
import com.rapha.vendafavorita.objects.TopProdutosParcelable;
import com.rapha.vendafavorita.objects.TopProdutosRevenda;
import com.rapha.vendafavorita.objects.TopRevendedoresParcelable;
import com.rapha.vendafavorita.vendedor.VendedorActivity;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements AdapterTopRevendedores.TopRevendedoresListener {

    private static final String ARG_TOP_PRODUTOS = "prod";
    private static final String ARG_TOP_VENDEDORES = "vend";
    private static final String ARG_TOTAL_RS = "total";
    private static final String ARG_NUM_VENDAS = "numvendas";
    private static final String ARG_COMISSOES = "comissoes";
    private static final String ARG_CANCELAMENTOS = "cancelamentos";
    private static final String ARG_ITENS = "itens";
    private static final String ARG_VENDEDORES = "vendedores";
    private static final String ARG_TICKET_MEDIO = "ticket";
    private static final String ARG_MEDIA_VENDEDOR = "media";

    private Toast mToats;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas;
    private ArrayList<TopRevendedores> topRevendedores;
    private ArrayList<ObjectRevenda> revendas;

    private TextView tv_total_analise, tv_num_vendas_analise, tv_num_vendedores_analise, tv_media_vendedor, tv_num_produtos_analise, tv_comissoes_analise, tv_ticket_analise, tv_canceladas_analise;
    private RecyclerView rvTopRevendedores;
    private RecyclerView rvTopProd;
    private AdapterTopProdutos adapterTopProdutos;
    private AdapterTopRevendedores adapterTopRevendedores;
    private ExtendedFloatingActionButton efabAtualizarHome;

    private FirebaseFirestore firestore;

    public static PlaceholderFragment newInstance(ArrayList<TopProdutosRevenda> topProdutosRevendas30, ArrayList<TopRevendedores> topRevendedores, ArrayList<ObjectRevenda> revendas) {
        PlaceholderFragment fragment = new PlaceholderFragment();

        ArrayList<TopProdutosParcelable> topProdutosParcelables = new ArrayList<>();
        ArrayList<TopRevendedoresParcelable> revendedoresParcelables = new ArrayList<>();

        for (int i = 0; i < topProdutosRevendas30.size(); i++) {
            topProdutosParcelables.add(topProdutosRevendas30.get(i).getParcelable());
        }

        for (int i = 0; i < topRevendedores.size(); i++) {
            revendedoresParcelables.add(topRevendedores.get(i).getOfc());
        }

        int totalrs = 0;
        int numVendas = 0;
        int comissoes = 0;
        int cancelamentos = 0;


        for (int i = 0; i < revendas.size(); i++) {

            ObjectRevenda rev = revendas.get(i);

            if (rev.getStatusCompra() != 3) {
                totalrs = totalrs + rev.getValorTotal();
                numVendas = numVendas + rev.getListaDeProdutos().size();
                comissoes = comissoes + rev.getComissaoTotal();
            }

            if (rev.getStatusCompra() == 3) {
                cancelamentos++;
            }

        }

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

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(ARG_TOP_PRODUTOS, topProdutosParcelables);
        bundle.putParcelableArrayList(ARG_TOP_VENDEDORES, revendedoresParcelables);

        bundle.putString(ARG_TOTAL_RS, "R$ " + totalFormat + ",00");
        bundle.putString(ARG_NUM_VENDAS, numVendas + "");
        bundle.putString(ARG_VENDEDORES, revendedoresParcelables.size() + "");
        bundle.putString(ARG_ITENS, topProdutosParcelables.size() + "");

        bundle.putString(ARG_COMISSOES, comissoes + "");
        bundle.putString(ARG_CANCELAMENTOS, cancelamentos + "");

        int media = 0;
        int ticket = 0;

        if (numVendas > 0) {
            ticket = (int) totalrs/numVendas;
        }

        if (revendedoresParcelables.size() > 0) {
            media = (int) totalrs/revendedoresParcelables.size();
        }



        bundle.putString(ARG_TICKET_MEDIO, "R$ " + ticket + ",00");
        bundle.putString(ARG_MEDIA_VENDEDOR, "R$ " + media + ",00");

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_analisar_dados, container, false);

        tv_total_analise = (TextView) root.findViewById(R.id.tv_total_analise);
        tv_num_vendas_analise = (TextView) root.findViewById(R.id.tv_num_vendas_analise);
        tv_num_vendedores_analise = (TextView) root.findViewById(R.id.tv_num_vendedores_analise);
        tv_num_produtos_analise = (TextView) root.findViewById(R.id.tv_num_produtos_analise);
        tv_comissoes_analise = (TextView) root.findViewById(R.id.tv_comissoes_analise);
        tv_canceladas_analise = (TextView) root.findViewById(R.id.tv_canceladas_analise);
        tv_ticket_analise = (TextView) root.findViewById(R.id.tv_ticket_analise);
        tv_media_vendedor = (TextView) root.findViewById(R.id.tv_media_vendedor);
        rvTopRevendedores = (RecyclerView) root.findViewById(R.id.rv_top_revendedores);
        rvTopProd = (RecyclerView) root.findViewById(R.id.rv_top_produtos);
        efabAtualizarHome = (ExtendedFloatingActionButton) root.findViewById(R.id.efab_analytics_atualizar_home);

        firestore = FirebaseFirestore.getInstance();

        ArrayList<TopProdutosParcelable> topProdutosParcelables = getArguments().getParcelableArrayList(ARG_TOP_PRODUTOS);
        ArrayList<TopRevendedoresParcelable> topRevendedoresParcelables = getArguments().getParcelableArrayList(ARG_TOP_VENDEDORES);

        topProdutosRevendas = new ArrayList<>();
        topRevendedores = new ArrayList<>();

        for (int i = 0; i < topProdutosParcelables.size(); i++) {
            topProdutosRevendas.add(topProdutosParcelables.get(i).getProdOfc());
        }

        for (int i = 0; i < topRevendedoresParcelables.size(); i++) {
            topRevendedores.add(topRevendedoresParcelables.get(i).getOfc());
        }


        tv_canceladas_analise.setText(getArguments().getString(ARG_CANCELAMENTOS));
        tv_total_analise.setText(getArguments().getString(ARG_TOTAL_RS));
        tv_num_vendas_analise.setText(getArguments().getString(ARG_NUM_VENDAS));
        tv_num_vendedores_analise.setText(getArguments().getString(ARG_VENDEDORES));
        tv_num_produtos_analise.setText(getArguments().getString(ARG_ITENS));
        tv_comissoes_analise.setText(getArguments().getString(ARG_COMISSOES));
        tv_ticket_analise.setText(getArguments().getString(ARG_TICKET_MEDIO));
        tv_media_vendedor.setText(getArguments().getString(ARG_MEDIA_VENDEDOR));

        rvTopProd.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rvTopRevendedores.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        adapterTopProdutos = new AdapterTopProdutos(topProdutosRevendas, getActivity());
        adapterTopRevendedores = new AdapterTopRevendedores(getActivity(), topRevendedores, PlaceholderFragment.this);

        rvTopRevendedores.setAdapter(adapterTopRevendedores);
        rvTopProd.setAdapter(adapterTopProdutos);

        efabAtualizarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mToats != null) {

                    mToats.cancel();

                }

                mToats.makeText(getActivity(), "Atualizando Feed...", Toast.LENGTH_LONG).show();

                efabAtualizarHome.setVisibility(View.GONE);

                ArrayList<ProdutoObj> topsProd = new ArrayList<>();
                ArrayList<RevendedorObj> topsRev = new ArrayList<>();

                for (int i = 0; i < topProdutosRevendas.size(); i++) {
                    TopProdutosRevenda ob = topProdutosRevendas.get(i);
                    ProdutoObj produtoObj = new ProdutoObj(ob.getNomeProduto(), ob.getPathProduto(), ob.getIdProduto());
                    topsProd.add(produtoObj);
                    if (i > 19) break;
                }

                for (int j = 0; j < topRevendedores.size(); j++) {
                    TopRevendedores rev = topRevendedores.get(j);
                    RevendedorObj rObj = new RevendedorObj(rev.getNomeRevendedor(), rev.getPathFotoRevendedor(), rev.getUidRevendedor(), rev.getNumeroItensReveendas());
                    topsRev.add(rObj);
                    if (j > 5) break;
                }



                firestore.collection("Feed").document("Main").update("topRevendedores", topsRev, "topProdutos", topsProd, "timeStamp", System.currentTimeMillis()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        efabAtualizarHome.setVisibility(View.VISIBLE);
                        if (mToats != null) {

                            mToats.cancel();

                        }
                        mToats.makeText(getActivity(), "Feed atualizado", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        efabAtualizarHome.setVisibility(View.VISIBLE);
                        if (mToats != null) {

                            mToats.cancel();

                        }
                        mToats.makeText(getActivity(), "Falha ao atualizar Feed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void verRevendedor(String uid, String path, String nome) {


        Intent intent = new Intent(getContext(), VendedorActivity.class);

        intent.putExtra("uid", uid);

        startActivity(intent);
    }
}
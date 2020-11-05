package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rapha.vendafavorita.AdapterProdutos;
import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;

public class AdapterInterfaceMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ProdObj> produtos;
    private ArrayList<ProdObj> smartwatchs;
    private ArrayList<ProdObj> caixasDeSom;
    private ArrayList<ProdObj> eletronicos;
    private ArrayList<ProdObj> salaoEbarbearia;
    private ArrayList<ProdObj> computadorAcessorios;
    private ArrayList<ProdObj> gameAcessorios;
    private ArrayList<ProdObj> ferramentas;
    private ArrayList<ProdObj> acessoriosAutomotivos;
    private ListenerPrincipal listenerPrincipal;


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view2 = LayoutInflater.from(context).inflate(R.layout.item_abrir_chat, parent, false);
            return new AbrirPainelViewHolder(view2);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_list_main_catg_atalho, parent, false);
        return new CatgViewHolder(view, context, produtos);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {

            return;
        }

        CatgViewHolder vh = (CatgViewHolder) holder;

        switch (position) {
            case 1:
                vh.titulo.setText("SmartWatchs");
                vh.setRv(smartwatchs, context);
                break;
            case 2:
                vh.titulo.setText("Caixas de Som");
                vh.setRv(caixasDeSom, context);
                break;
            case 3:
                vh.titulo.setText("Eletronicos");
                vh.setRv(eletronicos, context);
                break;
            case 4:
                vh.titulo.setText("Salão e Barbearia");
                vh.setRv(salaoEbarbearia, context);
                break;
            case 5:
                vh.titulo.setText("Automotivos");
                vh.setRv(acessoriosAutomotivos, context);
                break;
            case 6:
                vh.titulo.setText("VideoGames");
                vh.setRv(gameAcessorios, context);
                break;
            case 7:
                vh.titulo.setText("Computador");
                vh.setRv(computadorAcessorios, context);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public interface ListenerPrincipal {
        void clickCategoria(ArrayList<ProdObj> produtos);
        void clickProduto(ProdObj prod);
        void abriPainelRevendedor();
    }

    public AdapterInterfaceMain(Context context, ArrayList<ProdObj> produtos, ListenerPrincipal listenerPrincipal) {
        this.context = context;
        this.produtos = produtos;
        this.listenerPrincipal = listenerPrincipal;
        this.smartwatchs = pegarProdutos("1");
        this.caixasDeSom = pegarProdutos("2");
        this.eletronicos = pegarProdutos("3");
        this.salaoEbarbearia = pegarProdutos("4");
        this.acessoriosAutomotivos = pegarProdutos("5");
        this.gameAcessorios = pegarProdutos("6");
        this.computadorAcessorios = pegarProdutos("7");
        //this.ferramentas = pegarProdutos("8");
    }

    private ArrayList<ProdObj> pegarProdutos(String catg) {

        ArrayList<ProdObj> lista = new ArrayList<>();

        for (int i = 0; i < produtos.size(); i++) {
            ProdObj obj = produtos.get(i);
            if (obj.getCategorias().containsKey(catg)) {
                lista.add(obj);
            }
        }

        return lista;
    }



    public class AbrirPainelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgPerfil;
        private ExtendedFloatingActionButton btOfertas1, btCelular2, btComputador3, btVideoGame4, btPetshop5, btEletronicos16, btFerramentas17, btDiversos19;

        public AbrirPainelViewHolder(@NonNull View view) {
            super(view);
            imgPerfil = (ImageView) view.findViewById(R.id.img_perfil);
            btOfertas1 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_1);
            btCelular2 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_2);
            btComputador3 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_3);
            btVideoGame4 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_4);
            btPetshop5 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_5);
            btEletronicos16 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_16);
            btFerramentas17 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_17);
            btDiversos19 = (ExtendedFloatingActionButton) view.findViewById(R.id.ll_bt_19);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerPrincipal.abriPainelRevendedor();
        }
    }

    class CatgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterProdutoAtalho.HandlerProdAtalho {

        private Context context;
        private ArrayList<ProdObj> produtos;
        private TextView titulo;
        private RecyclerView rv_ctg_main;

        public CatgViewHolder(@NonNull View itemView, Context context, ArrayList<ProdObj> produtos) {
            super(itemView);
            rv_ctg_main = (RecyclerView) itemView.findViewById(R.id.rv_ctg_main);
            titulo = (TextView) itemView.findViewById(R.id.title_catg);
            titulo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String ctgria = String.valueOf(getAdapterPosition());
            listenerPrincipal.clickCategoria(pegarProdutos(ctgria));
        }

        public void setRv(ArrayList<ProdObj> lista, Context context) {
            this.rv_ctg_main.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            AdapterProdutoAtalho adapterProdutoAtalho = new AdapterProdutoAtalho(lista, context, this);
            this.rv_ctg_main.setAdapter(adapterProdutoAtalho);
        }

        @Override
        public void abriProduto(ProdObj prod) {
            listenerPrincipal.clickProduto(prod);
        }
    }

}

package com.rapha.vendafavorita;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.MainActivity.ids;


public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.ProdutoPrincipalViewHolder> {

    private ClickProdutoCliente clickProdutoCliente;
    private Context context;
    private ArrayList<ProdObj> produtos;
    private boolean revenda;
    private int coluns;

    public interface ClickProdutoCliente {
        void openDetalhe(ProdObj prodObj);
        void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj);
        void categoria(int categ);
        void adm();
        void openChat();
    }

    public AdapterProdutos(ClickProdutoCliente clickProdutoCliente, Context context, ArrayList<ProdObj> produtos, boolean revenda, int colun) {
        this.clickProdutoCliente = clickProdutoCliente;
        this.context = context;
        this.produtos = produtos;
        this.revenda = revenda;
        this.coluns = colun;
    }

    @Override
    public int getItemViewType(int position) {
        if(coluns > 2) {
            return 2;
        }

        return 1;
    }

    @NonNull
    @Override
    public ProdutoPrincipalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        if(viewType == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_produto_principal_mini, parent, false);
            return new ProdutoPrincipalViewHolder(view, context, produtos);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_principal, parent, false);
        return new ProdutoPrincipalViewHolder(view, context, produtos);

    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoPrincipalViewHolder vh, int position) {
        if (revenda) {
            ProdObj obj = produtos.get(position);
            vh.setImagem(obj.imgCapa);
            vh.setPreco(String.valueOf((int) obj.prodValor) + ",00");
            vh.setNome(obj.prodName);
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.fab1)));
            if (ids.contains(obj.getIdProduto())) {
                vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.colorPrimaryDark)));
            }
        } else {
            ProdObj obj = produtos.get(position);
            vh.setImagem(obj.imgCapa);
            vh.setPreco(String.valueOf((int) obj.prodValor) + ",00");
            vh.setNome(obj.prodName);
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.fab1)));
            if (ids.contains(obj.getIdProduto())) {
                vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.colorPrimaryDark)));
            }
        }

        if(coluns >= 3) {
            vh.miniCard(true);
        } else {
            vh.miniCard(false);
        }
    }

    @Override
    public int getItemCount() {
        if (produtos == null) return 0;
        return produtos.size();
    }

    public class AbrirChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgPerfil;
        private ExtendedFloatingActionButton btOfertas1, btCelular2, btComputador3, btVideoGame4, btPetshop5, btEletronicos16, btFerramentas17, btDiversos19;

        public AbrirChatViewHolder(@NonNull View view) {
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
            Glide.with(context).load(pathFotoUser).into(imgPerfil);
            view.setOnClickListener(this);
            imgPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.adm();
                }
            });
            btOfertas1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(0);
                }
            });

            btCelular2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(1);
                }
            });

            btComputador3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(2);
                }
            });

            btVideoGame4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(3);
                }
            });

            btPetshop5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(4);
                }
            });

            btEletronicos16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(15);
                }
            });

            btFerramentas17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(16);
                }
            });

            btDiversos19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProdutoCliente.categoria(18);
                }
            });

        }

        @Override
        public void onClick(View v) {
            clickProdutoCliente.openChat();
        }
    }

    public class ProdutoPrincipalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, preco;
        private FloatingActionButton fab;
        private MaterialButton bt_item_novidades;

        private Context context;
        private ArrayList<ProdObj> produtos;

        public ProdutoPrincipalViewHolder(@NonNull View itemView, Context context, ArrayList<ProdObj> produtos) {
            super(itemView);
            this.context = context;
            this.produtos = produtos;
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            fab = (FloatingActionButton) itemView.findViewById(R.id.fab_produto_item);
            bt_item_novidades = (MaterialButton) itemView.findViewById(R.id.bt_item_novidades);
            itemView.setOnClickListener(this);
            fab.setOnClickListener(this);
            bt_item_novidades.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int prodPosition = getAdapterPosition();

            if (v.getId() == R.id.fab_produto_item) {
                clickProdutoCliente.onclick(prodPosition, fab.getBackgroundTintList(), v, produtos.get(prodPosition));
            } else {
                clickProdutoCliente.openDetalhe(produtos.get(prodPosition));
            }
        }

        public void setPreco(String p) {
            preco.setText(p);
        }

        public void setImagem(String img) {
            Glide.with(context).load(img).into(imageView);
        }

        public void setNome(String s) {
            nome.setText(s);
        }

        public void miniCard(boolean mini) {
            if(!mini) {
                bt_item_novidades.setVisibility(View.VISIBLE);
                preco.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                nome.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            } else {
                bt_item_novidades.setVisibility(View.GONE);
                preco.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                nome.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            }
        }

    }

}

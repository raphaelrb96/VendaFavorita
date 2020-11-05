package com.rapha.vendafavorita;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.MainActivity.ids;


public class AdapterProdutos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ClickProdutoCliente clickProdutoCliente;
    private Context context;
    private ArrayList<ProdObj> produtos;
    private boolean revenda;

    public interface ClickProdutoCliente {
        void openDetalhe(ProdObj prodObj);
        void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj);
        void categoria(int categ);
        void adm();
        void openChat();
    }

    public AdapterProdutos(ClickProdutoCliente clickProdutoCliente, Context context, ArrayList<ProdObj> produtos, boolean revenda) {
        this.clickProdutoCliente = clickProdutoCliente;
        this.context = context;
        this.produtos = produtos;
        this.revenda = revenda;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            if (revenda) {
                return 1;
            } else {
                return 2;
            }
        } else return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        if (viewType == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_produto_principal, parent, false);
            return new ProdutoPrincipalViewHolder(view, context, produtos);
        } else {

            View view2 = LayoutInflater.from(context).inflate(R.layout.item_abrir_chat, parent, false);
            AbrirChatViewHolder vh = new AbrirChatViewHolder(view2);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 2 && revenda) {
            ProdObj obj = produtos.get(position - 1);
            ProdutoPrincipalViewHolder vh = (ProdutoPrincipalViewHolder) holder;
            vh.setImagem(obj.imgCapa);
            vh.setPreco(String.valueOf((int) obj.prodValor) + ",00");
            vh.setNome(obj.prodName);
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.fab1)));
            if (ids.contains(obj.getIdProduto())) {
                vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.colorPrimaryDark)));
            }
        } else if(holder.getItemViewType() == 2 && !revenda) {
            ProdObj obj = produtos.get(position);
            ProdutoPrincipalViewHolder vh = (ProdutoPrincipalViewHolder) holder;
            vh.setImagem(obj.imgCapa);
            vh.setPreco(String.valueOf((int) obj.prodValor) + ",00");
            vh.setNome(obj.prodName);
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.fab1)));
            if (ids.contains(obj.getIdProduto())) {
                vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.colorPrimaryDark)));
            }
        } else {

        }
    }

    @Override
    public int getItemCount() {
        if (!revenda) return produtos.size();
        return produtos.size() + 1;
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
            itemView.setOnClickListener(this);
            fab.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int prodPosition = 0;
            if (revenda) {
                prodPosition = getAdapterPosition() - 1;
            } else {
                prodPosition = getAdapterPosition();
            }

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

    }

}

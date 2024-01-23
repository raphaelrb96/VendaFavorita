package com.rapha.vendafavorita;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

public class ProdObjParcelable implements Parcelable {

    Map<String, Boolean> categorias;
    String descr;
    boolean disponivel;
    String idProduto;
    String imgCapa;
    ArrayList<String> imagens;
    String fabricante;
    int nivel;
    String prodName;
    float prodValor;
    float valorAntigo;
    boolean promocional;
    Map<String, Boolean> tag;
    Map<String, Double> fornecedores;
    int quantidade;

    int comissao;

    ArrayList<String> cores;

    private float prodValorPromocional;
    private float prodValorAtacarejo;
    private float prodValorAtacado;

    private boolean atacado;
    private String urlVideo;
    private int numVendas;
    private float avaliacao;


    public ProdObjParcelable(Map<String, Boolean> categorias, String descr, boolean disponivel, String idProduto, String imgCapa, ArrayList<String> imagens, String fabricante, int nivel, String prodName, float prodValor, float valorAntigo, boolean promocional, Map<String, Boolean> tag, Map<String, Double> fornecedores, int quantidade, int comissao, ArrayList<String> cores, float prodValorPromocional, float prodValorAtacarejo, float prodValorAtacado, boolean atacado, String urlVideo, int numVendas, float avaliacao) {
        this.categorias = categorias;
        this.descr = descr;
        this.disponivel = disponivel;
        this.idProduto = idProduto;
        this.imgCapa = imgCapa;
        this.imagens = imagens;
        this.fabricante = fabricante;
        this.nivel = nivel;
        this.prodName = prodName;
        this.prodValor = prodValor;
        this.valorAntigo = valorAntigo;
        this.promocional = promocional;
        this.tag = tag;
        this.fornecedores = fornecedores;
        this.quantidade = quantidade;
        this.cores = cores;
        this.comissao = comissao;
        this.prodValorPromocional = prodValorPromocional;
        this.prodValorAtacarejo = prodValorAtacarejo;
        this.prodValorAtacado = prodValorAtacado;
        this.atacado = atacado;
        this.urlVideo = urlVideo;
        this.numVendas = numVendas;
        this.avaliacao = avaliacao;
    }


    protected ProdObjParcelable(Parcel in) {
        descr = in.readString();
        disponivel = in.readByte() != 0;
        idProduto = in.readString();
        imgCapa = in.readString();
        imagens = in.createStringArrayList();
        fabricante = in.readString();
        nivel = in.readInt();
        prodName = in.readString();
        prodValor = in.readFloat();
        valorAntigo = in.readFloat();
        promocional = in.readByte() != 0;
        quantidade = in.readInt();
        comissao = in.readInt();
        cores = in.createStringArrayList();
        prodValorPromocional = in.readFloat();
        prodValorAtacarejo = in.readFloat();
        prodValorAtacado = in.readFloat();
        atacado = in.readByte() != 0;
        urlVideo = in.readString();
        numVendas = in.readInt();
        avaliacao = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descr);
        dest.writeByte((byte) (disponivel ? 1 : 0));
        dest.writeString(idProduto);
        dest.writeString(imgCapa);
        dest.writeStringList(imagens);
        dest.writeString(fabricante);
        dest.writeInt(nivel);
        dest.writeString(prodName);
        dest.writeFloat(prodValor);
        dest.writeFloat(valorAntigo);
        dest.writeByte((byte) (promocional ? 1 : 0));
        dest.writeInt(quantidade);
        dest.writeInt(comissao);
        dest.writeStringList(cores);
        dest.writeFloat(prodValorPromocional);
        dest.writeFloat(prodValorAtacarejo);
        dest.writeFloat(prodValorAtacado);
        dest.writeByte((byte) (atacado ? 1 : 0));
        dest.writeString(urlVideo);
        dest.writeInt(numVendas);
        dest.writeFloat(avaliacao);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProdObjParcelable> CREATOR = new Creator<ProdObjParcelable>() {
        @Override
        public ProdObjParcelable createFromParcel(Parcel in) {
            return new ProdObjParcelable(in);
        }

        @Override
        public ProdObjParcelable[] newArray(int size) {
            return new ProdObjParcelable[size];
        }
    };

    public Map<String, Boolean> getCategorias() {
        return categorias;
    }

    public ArrayList<String> getImagens() {
        return imagens;
    }

    public float getValorAntigo() {
        return valorAntigo;
    }

    public String getDescr() {
        return descr;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public String getImgCapa() {
        return imgCapa;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public ProdObj getProd() {
        return new ProdObj(categorias, descr, disponivel, idProduto, imgCapa, imagens, fabricante, nivel, prodName, prodValor, valorAntigo, promocional, tag, fornecedores, quantidade, 0, comissao, cores, prodValorPromocional, prodValorAtacarejo, prodValorAtacado, atacado, urlVideo, numVendas, avaliacao);
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getNivel() {
        return nivel;
    }

    public String getProdName() {
        return prodName;
    }

    public float getProdValor() {
        return prodValor;
    }

    public boolean isPromocional() {
        return promocional;
    }

    public Map<String, Boolean> getTag() {
        return tag;
    }

    public Map<String, Double> getFornecedores() {
        return fornecedores;
    }

    public int getComissao() {
        return comissao;
    }

    public ArrayList<String> getCores() {
        return cores;
    }

    public float getProdValorPromocional() {
        return prodValorPromocional;
    }

    public float getProdValorAtacarejo() {
        return prodValorAtacarejo;
    }

    public float getProdValorAtacado() {
        return prodValorAtacado;
    }

    public boolean isAtacado() {
        return atacado;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public int getNumVendas() {
        return numVendas;
    }

    public float getAvaliacao() {
        return avaliacao;
    }
}

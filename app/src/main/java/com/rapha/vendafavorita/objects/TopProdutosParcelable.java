package com.rapha.vendafavorita.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class TopProdutosParcelable implements Parcelable {

    String nomeProduto;
    String pathProduto;
    String idProduto;
    int numeroDeRevendas;

    protected TopProdutosParcelable(Parcel in) {
        nomeProduto = in.readString();
        pathProduto = in.readString();
        idProduto = in.readString();
        numeroDeRevendas = in.readInt();
    }

    public TopProdutosParcelable(String nomeProduto, String pathProduto, String idProduto, int numeroDeRevendas) {
        this.nomeProduto = nomeProduto;
        this.pathProduto = pathProduto;
        this.idProduto = idProduto;
        this.numeroDeRevendas = numeroDeRevendas;
    }

    public static final Creator<TopProdutosParcelable> CREATOR = new Creator<TopProdutosParcelable>() {
        @Override
        public TopProdutosParcelable createFromParcel(Parcel in) {
            return new TopProdutosParcelable(in);
        }

        @Override
        public TopProdutosParcelable[] newArray(int size) {
            return new TopProdutosParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomeProduto);
        parcel.writeString(pathProduto);
        parcel.writeString(idProduto);
        parcel.writeInt(numeroDeRevendas);
    }

    public int getNumeroDeRevendas() {
        return numeroDeRevendas;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getPathProduto() {
        return pathProduto;
    }

    public TopProdutosRevenda getProdOfc() {
        return new TopProdutosRevenda(nomeProduto, pathProduto, idProduto, numeroDeRevendas);
    }
}

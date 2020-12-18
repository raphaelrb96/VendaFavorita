package com.rapha.vendafavorita.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class TopRevendedoresParcelable implements Parcelable {

    String nomeRevendedor;
    String pathFotoRevendedor;
    String uidRevendedor;
    int numeroItensReveendas;

    public TopRevendedoresParcelable(String nomeRevendedor, String pathFotoRevendedor, String uidRevendedor, int numeroItensReveendas) {
        this.nomeRevendedor = nomeRevendedor;
        this.pathFotoRevendedor = pathFotoRevendedor;
        this.uidRevendedor = uidRevendedor;
        this.numeroItensReveendas = numeroItensReveendas;
    }


    protected TopRevendedoresParcelable(Parcel in) {
        nomeRevendedor = in.readString();
        pathFotoRevendedor = in.readString();
        uidRevendedor = in.readString();
        numeroItensReveendas = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nomeRevendedor);
        dest.writeString(pathFotoRevendedor);
        dest.writeString(uidRevendedor);
        dest.writeInt(numeroItensReveendas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TopRevendedoresParcelable> CREATOR = new Creator<TopRevendedoresParcelable>() {
        @Override
        public TopRevendedoresParcelable createFromParcel(Parcel in) {
            return new TopRevendedoresParcelable(in);
        }

        @Override
        public TopRevendedoresParcelable[] newArray(int size) {
            return new TopRevendedoresParcelable[size];
        }
    };

    public String getNomeRevendedor() {
        return nomeRevendedor;
    }

    public String getPathFotoRevendedor() {
        return pathFotoRevendedor;
    }

    public String getUidRevendedor() {
        return uidRevendedor;
    }

    public int getNumeroItensReveendas() {
        return numeroItensReveendas;
    }

    public TopRevendedores getOfc() {
        return new TopRevendedores(nomeRevendedor, pathFotoRevendedor, uidRevendedor, numeroItensReveendas);
    }
}

package com.rapha.vendafavorita.objects;

public class TopAdms {

    String nomeRevendedor;
    String pathFotoRevendedor;
    String uidRevendedor;
    int numAfiliados;

    public TopAdms() {

    }

    public TopAdms(String nomeRevendedor, String pathFotoRevendedor, String uidRevendedor, int numAfiliados) {
        this.nomeRevendedor = nomeRevendedor;
        this.pathFotoRevendedor = pathFotoRevendedor;
        this.uidRevendedor = uidRevendedor;
        this.numAfiliados = numAfiliados;
    }


    public String getNomeRevendedor() {
        return nomeRevendedor;
    }

    public void setNomeRevendedor(String nomeRevendedor) {
        this.nomeRevendedor = nomeRevendedor;
    }

    public String getPathFotoRevendedor() {
        return pathFotoRevendedor;
    }

    public void setPathFotoRevendedor(String pathFotoRevendedor) {
        this.pathFotoRevendedor = pathFotoRevendedor;
    }

    public String getUidRevendedor() {
        return uidRevendedor;
    }

    public void setUidRevendedor(String uidRevendedor) {
        this.uidRevendedor = uidRevendedor;
    }

    public int getNumAfiliados() {
        return numAfiliados;
    }

    public void setNumAfiliados(int numAfiliados) {
        this.numAfiliados = numAfiliados;
    }
}

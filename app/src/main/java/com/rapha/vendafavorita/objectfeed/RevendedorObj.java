package com.rapha.vendafavorita.objectfeed;

public class RevendedorObj {

    String nomeRevendedor;
    String pathFotoRevendedor;
    String uidRevendedor;
    int numeroItensReveendas;

    public RevendedorObj(String nomeRevendedor, String pathFotoRevendedor, String uidRevendedor, int numeroItensReveendas) {
        this.nomeRevendedor = nomeRevendedor;
        this.pathFotoRevendedor = pathFotoRevendedor;
        this.uidRevendedor = uidRevendedor;
        this.numeroItensReveendas = numeroItensReveendas;
    }

    public RevendedorObj() {

    }

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
}

package com.rapha.vendafavorita.objects;

public class TopRevendedores implements Comparable<TopRevendedores> {

    String nomeRevendedor;
    String pathFotoRevendedor;
    String uidRevendedor;
    int numeroItensReveendas;

    public TopRevendedores(String nomeRevendedor, String pathFotoRevendedor, String uidRevendedor, int numeroItensReveendas) {
        this.nomeRevendedor = nomeRevendedor;
        this.pathFotoRevendedor = pathFotoRevendedor;
        this.uidRevendedor = uidRevendedor;
        this.numeroItensReveendas = numeroItensReveendas;
    }

    public TopRevendedores() {

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

    public int getNumeroItensReveendas() {
        return numeroItensReveendas;
    }

    public void setNumeroItensReveendas(int numeroItensReveendas) {
        this.numeroItensReveendas = numeroItensReveendas;
    }

    public TopRevendedoresParcelable getOfc() {
        return new TopRevendedoresParcelable(nomeRevendedor, pathFotoRevendedor, uidRevendedor, numeroItensReveendas);
    }

    @Override
    public int compareTo(TopRevendedores topRevendedores) {
        if (this.numeroItensReveendas > topRevendedores.getNumeroItensReveendas()) {
            return -1;
        } if (this.numeroItensReveendas < topRevendedores.getNumeroItensReveendas()) {
            return 1;
        }
        return 0;
    }
}

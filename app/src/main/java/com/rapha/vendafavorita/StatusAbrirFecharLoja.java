package com.rapha.vendafavorita;

public class StatusAbrirFecharLoja {

    private boolean aberto;

    public StatusAbrirFecharLoja(boolean aberto) {
        this.aberto = aberto;
    }

    public StatusAbrirFecharLoja() {
    }

    public boolean isAberto() {
        return aberto;
    }
}

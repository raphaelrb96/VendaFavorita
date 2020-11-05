package com.rapha.vendafavorita.objects;

public class Falta {

    private String prod;
    private long time;

    public Falta(String prod, long time) {
        this.prod = prod;
        this.time = time;
    }

    public Falta() {

    }

    public String getProd() {
        return prod;
    }

    public long getTime() {
        return time;
    }
}

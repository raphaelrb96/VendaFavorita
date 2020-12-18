package com.rapha.vendafavorita.analitycs;

public class ItemFace {

    String id;
    String quantity;

    public ItemFace(String id, String quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public ItemFace() {
    }

    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }
}

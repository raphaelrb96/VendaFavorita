package com.rapha.vendafavorita.objects;

public class ObjectBonus {
    String meta;
    String bonus;

    public ObjectBonus(String meta, String bonus) {
        this.meta = meta;
        this.bonus = bonus;
    }

    public ObjectBonus() {

    }

    public String getBonus() {
        return bonus;
    }

    public String getMeta() {
        return meta;
    }
}

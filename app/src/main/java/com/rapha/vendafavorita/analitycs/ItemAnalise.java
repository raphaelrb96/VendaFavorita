package com.rapha.vendafavorita.analitycs;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemAnalise  {

    public String item_name;
    public String item_id;
    public String item_category;

    public ItemAnalise(String name, String id, String category) {
        this.item_name = name;
        this.item_id = id;
        this.item_category = category;
    }


    public String getItem_name() {
        return item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_category() {
        return item_category;
    }
}

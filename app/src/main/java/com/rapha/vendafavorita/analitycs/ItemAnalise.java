package com.rapha.vendafavorita.analitycs;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemAnalise implements Parcelable {

    public String item_name;
    public String item_id;
    public String item_category;

    public ItemAnalise(String name, String id, String category) {
        this.item_name = name;
        this.item_id = id;
        this.item_category = category;
    }

    protected ItemAnalise(Parcel in) {
        item_name = in.readString();
        item_id = in.readString();
        item_category = in.readString();
    }

    public static final Creator<ItemAnalise> CREATOR = new Creator<ItemAnalise>() {
        @Override
        public ItemAnalise createFromParcel(Parcel in) {
            return new ItemAnalise(in);
        }

        @Override
        public ItemAnalise[] newArray(int size) {
            return new ItemAnalise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(item_name);
        parcel.writeString(item_id);
        parcel.writeString(item_category);
    }
}

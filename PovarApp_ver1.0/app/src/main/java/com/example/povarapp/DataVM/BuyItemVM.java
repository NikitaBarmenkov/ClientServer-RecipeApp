package com.example.povarapp.DataVM;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BuyItemVM implements Serializable, Comparable<BuyItemVM> {
    private int id;
    private String name;
    @SerializedName("UserId")
    private int userId;
    private String quantity;
    private String unit;
    private String quantity_unit;
    private boolean checked;

    public int GetId() {
        return id;
    }
    public void SetId(int id) {
        this.id = id;
    }

    public String GetName() {
        return name;
    }
    public void SetName(String name) {
        this.name = name;
    }

    public String GetQuantity() {
        return quantity;
    }
    public void SetQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean GetChecked() {
        return checked;
    }
    public void SetChecked(boolean checked) {
        this.checked = checked;
    }

    public int GetUserId() {
        return userId;
    }
    public void SetUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int compareTo(BuyItemVM o) {
        int value1 = this.GetChecked() ? 1 : 0;
        int value2 = o.GetChecked() ? 1 : 0;
        return Integer.compare(value1, value2);
    }

    public String GetUnit() {
        return unit;
    }
    public void SetUnit(String unit) {
        this.unit = unit;
    }

    public String GetQuantity_unit() {
        return quantity_unit;
    }
    public void SetQuantity_unit(String quantity_unit) {
        this.quantity_unit = quantity_unit;
    }
}

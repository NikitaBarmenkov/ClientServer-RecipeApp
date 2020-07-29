package com.example.povarapp.DataVM;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavVM implements Serializable {
    private int id;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("DishId")
    private int dishId;

    public FavVM() {}

    public int GetId() {
        return id;
    }
    public void SetId(int id) {
        this.id = id;
    }

    public int GetUserId() {
        return userId;
    }
    public void SetUserId(int userId) {
        this.userId = userId;
    }

    public int GetDish_Id() {
        return dishId;
    }
    public void SetDish_Id(int dish_Id) {
        this.dishId = dish_Id;
    }
}

package com.example.povarapp.DataVM;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DishIngredientVM implements Serializable {
    private int id;
    @SerializedName("DishId")
    private int dishId;
    @SerializedName("IngredientId")
    private int ingredientId;
    private String ingredientName;
    private float quantity;
    private String unit;
    private int action;

    public DishIngredientVM() {}

    public void SetId(int id) { this.id = id; }
    public int GetId() {
        return this.id;
    }

    public void SetDishId(int dishId) {
        this.dishId = dishId;
    }
    public int GetDishId() {
        return this.dishId;
    }

    public void SetIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }
    public int GetIngredientId() {
        return this.ingredientId;
    }

    public void SetQuantity(float quantity) {
        this.quantity = quantity;
    }
    public float GetQuantity() {
        return this.quantity;
    }

    public void SetIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public String GetIngredientName() {
        return this.ingredientName;
    }

    public int GetAction() {
        return action;
    }
    public void SetAction(int action) {
        this.action = action;
    }

    public String GetUnit() {
        return unit;
    }
    public void SetUnit(String unit) {
        this.unit = unit;
    }
}

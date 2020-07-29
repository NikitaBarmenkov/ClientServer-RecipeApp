package com.example.povarapp.DataVM;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeStepVM implements Serializable, Comparable<RecipeStepVM> {
    private int id;
    private int number;
    private String text;
    @SerializedName("DishId")
    private int dishId;
    private int action;

    public RecipeStepVM() {}

    public void SetId(int id) { this.id = id; }
    public int GetId() {
        return this.id;
    }

    public void SetStepNumber(int stepNumber) {
        this.number = stepNumber;
    }
    public int GetStepNumber() {
        return this.number;
    }

    public void SetText(String text) {
        this.text = text;
    }
    public String GetText() {
        return this.text;
    }

    public void SetDishId(int dishId) {
        this.dishId = dishId;
    }
    public int GetDishId() {
        return this.dishId;
    }

    public int GetAction() {
        return action;
    }
    public void SetAction(int action) {
        this.action = action;
    }

    @Override
    public int compareTo(RecipeStepVM o) {
        return Integer.compare(this.GetStepNumber(), o.GetStepNumber());
    }
}

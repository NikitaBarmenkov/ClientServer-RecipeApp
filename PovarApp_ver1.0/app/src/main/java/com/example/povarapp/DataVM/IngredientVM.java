package com.example.povarapp.DataVM;

import java.io.Serializable;

public class IngredientVM implements Serializable {
    private int id;
    private String name;
    private boolean isAdded;

    public IngredientVM() {}

    public void SetId(int id) { this.id = id; }
    public int GetId() {
        return this.id;
    }

    public void SetName(String name) {
        this.name = name;
    }
    public String GetName() {
        return this.name;
    }

    public void SetAdded(boolean added) {
        this.isAdded = added;
    }
    public boolean GetIsAdded() {
        return this.isAdded;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

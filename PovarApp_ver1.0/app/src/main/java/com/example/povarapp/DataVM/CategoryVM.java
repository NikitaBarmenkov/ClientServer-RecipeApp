package com.example.povarapp.DataVM;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryVM implements Serializable {
    private int id;
    private String name;
    private ArrayList<DishVM> dishes;

    public CategoryVM() {}

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

    @Override
    public String toString() {
        return this.name;
    }

    public ArrayList<DishVM> GetDishes() {
        return dishes;
    }
    public void SetDishes(ArrayList<DishVM> dishes) {
        this.dishes = dishes;
    }
}

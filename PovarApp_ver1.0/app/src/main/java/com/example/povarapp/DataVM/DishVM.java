package com.example.povarapp.DataVM;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DishVM implements Serializable {
    private int id;
    private String name;
    @SerializedName("CategoryId")
    private int categoryId;
    private String categoryName;
    private double kkal;
    private double cooktime;
    private double rating;
    private Date creation_date;
    private Date update_date;
    private boolean is_public;//доступ к блюду
    @SerializedName("UserId")
    private int userId;
    private String userName;
    private ArrayList<DishIngredientVM> ingredients;
    private ArrayList<RecipeStepVM> recipes;
    private ArrayList<ReviewVM> reviews;
    public ArrayList<Integer> recipeStepsToDelete;
    public ArrayList<Integer> dishIngredientsToDelete;

    public DishVM() {}

    public void SetId(int id) { this.id = id; }
    public int GetId() {
        return this.id;
    }

    public void SetName(String Name) {
        this.name = Name;
    }
    public String GetName() {
        return this.name;
    }

    public void SetCategoryId(int CategoryId) {
        this.categoryId = CategoryId;
    }
    public int GetCategoryId() { return this.categoryId; }

    public void SetKkal(double Kkal) {
        this.kkal = Kkal;
    }
    public double GetKkal() { return this.kkal; }

    public void SetCookTime(double CookTime) {
        this.cooktime = CookTime;
    }
    public double GetCookTime() { return this.cooktime; }

    public void SetRating(double Rating) {
        this.rating = Rating;
    }
    public double GetRating() { return this.rating; }

    public void SetDate(Date Date) {
        this.creation_date = Date;
    }
    public Date GetDate() {
        return this.creation_date;
    }

    public void SetIsPublic(boolean isPublic) {
        this.is_public = isPublic;
    }
    public boolean GetIsPublic() {
        return this.is_public;
    }

    public void SetUserId(int appUserId) {
        this.userId = appUserId;
    }
    public int GetUserId() { return this.userId; }

    @Override
    public String toString() {
        return this.name;
    }

    public void SetCategoryName(String category_name) {
        this.categoryName = category_name;
    }
    public String GetCategoryName() {
        return this.categoryName;
    }

    public void SetUserName(String appUserName) {
        this.userName = appUserName;
    }
    public String GetUserName() {
        return this.userName;
    }

    public void SetDishIngredients(ArrayList<DishIngredientVM> dishIngredients) {
        this.ingredients = dishIngredients;
    }
    public ArrayList<DishIngredientVM> GetDishIngredients() {
        return this.ingredients;
    }

    public void SetRecipeSteps(ArrayList<RecipeStepVM> recipeSteps) {
        this.recipes = recipeSteps;
    }
    public ArrayList<RecipeStepVM> GetRecipeSteps() {
        return this.recipes;
    }

    public Date GetUpdate_date() {
        return update_date;
    }

    public void SetUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public ArrayList<ReviewVM> GetReviews() {
        return reviews;
    }
    public void SetReviews(ArrayList<ReviewVM> reviews) {
        this.reviews = reviews;
    }
}

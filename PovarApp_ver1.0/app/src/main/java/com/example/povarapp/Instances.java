package com.example.povarapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.IngredientVM;
import com.example.povarapp.DataVM.RecipeStepVM;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Instances implements Serializable {
    private DishVM dish;
    private IngredientVM ingredient;
    private DishIngredientVM dishIngredient;
    private RecipeStepVM recipeStep;

    public Instances() {
        dish = new DishVM();
        ingredient = new IngredientVM();
        dishIngredient = new DishIngredientVM();
        recipeStep = new RecipeStepVM();
    }

    public DishVM newDishInstance() {
        dish = new DishVM();
        dish.SetId(0);
        dish.SetName("");
        dish.SetCategoryId(0);
        dish.SetCategoryName("");
        dish.SetKkal(0);
        dish.SetCookTime(0);
        //dish.SetImage(GetDefaultImageBytes());
        dish.SetIsPublic(false);

        ArrayList<RecipeStepVM> recipes = new ArrayList<>();
        recipes.add(newRecipeInstance());
        dish.SetRecipeSteps(recipes);

        ArrayList<DishIngredientVM> dis = new ArrayList<>();
        dis.add(newDishingInstance());
        dish.SetDishIngredients(dis);

        dish.recipeStepsToDelete = new ArrayList<>();
        dish.dishIngredientsToDelete = new ArrayList<>();

        return dish;
    }

    private byte[] GetDefaultImageBytes() {
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher);
        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public IngredientVM newIngredientInstance() {
        ingredient = new IngredientVM();
        ingredient.SetId(-1);
        ingredient.SetName("");
        ingredient.SetAdded(false);
        return ingredient;
    }

    public DishIngredientVM newDishingInstance() {
        dishIngredient = new DishIngredientVM();
        dishIngredient.SetId(-1);
        dishIngredient.SetDishId(-1);
        dishIngredient.SetIngredientId(-1);
        dishIngredient.SetQuantity(0);
        dishIngredient.SetUnit("");
        dishIngredient.SetAction(1);
        dishIngredient.SetIngredientName("");
        return dishIngredient;
    }
    public RecipeStepVM newRecipeInstance() {
        recipeStep = new RecipeStepVM();
        recipeStep.SetId(-1);
        recipeStep.SetDishId(-1);
        recipeStep.SetText("");
        recipeStep.SetStepNumber(1);
        recipeStep.SetAction(1);
        return recipeStep;
    }
}

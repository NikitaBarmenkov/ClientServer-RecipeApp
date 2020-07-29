package com.example.povarapp;

import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.RecipeStepVM;
import com.example.povarapp.DataVM.ReviewVM;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void Test1_1() {
        DishActivity activity = new DishActivity();
        DishVM dish = new DishVM();
        dish.SetName("");
        dish.SetCategoryId(1);
        dish.SetRecipeSteps(new ArrayList<RecipeStepVM>());
        dish.SetDishIngredients(new ArrayList<DishIngredientVM>());
        boolean actual_result = activity.checkDish(dish);

        assertEquals(false, actual_result);
    }

    @Test
    public void Test1_2() {
        DishActivity activity = new DishActivity();
        DishVM dish = new DishVM();
        dish.SetName("Название блюда");
        dish.SetCategoryId(1);
        dish.SetRecipeSteps(new ArrayList<RecipeStepVM>());
        dish.SetDishIngredients(new ArrayList<DishIngredientVM>());
        boolean actual_result = activity.checkDish(dish);

        assertEquals(false, actual_result);
    }

    @Test
    public void Test1_3() {
        DishActivity activity = new DishActivity();
        DishVM dish = new DishVM();
        dish.SetName("Название блюда");
        dish.SetCategoryId(1);
        ArrayList<RecipeStepVM> recipe = new ArrayList<>();
        RecipeStepVM step;
        for (int i = 0; i < 5; i++) {
            step = new RecipeStepVM();
            step.SetStepNumber(i+1);
            step.SetText("Шаг рецепта " + i+1);
            recipe.add(step);
        }
        dish.SetRecipeSteps(recipe);
        dish.SetDishIngredients(new ArrayList<DishIngredientVM>());
        boolean actual_result = activity.checkDish(dish);

        assertEquals(false, actual_result);
    }

    @Test
    public void Test1_4() {
        DishActivity activity = new DishActivity();
        DishVM dish = new DishVM();
        dish.SetName("Название блюда");
        dish.SetCategoryId(1);
        ArrayList<RecipeStepVM> recipe = new ArrayList<>();
        RecipeStepVM step;
        for (int i = 0; i < 5; i++) {
            step = new RecipeStepVM();
            step.SetStepNumber(i+1);
            step.SetText("Шаг рецепта " + i+1);
            recipe.add(step);
        }
        dish.SetRecipeSteps(recipe);

        ArrayList<DishIngredientVM> ings = new ArrayList<>();
        DishIngredientVM ing;
        for (int i = 0; i < 10; i++) {
            ing = new DishIngredientVM();
            ing.SetIngredientName("Ингредиент" + i+1);
            ing.SetQuantity(3.3f);
            ing.SetUnit("asd");
            ing.SetIngredientId(i+1);
            ings.add(ing);
        }
        dish.SetDishIngredients(ings);
        boolean actual_result = activity.checkDish(dish);

        assertEquals(true, actual_result);
    }

    @Test
    public void Test2_1() {
        ReviewActivity activity = new ReviewActivity();
        ReviewVM review = new ReviewVM();
        boolean actual_result = activity.ValidateReviewData(review);

        assertEquals(false, actual_result);
    }

    @Test
    public void Test2_2() {
        ReviewActivity activity = new ReviewActivity();
        ReviewVM review = new ReviewVM();
        review.SetUserId(1);
        review.SetDishId(1);
        review.SetRating(0);
        boolean actual_result = activity.ValidateReviewData(review);

        assertEquals(false, actual_result);
    }

    @Test
    public void Test2_3() {
        ReviewActivity activity = new ReviewActivity();
        ReviewVM review = new ReviewVM();
        review.SetUserId(1);
        review.SetDishId(1);
        review.SetRating(4.5f);
        boolean actual_result = activity.ValidateReviewData(review);

        assertEquals(true, actual_result);
    }
}
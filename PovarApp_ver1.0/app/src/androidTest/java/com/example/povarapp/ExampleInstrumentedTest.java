package com.example.povarapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.RecipeStepVM;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void Test1() {
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //assertEquals("com.example.povarapp", appContext.getPackageName());

        DishActivity activity = new DishActivity();
        DishVM dish = new DishVM();
        dish.SetName("sad");
        dish.SetCategoryId(1);
        dish.SetRecipeSteps(new ArrayList<RecipeStepVM>());
        dish.SetDishIngredients(new ArrayList<DishIngredientVM>());
        boolean actual_result = activity.checkDish(dish);

        assertEquals(false, actual_result);
    }
}

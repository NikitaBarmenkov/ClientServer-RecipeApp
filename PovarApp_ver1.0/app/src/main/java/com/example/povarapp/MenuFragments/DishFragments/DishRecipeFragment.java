package com.example.povarapp.MenuFragments.DishFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.povarapp.Enums.DishState;
import com.example.povarapp.Instances;
import com.example.povarapp.MainActivity;
import com.example.povarapp.MenuFragments.DishFragments.Adapters.DishRecipeAdapter;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.RecipeStepVM;

import java.util.ArrayList;
import java.util.Collections;

public class DishRecipeFragment extends Fragment implements DishRecipeAdapter.OnRecipeStepAction {
    private DishVM dish;
    private DishState dishState;
    private ArrayList<RecipeStepVM> recipe;
    private DishRecipeAdapter dishRecipeAdapter;
    private Instances instances;

    private RecyclerView dishpage_recipe_rv;
    private FloatingActionButton add_dishrecipe;

    public boolean isInitialized = false;

    public DishRecipeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_recipe_fragment, container, false);
        instances = new Instances();
        GetBundle();
        InitViewWithData(view);
        ChangeView(dishState);
        return view;
    }

    private void GetBundle() {
        recipe = new ArrayList<>();
        try {
            Bundle args = this.getArguments();
            dishState = (DishState) args.getSerializable("dishState");
            dish = (DishVM) args.getSerializable("dish");
            recipe = dish.GetRecipeSteps();
            Collections.sort(recipe);
            isInitialized = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void InitViewWithData(View view) {
        //инициализация
        dishpage_recipe_rv = (RecyclerView) view.findViewById(R.id.dishpage_recipe_rv);
        add_dishrecipe = (FloatingActionButton) view.findViewById(R.id.add_dishrecipe);
        SetData();
    }

    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
            int position_dragged = dragged.getAdapterPosition();
            int position_target = target.getAdapterPosition();

            Collections.swap(recipe, position_dragged, position_target);
            for (int i = 0; i < recipe.size(); i++) {
                recipe.get(i).SetStepNumber(i+1);
            }
            Collections.sort(recipe);
            dishRecipeAdapter.notifyItemMoved(position_dragged, position_target);
            dishRecipeAdapter.notifyDataSetChanged();

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }
    });

    private void SetData() {
        dishRecipeAdapter = new DishRecipeAdapter(getContext(), this, recipe, dishState);
        dishpage_recipe_rv.setAdapter(dishRecipeAdapter);
        add_dishrecipe.setOnClickListener(add_dishrecipe_listener);
    }

    private View.OnClickListener add_dishrecipe_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecipeStepVM step = instances.newRecipeInstance();
            step.SetStepNumber(recipe.get(recipe.size() - 1).GetStepNumber() + 1);
            step.SetAction(1);
            /*for (RecipeStepVM item : recipe) {
                if (item.GetStepNumber() > recipeStep.GetStepNumber()) {
                    item.SetStepNumber(item.GetStepNumber() + 1);
                }
            }
            step.SetStepNumber(recipeStep.GetStepNumber() + 1);*/
            recipe.add(step);
            Collections.sort(recipe);
            dishRecipeAdapter.notifyItemInserted(recipe.size() - 1);
            dishRecipeAdapter.notifyDataSetChanged();
        }
    };

    public ArrayList<RecipeStepVM> GetDataFromFragment() {
        return this.recipe;
    }

    public void ChangeView(DishState dishState) {
        Bundle args = this.getArguments();
        args.putSerializable("dishState", dishState);
        this.setArguments(args);
        switch(dishState) {
            case EDIT:
            case ADD:
                add_dishrecipe.setVisibility(View.VISIBLE);
                helper.attachToRecyclerView(dishpage_recipe_rv);
                break;
            case VIEW:
                add_dishrecipe.setVisibility(View.GONE);
                helper.attachToRecyclerView(null);
                break;
        }
        dishRecipeAdapter.ChangeViewState(dishState);
        dishRecipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDeleteStep(RecipeStepVM recipeStep) {
        /*for (RecipeStepVM item : recipe) {
            if (item.GetStepNumber() > recipeStep.GetStepNumber()) {
                item.SetStepNumber(item.GetStepNumber() - 1);
                item.SetAction(2);
            }
        }*/
        recipe.remove(recipeStep);
        for (int i = 0; i < recipe.size(); i++) {
            recipe.get(i).SetStepNumber(i+1);
        }
        if (recipeStep.GetAction() != 1) {
            dish.recipeStepsToDelete.add(recipeStep.GetId());
        }
        Collections.sort(recipe);
        dishRecipeAdapter.notifyItemRemoved(recipeStep.GetStepNumber() - 1);
        dishRecipeAdapter.notifyDataSetChanged();
    }
}

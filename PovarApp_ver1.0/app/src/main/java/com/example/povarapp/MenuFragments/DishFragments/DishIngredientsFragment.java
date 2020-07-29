package com.example.povarapp.MenuFragments.DishFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.povarapp.DishActivity;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.Instances;
import com.example.povarapp.MainActivity;
import com.example.povarapp.MenuFragments.DishFragments.Adapters.DishIngredientsAdapter;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.IngredientVM;
import com.example.povarapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DishIngredientsFragment extends Fragment implements DishIngredientsAdapter.OnDishIngredientAction{
    private ArrayList<DishIngredientVM> dishIngredients;
    private DishState dishState;
    private DishVM dish;
    private ArrayList<IngredientVM> ingredients;
    private DishIngredientsAdapter dishIngredientsAdapter;
    private Instances instances;

    private RecyclerView dishpage_ingredients_rv;
    private FloatingActionButton add_dishing;
    private Button add_to_buylist;

    private DishActivity activity;

    public boolean isInitialized = false;

    public DishIngredientsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_ingredients_fragment, container, false);
        instances = new Instances();
        activity = (DishActivity) getActivity();
        GetBundle();
        InitViewWithData(view);
        ChangeView(dishState);
        return view;
    }

    private void GetBundle() {
        try {
            Bundle args = this.getArguments();
            dishState = (DishState) args.getSerializable("dishState");
            dish = (DishVM) args.getSerializable("dish");
            dishIngredients = dish.GetDishIngredients();
            ingredients = (ArrayList<IngredientVM>) args.getSerializable("ingredients");
            isInitialized = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void InitViewWithData(View view) {
        //инициализация
        dishpage_ingredients_rv = (RecyclerView) view.findViewById(R.id.dishpage_ingredients_rv);
        add_dishing = (FloatingActionButton) view.findViewById(R.id.add_dishing);
        add_to_buylist = (Button) view.findViewById(R.id.add_to_buylist);
        SetDishIngredients();
    }

    private void SetDishIngredients() {
        dishIngredientsAdapter = new DishIngredientsAdapter(getActivity(), this, dishIngredients, ingredients, dishState);
        dishpage_ingredients_rv.setAdapter(dishIngredientsAdapter);
        add_dishing.setOnClickListener(add_dishing_listener);
        add_to_buylist.setOnClickListener(add_to_buylist_listener);
    }

    private View.OnClickListener add_dishing_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DishIngredientVM ing = instances.newDishingInstance();
            dishIngredients.add(ing);
            //dishIngredientsAdapter.notifyItemInserted(dishIngredients.size() - 1);
            dishIngredientsAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener add_to_buylist_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.AddToBuyList(dishIngredients);
        }
    };

    public ArrayList<DishIngredientVM> GetDataFromFragment() {
        //this.dishIngredients = dishIngredientsAdapter.GetDishIngredients();
        return this.dishIngredients;
    }

    public void ChangeView(DishState dishState) {
        if (dishIngredients != null) {
            try {
                Bundle args = this.getArguments();
                args.putSerializable("dishState", dishState);
                this.setArguments(args);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            switch (dishState) {
                case EDIT:
                case ADD:
                    add_dishing.setVisibility(View.VISIBLE);
                    add_to_buylist.setVisibility(View.GONE);
                    break;
                case VIEW:
                    add_dishing.setVisibility(View.GONE);
                    add_to_buylist.setVisibility(View.VISIBLE);
                    break;
            }
            dishIngredientsAdapter.ChangeViewState(dishState);
            dishIngredientsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnDeleteIngredient(DishIngredientVM dishIngredient) {
        dishIngredients.remove(dishIngredient);
        if (dishIngredient.GetIngredientId() > 0) {
            IngredientVM ing = new IngredientVM();
            ing.SetId(dishIngredient.GetIngredientId());
            ing.SetName(dishIngredient.GetIngredientName());
            ingredients.add(ing);
            dishIngredientsAdapter.notifyDataSetChanged();
        }
        if (dishIngredient.GetAction() != 1) {
            dish.dishIngredientsToDelete.add(dishIngredient.GetId());
        }
        if (dishIngredients.isEmpty()) {
            DishIngredientVM ing = instances.newDishingInstance();
            dishIngredients.add(ing);
            dishIngredientsAdapter.notifyDataSetChanged();
        }
        dishIngredientsAdapter.notifyDataSetChanged();
    }
}

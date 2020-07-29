package com.example.povarapp.MenuFragments.DishFragments.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.povarapp.Enums.DishState;
import com.example.povarapp.Enums.MainState;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.RecipeStepVM;

import java.util.ArrayList;

public class DishRecipeAdapter extends RecyclerView.Adapter<DishRecipeAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<RecipeStepVM> recipe;
    private OnRecipeStepAction listener;
    private DishState state;

    public interface OnRecipeStepAction {
        public void OnDeleteStep(RecipeStepVM recipeStep);
    }

    public void ChangeViewState(DishState state) {
        this.state = state;
    }

    public DishRecipeAdapter (Context context, OnRecipeStepAction listener, ArrayList<RecipeStepVM> recipe, DishState state) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recipe = recipe;
        this.listener = listener;
        this.state = state;
    }

    @NonNull
    @Override
    public DishRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.recipe_step, viewGroup, false);
        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(DishRecipeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.myCustomEditTextListener.updatePosition(i);
        viewHolder.bind(recipe.get(i), i);
        viewHolder.changeView(recipe.get(i), this.state);
        viewHolder.SetButtonClickListeners(recipe.get(i));
    }

    @Override
    public int getItemCount() {
        return recipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dishpage_recipe_step_number;
        private ImageButton dishpage_recipe_step_delete_but;
        private EditText dishpage_recipe_step_text;
        public MyCustomEditTextListener myCustomEditTextListener;

        ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);
            dishpage_recipe_step_number = (TextView) v.findViewById(R.id.dishpage_recipe_step_number);
            dishpage_recipe_step_delete_but = (ImageButton) v.findViewById(R.id.dishpage_recipe_step_delete_but);
            dishpage_recipe_step_text = (EditText) v.findViewById(R.id.dishpage_recipe_step_text);
            this.myCustomEditTextListener = myCustomEditTextListener;
            dishpage_recipe_step_text.addTextChangedListener(myCustomEditTextListener);
        }

        public void changeView(RecipeStepVM recipeStep, DishState state) {
            if (state == DishState.VIEW) {
                dishpage_recipe_step_delete_but.setVisibility(View.GONE);
                dishpage_recipe_step_text.setEnabled(false);
            }
            else {
                dishpage_recipe_step_delete_but.setVisibility(View.VISIBLE);
                dishpage_recipe_step_text.setEnabled(true);
            }
        }

        public void bind(RecipeStepVM recipeStep, int position) {
            dishpage_recipe_step_text.setText(recipeStep.GetText());
            dishpage_recipe_step_number.setText("Шаг " + String.valueOf(recipeStep.GetStepNumber()));
        }

        public void SetButtonClickListeners(final RecipeStepVM recipeStep) {
            dishpage_recipe_step_delete_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipe.size() > 0)
                        listener.OnDeleteStep(recipeStep);
                }
            });
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (recipe.get(position).GetText() != s) {
                recipe.get(position).SetText(s.toString());
                if (recipe.get(position).GetAction() != 1)
                    recipe.get(position).SetAction(2);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }
}

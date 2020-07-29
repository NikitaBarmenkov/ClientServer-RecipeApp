package com.example.povarapp.MenuFragments.DishFragments.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.IngredientVM;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.R;

import java.util.ArrayList;

public class DishIngredientsAdapter extends RecyclerView.Adapter<DishIngredientsAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DishIngredientVM> dishIngredients;
    private ArrayList<IngredientVM> ingredients;
    private OnDishIngredientAction listener;
    private DishState state;

    private ArrayList<String> units;

    public interface OnDishIngredientAction {
        public void OnDeleteIngredient(DishIngredientVM dishIngredient);
    }

    public void ChangeViewState(DishState state) {
        this.state = state;
    }

    public DishIngredientsAdapter (Context context, OnDishIngredientAction listener, ArrayList<DishIngredientVM> dishIngredients, ArrayList<IngredientVM> ingredients, DishState state) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dishIngredients = dishIngredients;
        this.ingredients = ingredients;
        this.listener = listener;
        this.state = state;
        SetupUnits();
    }

    private void SetupUnits() {
        units = new ArrayList<String>();
        units.add("г.");
        units.add("чашка");
        units.add("ч.л.");
        units.add("ст.л.");
        units.add("кг.");
        units.add("шт.");
        units.add("веточка");
        units.add("банка");
        units.add("бутылка");
        units.add("упаковка");
        units.add("стакан");
        units.add("горстка");
        units.add("ломтик");
        units.add("л.");
        units.add("мл.");
        units.add("щепот.");
        units.add("зуб.");
        units.add("вилок");
        units.add("пуч.");
        units.add("дол.");
    }

    @NonNull
    @Override
    public DishIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.dishpage_ingredient, viewGroup, false);
        return new DishIngredientsAdapter.ViewHolder(view, new IngredientNameTextListener(), new QuantityTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull DishIngredientsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.ingredientNameTextListener.updatePosition(i);
        viewHolder.quantityTextListener.updatePosition(i);
        viewHolder.bind(dishIngredients.get(i));
        viewHolder.changeView(dishIngredients.get(i), this.state);
        viewHolder.SetButtonClickListeners(dishIngredients.get(i));
    }

    @Override
    public int getItemCount() {
        return dishIngredients.size();
    }

    public ArrayList<DishIngredientVM> GetDishIngredients() {
        return this.dishIngredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton dishpage_ingredient_del_but;
        private AutoCompleteTextView dishpage_ingredient_name;
        private EditText dishpage_ingredient_quantity;
        private Spinner ingredient_unit;
        public IngredientNameTextListener ingredientNameTextListener;
        public QuantityTextListener quantityTextListener;
        private ArrayAdapter<IngredientVM> ingredients_to_searchAdapter;

        public ViewHolder(@NonNull View v, IngredientNameTextListener ingredientNameTextListener, QuantityTextListener quantityTextListener) {
            super(v);
            dishpage_ingredient_name = (AutoCompleteTextView) v.findViewById(R.id.dishpage_ingredient_name);
            ingredients_to_searchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ingredients);
            dishpage_ingredient_name.setAdapter(ingredients_to_searchAdapter);

            /*for (DishIngredientVM dishIngredient : dishIngredients) {
                for (IngredientVM ing : ingredients) {
                    if (dishIngredient.GetIngredientId() == ing.GetId()) {
                        dishIngredient.SetIngredientName(ing.GetName());
                        break;
                    }
                }
            }*/

            dishpage_ingredient_del_but = (ImageButton) v.findViewById(R.id.ingredient_delete_but);
            dishpage_ingredient_quantity = (EditText) v.findViewById(R.id.dishpage_ingredient_quantity);
            ingredient_unit = (Spinner) v.findViewById(R.id.ingredient_unit);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, units);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            ingredient_unit.setAdapter(spinnerAdapter);
            //for (DishIngredientVM dishIngredient : dishIngredients) {
                //ingredient_unit.setSelection(units.indexOf(dishIngredient.GetUnit()));
            //}

            this.ingredientNameTextListener = ingredientNameTextListener;
            dishpage_ingredient_name.addTextChangedListener(ingredientNameTextListener);
            this.quantityTextListener = quantityTextListener;
            dishpage_ingredient_quantity.addTextChangedListener(quantityTextListener);
        }

        public void changeView(DishIngredientVM dishIngredient, DishState state) {
            if (state != DishState.VIEW) {
                dishpage_ingredient_del_but.setVisibility(View.VISIBLE);
                dishpage_ingredient_name.setEnabled(true);
                dishpage_ingredient_quantity.setEnabled(true);
                ingredient_unit.setEnabled(true);
                ingredient_unit.setBackground(context.getDrawable(R.drawable.spinner_custom_style));
            }
            else {
                dishpage_ingredient_del_but.setVisibility(View.GONE);
                dishpage_ingredient_name.setEnabled(false);
                dishpage_ingredient_quantity.setEnabled(false);
                ingredient_unit.setEnabled(false);
                ingredient_unit.setBackground(null);
            }
        }

        public void bind(final DishIngredientVM dishIngredient) {
            ingredients_to_searchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ingredients);
            dishpage_ingredient_name.setAdapter(ingredients_to_searchAdapter);

            for (IngredientVM ing : ingredients) {
                if (dishIngredient.GetIngredientId() == ing.GetId()) {
                    dishIngredient.SetIngredientName(ing.GetName());
                    ingredients.remove(ing);
                    break;
                }
            }

            dishpage_ingredient_name.setText(dishIngredient.GetIngredientName());
            dishpage_ingredient_quantity.setText(String.valueOf(dishIngredient.GetQuantity()));
            dishpage_ingredient_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IngredientVM ing = (IngredientVM) parent.getItemAtPosition(position);
                    position = ingredients.indexOf(ing);
                    ingredients.remove(ing);
                    dishIngredient.SetIngredientId(ing.GetId());
                    dishIngredient.SetIngredientName(ing.GetName());
                }
            });
            ingredient_unit.setSelection(units.indexOf(dishIngredient.GetUnit()));
            ingredient_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected_item = (String) ingredient_unit.getSelectedItem();
                    dishIngredient.SetUnit(selected_item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public void SetButtonClickListeners(final DishIngredientVM dishIngredient) {
            dishpage_ingredient_del_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnDeleteIngredient(dishIngredient);
                }
            });
        }
    }

    private class IngredientNameTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            //dishIngredients.get(position).SetIngredientName(s.toString());
            if (!dishIngredients.get(position).GetIngredientName().equals(s.toString()) &&
            dishIngredients.get(position).GetIngredientId() > 0) {
                IngredientVM ing = new IngredientVM();
                ing.SetId(dishIngredients.get(position).GetIngredientId());
                ing.SetName(dishIngredients.get(position).GetIngredientName());
                ingredients.add(ing);
                dishIngredients.get(position).SetIngredientId(0);
                dishIngredients.get(position).SetIngredientName(s.toString());
            }
            if (dishIngredients.get(position).GetAction() != 1)
                dishIngredients.get(position).SetAction(2);
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }

    private class QuantityTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.length() > 0) {
                dishIngredients.get(position).SetQuantity(Float.parseFloat(s.toString()));
            }
            else {
                dishIngredients.get(position).SetQuantity(0);
            }
            if (dishIngredients.get(position).GetAction() != 1)
                dishIngredients.get(position).SetAction(2);
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }
}

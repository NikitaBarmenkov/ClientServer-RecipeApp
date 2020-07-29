package com.example.povarapp.MenuFragments.SearchFragment.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.povarapp.R;
import com.example.povarapp.DataVM.IngredientVM;

import java.util.ArrayList;

public class SearchIngredientsAdapter extends RecyclerView.Adapter<SearchIngredientsAdapter.ViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<IngredientVM> allingredients;
    private ArrayList<IngredientVM> currentingredients;
    private Context context;
    private OnIngredientListener listener;

    public interface OnIngredientListener {
        public void OnDeleteSearchIngredient(int position, IngredientVM ingredient);
        public void OnAddSearchIngredient(int position, IngredientVM ingredient);
    }

    public SearchIngredientsAdapter(Context context, OnIngredientListener listener, ArrayList<IngredientVM> currentingredients, ArrayList<IngredientVM> allingredients) {
        this.context = context;
        this.listener = listener;
        this.currentingredients = currentingredients;
        this.allingredients = allingredients;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SearchIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ingredient_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchIngredientsAdapter.ViewHolder holder, int position) {
        holder.bind(position, currentingredients.get(position));
        holder.ChangeView(currentingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return currentingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AutoCompleteTextView nameView;
        private ImageButton delBut;

        ArrayAdapter<IngredientVM> ingredients_to_searchAdapter;
        ViewHolder(View view){
            super(view);
            nameView = (AutoCompleteTextView) view.findViewById(R.id.ingredient_name_text);
            ingredients_to_searchAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allingredients);
            nameView.setAdapter(ingredients_to_searchAdapter);
            nameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IngredientVM ing = (IngredientVM) parent.getItemAtPosition(position);
                    position = allingredients.indexOf(ing);
                    listener.OnAddSearchIngredient(position, allingredients.get(position));
                    //nameView.dismissDropDown();
                }
            });

            delBut = (ImageButton) view.findViewById(R.id.ingredient_del_but);
        }

        public void bind(final int position, final IngredientVM i) {
            nameView.setText(i.GetName());
            ingredients_to_searchAdapter = new ArrayAdapter<IngredientVM>(context, android.R.layout.simple_list_item_1, allingredients);
            nameView.setAdapter(ingredients_to_searchAdapter);

            delBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnDeleteSearchIngredient(position, i);
                }
            });
        }

        public void ChangeView(IngredientVM i) {
            if (i.GetIsAdded()) {
                nameView.setEnabled(false);
                delBut.setVisibility(View.VISIBLE);
            }
            else {
                nameView.setEnabled(true);
                delBut.setVisibility(View.GONE);
            }
        }
    }
}

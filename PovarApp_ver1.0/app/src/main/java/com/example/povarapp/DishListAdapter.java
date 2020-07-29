package com.example.povarapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.povarapp.DataVM.DishVM;

import java.util.ArrayList;

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DishVM> dishes;
    private OnDishListener listener;

    public DishListAdapter (Context context, OnDishListener listener, ArrayList<DishVM> dishes) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dishes = dishes;
        this.listener = listener;
    }

    public void SetData(ArrayList<DishVM> dishes) {
        this.dishes = dishes;
        notifyDataSetChanged();
    }

    public interface OnDishListener {
        public void OnDishClick(DishVM dish);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.dish_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(dishes.get(i));
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ddish_name;
        private RatingBar ddish_rating;

        public ViewHolder(View v) {
            super(v);
            ddish_name = (TextView) v.findViewById(R.id.ddish_name);
            ddish_rating = (RatingBar) v.findViewById(R.id.ddish_rating);
        }

        public void bind(final DishVM dish) {
            ddish_name.setText(dish.GetName());
            ddish_rating.setRating((float)dish.GetRating());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnDishClick(dish);
                }
            });
        }
    }
}

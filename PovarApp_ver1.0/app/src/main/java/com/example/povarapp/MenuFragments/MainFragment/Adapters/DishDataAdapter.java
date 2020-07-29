package com.example.povarapp.MenuFragments.MainFragment.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.povarapp.R;
import com.example.povarapp.DataVM.DishVM;

import java.util.ArrayList;

public class DishDataAdapter extends RecyclerView.Adapter<DishDataAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onDishClick(DishVM dish);
    }

    private LayoutInflater inflater;
    private ArrayList<DishVM> dishes;
    private OnItemClickListener listener;
    Context context;

    public DishDataAdapter(Context context, OnItemClickListener listener, ArrayList<DishVM> dishes) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DishDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dish_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishDataAdapter.ViewHolder holder, int position) {
        holder.bind(dishes.get(position));
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.dish_image);
            nameView = (TextView) view.findViewById(R.id.dish_name);
            imageView.setClipToOutline(true);
        }

        public void bind(final DishVM dish) {
            nameView.setText(dish.GetName());
            imageView.setClipToOutline(true);
            //imageView.setImageBitmap(BitmapFactory.decodeByteArray(dish.GetImage(), 0, dish.GetImage().length));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onDishClick(dish);
                }
            });
        }
    }
}

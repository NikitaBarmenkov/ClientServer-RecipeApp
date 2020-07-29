package com.example.povarapp.MenuFragments.BuyListFragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.povarapp.DataVM.BuyItemVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.R;

import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<BuyItemVM> buyList;
    private CheckListener listener;

    public BuyListAdapter(Context context, CheckListener listener, List<BuyItemVM> buyList) {
        this.context = context;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.buyList = buyList;
    }

    public interface CheckListener {
        public void OnCheckBoxclick(BuyItemVM item, int position);
    }

    @NonNull
    @Override
    public BuyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.buy_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(buyList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ingName;
        private TextView ingQuantity;
        private CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            ingName = (TextView) v.findViewById(R.id.buy_list_ing_name);
            ingQuantity = (TextView) v.findViewById(R.id.buy_list_ing_quantity);
            checkBox = (CheckBox) v.findViewById(R.id.buy_list_ing_check);
        }

        public void bind(final BuyItemVM item, final int position) {
            ingName.setText(item.GetName());
            ingQuantity.setText(item.GetQuantity_unit());
            checkBox.setChecked(item.GetChecked());
            checkBox.setOnCheckedChangeListener(null);
            /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.SetChecked(isChecked);
                    listener.OnCheckBoxclick(item, position);
                }
            });*/
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.GetChecked()) {
                        checkBox.setChecked(false);
                        item.SetChecked(false);
                    }
                    else {
                        checkBox.setChecked(true);
                        item.SetChecked(true);
                    }
                    listener.OnCheckBoxclick(item, position);
                }
            });
        }
    }
}

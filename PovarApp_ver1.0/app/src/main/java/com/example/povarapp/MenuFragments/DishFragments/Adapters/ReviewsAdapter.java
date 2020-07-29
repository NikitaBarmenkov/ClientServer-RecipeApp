package com.example.povarapp.MenuFragments.DishFragments.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.povarapp.DataVM.ReviewVM;
import com.example.povarapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ReviewVM> reviews;

    public ReviewsAdapter(Context context, ArrayList<ReviewVM> reviews) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.review_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(reviews.get(i), i);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView review_author;
        private RatingBar review_rating;
        private TextView review_date;
        private TextView review_text;

        ViewHolder(View v) {
            super(v);
            review_author = (TextView) v.findViewById(R.id.review_author);
            review_rating = (RatingBar) v.findViewById(R.id.review_rating);
            review_date = (TextView) v.findViewById(R.id.review_date);
            review_text = (TextView) v.findViewById(R.id.review_text);
            review_author.setVisibility(View.GONE);
        }

        private String GetDateString(String str, Date date) {
            return str + String.format(Locale.getDefault(), "%td-%tm-%tY", date, date, date);
        }

        public void bind(ReviewVM review, int position) {
            //review_author.setText(review.GetUserName());
            review_rating.setRating((float)review.GetRating());
            review_date.setText(GetDateString("", review.GetDate()));
            if (review.GetComment().isEmpty()) {
                review_text.setVisibility(View.GONE);
            }
            else {
                review_text.setVisibility(View.VISIBLE);
                review_text.setText(review.GetComment());
            }
        }
    }
}

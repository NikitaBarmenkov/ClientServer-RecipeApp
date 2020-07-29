package com.example.povarapp.MenuFragments.DishFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.ReviewVM;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.MenuFragments.DishFragments.Adapters.ReviewsAdapter;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.CategoryVM;
import com.example.povarapp.DataVM.DishVM;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DishInfoFragment extends Fragment {

    private DishVM dish;
    private DishState dishState;
    private ArrayList<CategoryVM> categories;
    private AppUserVM user;
    private ArrayList<ReviewVM> reviews;

    private EditText dishpage_name;
    private Spinner dishpage_category;
    private TextView dishpage_author;
    private TextView dishpage_creation_date;
    private TextView dishpage_update_date;
    private EditText dishpage_kkal;
    private EditText dishpage_time;
    private TextView dishpage_rating;
    private Button dishpage_review_but;
    private LinearLayout dishpage_reviews;
    private RecyclerView rv_dish_reviews;
    private Switch dishpage_switch;
    private TextView switch_text;

    private IReview listener;
    private boolean isReviewExist;

    private String pictureImagePath = "";

    public DishInfoFragment() {}

    public interface IReview{
        public void OnReviewButClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_info_fragment, container, false);
        listener = (IReview) getActivity();
        GetBundle();
        InitViewWithData(view);
        ChangeView(dishState);
        return view;
    }

    private void InitViewWithData(View v) {
        //инициализация
        dishpage_name = (EditText) v.findViewById(R.id.dishpage_name);
        dishpage_category = (Spinner) v.findViewById(R.id.dishpage_category);
        dishpage_author = (TextView) v.findViewById(R.id.dishpage_author);
        dishpage_creation_date = (TextView) v.findViewById(R.id.dishpage_creation_date);
        dishpage_update_date = (TextView) v.findViewById(R.id.dishpage_update_date);
        dishpage_kkal = (EditText) v.findViewById(R.id.dishpage_kkal);
        dishpage_time = (EditText) v.findViewById(R.id.dishpage_time);
        dishpage_rating = (TextView) v.findViewById(R.id.dishpage_rating);
        dishpage_review_but = (Button) v.findViewById(R.id.dishpage_review_but);
        dishpage_review_but.setOnClickListener(dishpage_review_but_listener);
        dishpage_reviews = (LinearLayout) v.findViewById(R.id.dishpage_reviews);
        rv_dish_reviews = (RecyclerView) v.findViewById(R.id.rv_dish_reviews);
        dishpage_switch = (Switch) v.findViewById(R.id.dishpage_switch);
        switch_text = (TextView) v.findViewById(R.id.switch_text);
        if (isReviewExist) {//review is added to dish
            dishpage_review_but.setText("Изменить отзыв");
        }
        else {
            dishpage_review_but.setText("Оставить отзыв");
        }

        //данные
        dishpage_name.setText(dish.GetName());
        dishpage_author.setText("от " + dish.GetUserName());
        if (dishState == DishState.VIEW) {
            dishpage_creation_date.setText(GetDateString("Создано: ", dish.GetDate()));
            dishpage_update_date.setText(GetDateString("Обновлено: ", dish.GetUpdate_date()));
            dishpage_rating.setText(String.valueOf(dish.GetRating()));
        }
        dishpage_kkal.setText(String.valueOf(dish.GetKkal()));
        dishpage_time.setText(String.valueOf(dish.GetCookTime()));
        dishpage_switch.setChecked(dish.GetIsPublic());
        InitRvreviews();
        SetupSpinner();
    }

    private String GetDateString(String str, Date date) {
        return str + String.format(Locale.getDefault(), "%td-%tm-%tY", date, date, date);
    }

    private void InitRvreviews() {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter((Context) getActivity(), reviews);
        rv_dish_reviews.setAdapter(reviewsAdapter);
    }

    private void SetupSpinner() {
        ArrayAdapter<CategoryVM> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        dishpage_category.setAdapter(spinnerAdapter);
        for (int i = 0; i < categories.size(); i++) {
            if (dish.GetCategoryId() == categories.get(i).GetId()) {
                dishpage_category.setSelection(i);
            }
        }
        //TODO создать своё отображение элемента спиннера
        /*dishpage_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    private void GetBundle() {
        categories = new ArrayList<>();
        reviews = new ArrayList<>();
        try {
            Bundle args = getArguments();
            dishState = (DishState)args.getSerializable("dishState");
            dish = (DishVM) args.getSerializable("dish");
            user = (AppUserVM) args.getSerializable("user");
            categories = (ArrayList<CategoryVM>) args.getSerializable("categories");
            reviews = (ArrayList<ReviewVM>) args.getSerializable("reviews");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ChangeReviewButName(boolean isReviewExist) {
        this.isReviewExist = isReviewExist;
    }

    public DishVM GetDataFromFragment() {
        dish.SetName(dishpage_name.getText().toString());
        CategoryVM category = (CategoryVM) dishpage_category.getSelectedItem();
        dish.SetCategoryId(category.GetId());
        dish.SetCategoryName(category.GetName());
        try {
            dish.SetKkal(Double.parseDouble(dishpage_kkal.getText().toString()));
            dish.SetCookTime(Double.parseDouble(dishpage_time.getText().toString()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        dish.SetIsPublic(dishpage_switch.isChecked());
        return dish;
    }

    public void ChangeView(DishState dishState) {
        try {
            Bundle args = this.getArguments();
            args.putSerializable("dishState", dishState);
            this.setArguments(args);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        switch(dishState) {
            case EDIT:
            case ADD:
                dishpage_author.setVisibility(View.GONE);
                dishpage_creation_date.setVisibility(View.GONE);
                dishpage_update_date.setVisibility(View.GONE);
                dishpage_name.setEnabled(true);
                dishpage_category.setEnabled(true);
                dishpage_category.setBackground(getContext().getDrawable(R.drawable.spinner_custom_style));
                dishpage_kkal.setEnabled(true);
                dishpage_time.setEnabled(true);
                dishpage_rating.setVisibility(View.GONE);

                dishpage_review_but.setVisibility(View.GONE);
                dishpage_reviews.setVisibility(View.GONE);

                dishpage_switch.setVisibility(View.VISIBLE);
                switch_text.setVisibility(View.VISIBLE);
                break;
            case VIEW:
                dishpage_author.setVisibility(View.VISIBLE);
                dishpage_creation_date.setVisibility(View.VISIBLE);
                dishpage_update_date.setVisibility(View.VISIBLE);
                dishpage_name.setEnabled(false);
                dishpage_category.setEnabled(false);
                dishpage_category.setBackground(null);
                dishpage_kkal.setEnabled(false);
                dishpage_time.setEnabled(false);
                dishpage_rating.setVisibility(View.VISIBLE);
                dishpage_review_but.setVisibility(View.VISIBLE);
                dishpage_reviews.setVisibility(View.VISIBLE);
                dishpage_switch.setVisibility(View.GONE);
                switch_text.setVisibility(View.GONE);
                break;
        }
    }

    private View.OnClickListener dishpage_review_but_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.OnReviewButClick();
        }
    };
}

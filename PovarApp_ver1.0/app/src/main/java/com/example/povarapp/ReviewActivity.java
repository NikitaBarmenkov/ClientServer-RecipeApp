package com.example.povarapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.FavVM;
import com.example.povarapp.DataVM.ReviewVM;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReviewActivity extends AppCompatActivity {
    private RatingBar review_rating;
    private EditText review_text;
    private Button review_save_but;
    private Button review_del_but;

    private ReviewVM review;
    private AppUserVM user;
    private DishVM dish;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.review_toolbar);
        review_rating = (RatingBar) findViewById(R.id.review_rating);
        review_text = (EditText) findViewById(R.id.review_text);
        review_save_but = (Button) findViewById(R.id.review_save_but);
        review_del_but = (Button) findViewById(R.id.review_del_but);

        review_rating.setRating((float)review.GetRating());
        review_text.setText(review.GetComment());
        if (review.GetId() > 0) {
            review_del_but.setVisibility(View.VISIBLE);
        }
        else review_del_but.setVisibility(View.GONE);
        review.SetUserName(user.GetName());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        review_save_but.setOnClickListener(saveButListener);
        review_del_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest();
                String json = "{\"id\":" + String.valueOf(review.GetId()) + "}";
                deleteReviewRequest.execute("http://192.168.1.45:3030/deletereview", json);
            }
        });
        review_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                review_rating.setRating(rating);
                review.SetRating(rating);
            }
        });
    }

    private void SetReviewData() {
        review.SetRating(review_rating.getRating());
        review.SetComment(review_text.getText().toString());
        review.SetDishId(dish.GetId());
        review.SetUserId(user.GetId());
    }

    View.OnClickListener saveButListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SetReviewData();
            if (user.GetId() > 0) {
                if (ValidateReviewData(review)) {
                    if (review.GetId() > 0) {
                        //edit review
                        SaveReviewRequest saveReviewRequest = new SaveReviewRequest();
                        String json = new Gson().toJson(review);
                        saveReviewRequest.execute("http://192.168.1.45:3030/updatereview", json);
                    } else {
                        //save new review
                        AddReviewRequest addReviewRequest = new AddReviewRequest();
                        String json = new Gson().toJson(review);
                        addReviewRequest.execute("http://192.168.1.45:3030/createreview", json);
                    }
                } else {
                    ShowMessage("Вы не указали рейтинг!");
                }
            }
            else ShowMessage("Вы не авторизованы!");
        }
    };

    private void ShowMessage(String mess) {
        SuperActivityToast.create(this, new Style(), Style.TYPE_BUTTON)
                .setText(mess)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    public boolean ValidateReviewData(ReviewVM review) {
        if (review.GetDishId() < 1 || review.GetUserId() < 1) return false;
        if (review.GetRating() < 1 || review.GetRating() > 5)
            return false;
        else return true;
    }

    private void getData() {
        review = new ReviewVM();
        dish = new DishVM();
        user = new AppUserVM();
        try {
            Bundle args = getIntent().getExtras();
            dish = (DishVM) args.getSerializable("dish");
            user = (AppUserVM) args.getSerializable("user");
            review = (ReviewVM) args.getSerializable("review");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class AddReviewRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AddReviewRequest() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(String... args) {
            try {
                RequestBody body = RequestBody.create(args[1], JSON);
                Request request = new Request.Builder()
                        .url(args[0])
                        .post(body)
                        .build();

                return client.newCall(request).execute();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null) return;
            if (response.isSuccessful()) {
                try {
                    String f = response.body().string();
                    String f1 = f.substring(1, f.length()-1);
                    f = f1.replace("\\", "");
                    review = new Gson().fromJson(f, ReviewVM.class);
                    FinishActivity(RESULT_OK);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class SaveReviewRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public SaveReviewRequest() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(String... args) {
            try {
                RequestBody body = RequestBody.create(args[1], JSON);
                Request request = new Request.Builder()
                        .url(args[0])
                        .post(body)
                        .build();

                return client.newCall(request).execute();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isSuccessful()) {
                try {
                    String f = response.body().string();
                    String f1 = f.substring(1, f.length()-1);
                    f = f1.replace("\\", "");
                    ArrayList<ReviewVM> reviews = new Gson().fromJson(f, new TypeToken<ArrayList<ReviewVM>>() {}.getType());
                    if (reviews.size() > 0) {
                        review = reviews.get(0);
                    }
                    else {
                        review = new ReviewVM();
                    }
                    FinishActivity(RESULT_OK);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private void FinishActivity(int res) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("review", review);
        setResult(res, resultIntent);
        onBackPressed();
    }

    class DeleteReviewRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DeleteReviewRequest() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(String... args) {
            try {
                RequestBody body = RequestBody.create(args[1], JSON);
                Request request = new Request.Builder()
                        .url(args[0])
                        .post(body)
                        .build();

                return client.newCall(request).execute();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isSuccessful()) {
                review = new ReviewVM();
                FinishActivity(RESULT_OK);
            }
        }
    }
}

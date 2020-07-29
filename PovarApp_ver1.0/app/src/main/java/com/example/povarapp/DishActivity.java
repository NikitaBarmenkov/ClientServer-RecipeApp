package com.example.povarapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.FavVM;
import com.example.povarapp.DataVM.ReviewVM;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.MenuFragments.DishFragments.DishInfoFragment;
import com.example.povarapp.MenuFragments.DishFragments.DishIngredientsFragment;
import com.example.povarapp.MenuFragments.DishFragments.DishRecipeFragment;
import com.example.povarapp.DataVM.CategoryVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.DishIngredientVM;
import com.example.povarapp.DataVM.IngredientVM;
import com.example.povarapp.DataVM.RecipeStepVM;
import com.example.povarapp.MenuFragments.DishFragments.Adapters.DishTabsPagerAdapter;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DishActivity extends AppCompatActivity implements DishInfoFragment.IReview {

    private DishInfoFragment dishInfoFragment;
    private DishRecipeFragment dishRecipeFragment;
    private DishIngredientsFragment dishIngredientsFragment;

    private DishVM dish;
    private ArrayList<RecipeStepVM> recipe;
    private ArrayList<DishIngredientVM> dishIngredients;
    private DishState dishState;
    private ArrayList<CategoryVM> categories;
    private ArrayList<IngredientVM> ingredients;
    private Instances instances;
    private AppUserVM user;
    private FavVM fav;
    private ReviewVM review;
    private ArrayList<ReviewVM> reviews;

    private ImageButton dish_fav;
    //private ImageButton back_but;
    private ImageButton delete_but;
    private ImageButton edit_but;;
    private ImageButton save_but;

    private ViewPager dishTabsViewPager;
    private TabLayout tabs;
    private Toolbar toolbar;

    //private DBRepository db;

    private boolean isInFav = false;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        SetupAppbarButtons();
        dishTabsViewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.dish_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishState != DishState.EDIT) {
                    onBackPressed();
                }
                else {
                    dishState = DishState.VIEW;
                    ChangeAppBarView(dishState);
                    dishInfoFragment.ChangeView(dishState);
                    dishRecipeFragment.ChangeView(dishState);
                    dishIngredientsFragment.ChangeView(dishState);
                    AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
                    allCategoriesRequest.execute("http://192.168.1.45:3030/categories");
                    //String json = "{\"id\":"+ String.valueOf(dish.GetId()) +"}";
                    //new DishRequest().execute("http://192.168.1.45:3030/dish", json);
                }
            }
        });
        dishInfoFragment = new DishInfoFragment();
        dishRecipeFragment = new DishRecipeFragment();
        dishIngredientsFragment = new DishIngredientsFragment();
        getData();

        ChangeAppBarView(dishState);
    }

    private void SetBundle() {
        Bundle args = new Bundle();
        args.putSerializable("dish", dish);
        args.putSerializable("dishState", dishState);
        args.putSerializable("categories", categories);
        args.putSerializable("ingredients", ingredients);
        args.putSerializable("reviews", reviews);
        args.putSerializable("user", user);
        dishInfoFragment.setArguments(args);
        dishRecipeFragment.setArguments(args);
        dishIngredientsFragment.setArguments(args);
    }

    private void SetupAppbarButtons() {
        dish_fav = (ImageButton) findViewById(R.id.dish_fav);
        //back_but = (ImageButton) findViewById(R.id.dish_back);
        edit_but = (ImageButton) findViewById(R.id.dish_edit);
        delete_but = (ImageButton) findViewById(R.id.dish_delete);
        save_but = (ImageButton) findViewById(R.id.dish_save);
        dish_fav.setOnClickListener(appBarListener);
        //back_but.setOnClickListener(appBarListener);
        edit_but.setOnClickListener(appBarListener);
        delete_but.setOnClickListener(appBarListener);
        save_but.setOnClickListener(appBarListener);
    }

    private void setupDishTabsViewPager() {
        DishTabsPagerAdapter adapter = new DishTabsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(dishInfoFragment, "ONE");
        adapter.addFragment(dishRecipeFragment, "TWO");
        adapter.addFragment(dishIngredientsFragment, "THREE");
        dishTabsViewPager.setAdapter(adapter);
        tabs.setupWithViewPager(dishTabsViewPager);
    }

    private void getData() {
        instances = new Instances();
        recipe = new ArrayList<>();
        dishIngredients = new ArrayList<>();
        reviews = new ArrayList<>();
        fav = new FavVM();
        review = new ReviewVM();
        try {
            Bundle arguments = getIntent().getExtras();
            dish = (DishVM) arguments.getSerializable("dish");
            dishState = (DishState) arguments.getSerializable("dishState");
            user = (AppUserVM) arguments.getSerializable("user");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        ingredients = new ArrayList<>();
        categories = new ArrayList<>();

        AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
        allCategoriesRequest.execute("http://192.168.1.45:3030/categories");
    }

    public void ChangeAppBarView(DishState dishState) {
        switch(dishState) {
            case VIEW:
                //back_but.setVisibility(View.VISIBLE);
                save_but.setVisibility(View.GONE);
                dish_fav.setVisibility(View.VISIBLE);
                if (user.GetId() == dish.GetUserId()) {
                    edit_but.setVisibility(View.VISIBLE);
                    delete_but.setVisibility(View.VISIBLE);
                }
                else {
                    edit_but.setVisibility(View.GONE);
                    delete_but.setVisibility(View.GONE);
                }
                break;
            case EDIT:
            case ADD:
                dish_fav.setVisibility(View.GONE);
                //back_but.setVisibility(View.VISIBLE);
                edit_but.setVisibility(View.GONE);
                delete_but.setVisibility(View.GONE);
                save_but.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void set_fav_but_state() {
        Drawable new_image;
        if (isInFav) {
            new_image = getDrawable(R.mipmap.heart);
            dish_fav.setImageDrawable(new_image);
        }
        else {
            new_image = getDrawable(R.mipmap.heart_outline);
            dish_fav.setImageDrawable(new_image);
        }
    }
    private void showMessage(String message) {
        SuperActivityToast.create(this, new Style(), Style.TYPE_BUTTON)
                .setText(message)
                .setDuration(Style.DURATION_VERY_SHORT)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(getResources().getColor(R.color.colorPrimary, null))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    public View.OnClickListener appBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.dish_fav:
                    if (user.GetId() <= 0) {
                        showMessage("Пожалуйста авторизируйтесь!");
                        break;
                    }
                    if (isInFav) {
                        DeleteFavRequest deleteFavRequest = new DeleteFavRequest();
                        String json = "{\"id\":"+ fav.GetId() + "}";
                        deleteFavRequest.execute("http://192.168.1.45:3030/deletefav", json);
                    }
                    else {
                        AddFavRequest saveFavRequest = new AddFavRequest();
                        String json = "{\"UserId\":"+ user.GetId() + "," + "\"DishId\":" + dish.GetId() + "}";
                        saveFavRequest.execute("http://192.168.1.45:3030/createfav", json);
                    }
                    break;
                case R.id.dish_edit:
                    dishState = DishState.EDIT;
                    ChangeAppBarView(dishState);
                    dishInfoFragment.ChangeView(dishState);
                    dishRecipeFragment.ChangeView(dishState);
                    dishIngredientsFragment.ChangeView(dishState);
                    break;
                case R.id.dish_delete:
                    DeleteDishRequest deleteDishRequest = new DeleteDishRequest();
                    String json = "{\"id\":"+ dish.GetId() + "}";
                    deleteDishRequest.execute("http://192.168.1.45:3030/deletedish", json);
                    //navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
                    break;
                case R.id.dish_save:
                    dish = dishInfoFragment.GetDataFromFragment();
                    if (!dishRecipeFragment.isInitialized) {
                        dish.SetRecipeSteps(dish.GetRecipeSteps());
                    }
                    else {
                        dish.SetRecipeSteps(dishRecipeFragment.GetDataFromFragment());
                    }
                    if (!dishIngredientsFragment.isInitialized) {
                        dish.SetDishIngredients(dish.GetDishIngredients());
                    }
                    else {
                        dish.SetDishIngredients(dishIngredientsFragment.GetDataFromFragment());
                    }
                    if (checkDish(dish)) {
                        if (dish.GetId() <= 0) {
                            AddDishRequest addDishRequest = new AddDishRequest();
                            addDishRequest.execute("http://192.168.1.45:3030/createdish", new Gson().toJson(dish));
                        }
                        else {
                            UpdateDishRequest updateDishRequest = new UpdateDishRequest();
                            updateDishRequest.execute("http://192.168.1.45:3030/updatedish", new Gson().toJson(dish));
                        }
                    }
                    break;
            }
        }
    };

    public boolean checkDish(DishVM dish) {
        if (dish.GetName().isEmpty() || dish.GetCategoryId() <= 0) {
            showMessage("Не указано название блюда");
            return false;
        }
        if (dish.GetRecipeSteps().isEmpty() || dish.GetDishIngredients().isEmpty()) {
            showMessage("Ошибка! Нет рецепта или ингредиентов!");
            return false;
        }
        for (RecipeStepVM step : dish.GetRecipeSteps()) {
            if (step.GetText().isEmpty()) {
                showMessage("Ошибка! Пустой шаг!");
                return false;
            }
        }
        for (DishIngredientVM ingredient : dish.GetDishIngredients()) {
            if (ingredient.GetIngredientId() <= 0) {
                showMessage("Ингредиент " + ingredient.GetIngredientName() + " не существует!");
                return false;
            }
            if (ingredient.GetUnit().isEmpty() || ingredient.GetQuantity() <= 0) {
                showMessage("Ошибка! Пустой ингредиент");
                return false;
            }
        }
        return true;
    }

    public void AddToBuyList(List<DishIngredientVM> list) {
        if (user.GetId() > 0) {
            AddToBuyListRequest addToBuyListRequest = new AddToBuyListRequest();
            String json = "{\"UserId\":" + String.valueOf(user.GetId()) + "," + "\"items\":" + new Gson().toJson(list) + "}";
            addToBuyListRequest.execute("http://192.168.1.45:3030/addtobuylist", json);
        }
        else {
            showMessage("Вы не авторизовались!");
        }
    }

    @Override
    public void OnReviewButClick() {
        if (user.GetId() > 0) {
            Intent myIntent = new Intent(this, ReviewActivity.class);
            myIntent.putExtra("dish", dish);
            myIntent.putExtra("user", user);
            myIntent.putExtra("review", review);
            startActivityForResult(myIntent, 1);
        }
        else {
            showMessage("Вы не авторизованы!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    review = (ReviewVM) data.getSerializableExtra("review");
                } else if (requestCode == RESULT_CANCELED) {
                    review = new ReviewVM();
                }
                if (review.GetId() > 0) {
                    dishInfoFragment.ChangeReviewButName(true);
                }
                else dishInfoFragment.ChangeReviewButName(false);
                DishRequest dishRequest = new DishRequest();
                String json = "{\"id\":" + String.valueOf(dish.GetId()) + "}";
                dishRequest.execute("http://192.168.1.45:3030/dish", json);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class AllCategoriesRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AllCategoriesRequest() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(String... args) {
            try {
                Request request = new Request.Builder()
                        .url(args[0])
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
                    categories = new Gson().fromJson(f, new TypeToken<ArrayList<CategoryVM>>() {}.getType());//JSON to JAVA
                    AllIngredientsRequest allIngredientsRequest = new AllIngredientsRequest();
                    allIngredientsRequest.execute("http://192.168.1.45:3030/ingredients");
                    //dishInfoFragment.SetCategories(categories);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class AllIngredientsRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AllIngredientsRequest() {
            client = new OkHttpClient();
        }

        @Override
        protected Response doInBackground(String... args) {
            try {
                Request request = new Request.Builder()
                        .url(args[0])
                        .build();

                Response response = client.newCall(request).execute();
                return response;
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
                    ingredients = new Gson().fromJson(f, new TypeToken<ArrayList<IngredientVM>>() {}.getType());//JSON to JAVA
                    FavRequest favRequest = new FavRequest();
                    String json = "{\"UserId\":"+ user.GetId() + "," + "\"DishId\":" + dish.GetId() + "}";
                    favRequest.execute("http://192.168.1.45:3030/userfavfordish", json);
                    //dishIngredientsFragment.SetIngredients(ingredients);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }


    //блюдо

    class DishRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DishRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    String f1 = f.substring(1, f.length() - 1);
                    f = f1.replace("\\", "");
                    dish = new Gson().fromJson(f, DishVM.class);//JSON to JAVA
                    new DishRecipeRequest().execute("http://192.168.1.45:3030/dishrecipes", "{\"id\":" + String.valueOf(dish.GetId()) + "}");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DishReviewsRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DishReviewsRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    ArrayList<ReviewVM> reviewVMS = new Gson().fromJson(f, new TypeToken<ArrayList<ReviewVM>>() {}.getType());//JSON to JAVA
                    reviews = reviewVMS;
                    dish.SetReviews(reviewVMS);
                    //конец информации по блюду
                    new DishAuthorRequest().execute("http://192.168.1.45:3030/dishauthor", "{\"id\":" + String.valueOf(dish.GetUserId()) + "}");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DishAuthorRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DishAuthorRequest() {
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
                    dish.SetUserName(response.body().string());
                    //конец информации по блюду
                    dish.dishIngredientsToDelete = new ArrayList<>();
                    dish.recipeStepsToDelete = new ArrayList<>();
                    SetBundle();
                    setupDishTabsViewPager();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DishingredientsRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DishingredientsRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    ArrayList<DishIngredientVM> ings = new Gson().fromJson(f, new TypeToken<ArrayList<DishIngredientVM>>() {}.getType());//JSON to JAVA
                    dish.SetDishIngredients(ings);
                    new DishReviewsRequest().execute("http://192.168.1.45:3030/dishreviews", "{\"id\":" + String.valueOf(dish.GetId()) + "}");
                    //dishIngredientsFragment.SetDishIngredients(ings);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class DishRecipeRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DishRecipeRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    ArrayList<RecipeStepVM> steps = new Gson().fromJson(f, new TypeToken<ArrayList<RecipeStepVM>>() {}.getType());//JSON to JAVA
                    dish.SetRecipeSteps(steps);
                    new DishingredientsRequest().execute("http://192.168.1.45:3030/dishingredients", "{\"id\":" + String.valueOf(dish.GetId()) + "}");
                    //dishRecipeFragment.SetRecipes(steps);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    //блюдо


    class FavRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public FavRequest() {
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
                    ArrayList<FavVM> favs = new Gson().fromJson(f, new TypeToken<ArrayList<FavVM>>() {}.getType());
                    if (favs.size() > 0) {
                        fav = favs.get(0);
                        isInFav = true;
                    }
                    else {
                        fav = new FavVM();
                        isInFav = false;
                    }
                    set_fav_but_state();
                    ReviewRequest reviewRequest = new ReviewRequest();
                    String json = "{\"UserId\":"+ user.GetId() + "," + "\"DishId\":" + dish.GetId() + "}";
                    reviewRequest.execute("http://192.168.1.45:3030/userdishreview", json);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class AddFavRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AddFavRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    FavVM favs = new Gson().fromJson(f, FavVM.class);
                    fav = favs;
                    isInFav = true;
                    set_fav_but_state();
                    showMessage("Добавлено в избранные");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DeleteFavRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DeleteFavRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                fav = new FavVM();
                isInFav = false;
                set_fav_but_state();
                showMessage("Удалено из избранных");
            }

        }
    }

    class ReviewRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public ReviewRequest() {
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
                    ArrayList<ReviewVM> reviews = new Gson().fromJson(f, new TypeToken<ArrayList<ReviewVM>>() {}.getType());
                    if (reviews.size() > 0) {
                        review = reviews.get(0);
                        dishInfoFragment.ChangeReviewButName(true);
                    }
                    else {
                        review = new ReviewVM();
                        review.SetRating(1);
                        review.SetComment("");
                        dishInfoFragment.ChangeReviewButName(false);
                    }
                    if (dishState == DishState.ADD) {
                        dish.SetUserId(user.GetId());
                        dish.SetUserName(user.GetName());
                        SetBundle();
                        setupDishTabsViewPager();
                    }
                    else {
                        DishRequest dishRequest = new DishRequest();
                        String json = "{\"id\":" + String.valueOf(dish.GetId()) + "}";
                        dishRequest.execute("http://192.168.1.45:3030/dish", json);
                        //конец остальной информации
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class AddDishRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AddDishRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                    dish = new DishVM();

                    //save image to local db
                    //db.dishImageRepository.CreateItem(dish);

                    ArrayList<DishVM> result = new Gson().fromJson(f, new TypeToken<ArrayList<DishVM>>() {}.getType());
                    if (result.size() > 0) {
                        dish = result.get(0);
                    }

                    //get image bytes back
                    //dish.SetImage(db.dishImageRepository.GetImageByDishId(dish.GetId()).GetImage());

                    AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
                    allCategoriesRequest.execute("http://192.168.1.45:3030/categories");

                    dishState = DishState.VIEW;
                    ChangeAppBarView(dishState);
                    dishInfoFragment.ChangeView(dishState);
                    dishRecipeFragment.ChangeView(dishState);
                    dishIngredientsFragment.ChangeView(dishState);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class UpdateDishRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public UpdateDishRequest() {
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
                    AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
                    allCategoriesRequest.execute("http://192.168.1.45:3030/categories");

                    dishState = DishState.VIEW;
                    ChangeAppBarView(dishState);
                    dishInfoFragment.ChangeView(dishState);
                    dishRecipeFragment.ChangeView(dishState);
                    dishIngredientsFragment.ChangeView(dishState);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DeleteDishRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DeleteDishRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
            if (!response.isSuccessful()) {
                showMessage("Удаление не прошло!");
            }
            else {
                finish();
            }
        }
    }

    class AddToBuyListRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public AddToBuyListRequest() {
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

                Response response = client.newCall(request).execute();
                return response;
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
                showMessage("Ингредиенты добавлены в список покупок!");
            }
            else {
                showMessage("Произошла ошибка! Ингредиенты не добавлены!");
            }
        }
    }
}
package com.example.povarapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.CategoryVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.Enums.DishState;
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

public class CategoriesActivity extends AppCompatActivity implements DishListAdapter.OnDishListener {

    private RecyclerView rv_dish_list;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DishVM> dishes;
    private AppUserVM user;
    private CategoryVM category;
    private Toolbar toolbar;

    private SwipeRefreshLayout dish_list_swipe_layout;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        toolbar = (Toolbar) findViewById(R.id.categorydishes_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rv_dish_list = (RecyclerView) findViewById(R.id.rv_dish_list);
        GetData();
        toolbar.setTitle(category.GetName());
        dish_list_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.dish_list_swipe_layout);
        dish_list_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String json = "{\"id\":" + category.GetId() + "}";
                new CategoryDishesRequest().execute("http://192.168.1.45:3030/categorydishes", json);
            }
        });
    }

    private void SetData() {
        DishListAdapter adapter = new DishListAdapter(this, this, dishes);
        //rv_dish_list.setLayoutManager(layoutManager);
        rv_dish_list.setAdapter(adapter);
    }

    private void GetData() {
        try {
            category = new CategoryVM();
            user = new AppUserVM();
            Bundle args = getIntent().getExtras();
            user = (AppUserVM) args.getSerializable("user");
            category = (CategoryVM) args.getSerializable("category");
            dishes = new ArrayList<>();
            if (category.GetId() > 0) {
                String json = "{\"id\":" + category.GetId() + "}";
                new CategoryDishesRequest().execute("http://192.168.1.45:3030/categorydishes", json);
            }
            else {
                ShowMessage("Ошибка!");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    private void ShowMessage(String str) {
        SuperActivityToast.create(this, new Style(), Style.TYPE_BUTTON)
                .setText(str)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    @Override
    public void OnDishClick(DishVM dish) {
        Intent myIntent = new Intent(this, DishActivity.class);
        myIntent.putExtra("dish", dish);
        myIntent.putExtra("dishState", DishState.VIEW);
        myIntent.putExtra("user", user);
        this.startActivity(myIntent);
    }

    class CategoryDishesRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public CategoryDishesRequest() {
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
                    ArrayList<DishVM> result = new Gson().fromJson(f, new TypeToken<ArrayList<DishVM>>() {}.getType());
                    if (result.size() > 0) {
                        dishes = result;
                        SetData();
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                dish_list_swipe_layout.setRefreshing(false);
            }
            else {
                ShowMessage("Что-то пошло не так!");
            }
        }
    }
}

package com.example.povarapp.MenuFragments.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.Enums.*;
import com.example.povarapp.MenuFragments.MainFragment.Adapters.CategoryDataAdapter;
import com.example.povarapp.MenuFragments.MainFragment.Adapters.DishDataAdapter;
import com.example.povarapp.DishActivity;
import com.example.povarapp.DataVM.CategoryVM;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.DishVM;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFragment extends Fragment implements DishDataAdapter.OnItemClickListener {
    private RecyclerView rv_categories;
    private LinearLayoutManager layoutManager;
    private Context context;
    private ArrayList<CategoryVM> categories;
    private AppUserVM user;

    private SwipeRefreshLayout main_swipe_layout;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_fragment, container, false);
        try {
            Bundle args = getArguments();
            user = (AppUserVM) args.getSerializable("user");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            user = new AppUserVM();
        }
        GetData();
        InitView(view);
        return view;
    }

    private void GetData() {
        categories = new ArrayList<>();
        AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
        allCategoriesRequest.execute("http://192.168.1.45:3030/categories");
    }

    public void InitView(View v) {
        rv_categories = (RecyclerView) v.findViewById(R.id.rv_categories);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_categories.setLayoutManager(layoutManager);

        main_swipe_layout = (SwipeRefreshLayout) v.findViewById(R.id.main_swipe_layout);
        main_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AllCategoriesRequest allCategoriesRequest = new AllCategoriesRequest();
                allCategoriesRequest.execute("http://192.168.1.45:3030/categories");
            }
        });
    }

    private void SetData() {
        CategoryDataAdapter adapter = new CategoryDataAdapter(context, this, categories, user);
        rv_categories.setAdapter(adapter);
    }

    @Override
    public void onDishClick(DishVM dish) {
        if(dish == null) {
            Toast.makeText(context,"dish is null",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent myIntent = new Intent(context, DishActivity.class);
            myIntent.putExtra("dish", dish);
            myIntent.putExtra("dishState", DishState.VIEW);
            myIntent.putExtra("user", this.user);
            context.startActivity(myIntent);
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
            if (response == null) return;
            if (response.isSuccessful()) {
                try {
                    String f = response.body().string();
                    String f1 = f.substring(1, f.length()-1);
                    f = f1.replace("\\", "");
                    categories = new Gson().fromJson(f, new TypeToken<ArrayList<CategoryVM>>() {}.getType());//JSON to JAVA
                    SetData();
                    main_swipe_layout.setRefreshing(false);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

package com.example.povarapp.MenuFragments.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DishListAdapter;
import com.example.povarapp.Enums.*;
import com.example.povarapp.Instances;
import com.example.povarapp.MenuFragments.SearchFragment.Adapters.SearchIngredientsAdapter;
import com.example.povarapp.DishActivity;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.IngredientVM;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFragment extends Fragment implements SearchIngredientsAdapter.OnIngredientListener, DishListAdapter.OnDishListener {
    private RecyclerView rv_ingredients_to_search;
    private RecyclerView rv_search_list;
    private Context context;
    private SearchIngredientsAdapter searchIngredientsAdapter;
    private DishListAdapter searchResultsAdapter;

    private ArrayList<IngredientVM> currentingredients;
    private ArrayList<IngredientVM> allingredients;
    private ArrayList<DishVM> dishes;
    private String search_name;
    private Instances instances;
    private MainState state;
    private AppUserVM user;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        instances = new Instances();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        try {
            Bundle args = getArguments();
            user = (AppUserVM) args.getSerializable("user");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        GetData();
        InitViewWithData(view);
        return view;
    }

    private void GetData() {
        dishes = new ArrayList<>();
        /*AllDishesRequest allDishesRequest = new AllDishesRequest();
        allDishesRequest.execute("http://192.168.1.45:3030/dishes");*/

        allingredients = new ArrayList<>();
        AllIngredientsRequest allIngredientsRequest = new AllIngredientsRequest();
        allIngredientsRequest.execute("http://192.168.1.45:3030/ingredients");

        currentingredients = new ArrayList<>();
        currentingredients.add(instances.newIngredientInstance());
        search_name = "";
    }

    private void InitViewWithData(View v) {
        rv_ingredients_to_search = (RecyclerView) v.findViewById(R.id.rv_ingredients_to_search);
        rv_search_list = (RecyclerView) v.findViewById(R.id.rv_search_list);
    }

    private void InitIngredientsSearch() {
        searchIngredientsAdapter = new SearchIngredientsAdapter(context, this, currentingredients, allingredients);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(context);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.FLEX_START);
        manager.setAlignItems(AlignItems.FLEX_START);
        //manager.setAlignContent(AlignContent.FLEX_START);
        rv_ingredients_to_search.setLayoutManager(manager);
        rv_ingredients_to_search.setAdapter(searchIngredientsAdapter);
        searchIngredientsAdapter.notifyDataSetChanged();
    }

    private void InitSearchresultsAdapter() {
        searchResultsAdapter = new DishListAdapter(context, this, dishes);
        rv_search_list.setAdapter(searchResultsAdapter);
        //makeSearch();
        //searchResultsAdapter.getMyFilter(currentingredients, search_name);

    }
    private void makeSearch() {
        SearchRequest searchRequest = new SearchRequest();
        /*if (search_name == "")
        {
            search_name = "0";
        }*/

        String json = "{\"text\":\"" + search_name + "\", " + "\"ingredients\":" + new Gson().toJson(currentingredients) + "}";
        searchRequest.execute("http://192.168.1.45:3030/searchdishes", json);
    }

    public void OnSearchTextChanged(String text) {
        search_name = text;
        makeSearch();
        //searchResultsAdapter.getMyFilter(currentingredients, text);
        searchResultsAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDeleteSearchIngredient(int position, IngredientVM ingredient) {
        currentingredients.remove(ingredient);
        makeSearch();
        if (currentingredients.isEmpty()) {
            currentingredients.add(instances.newIngredientInstance());
        }
        allingredients.add(ingredient);
        searchIngredientsAdapter.notifyItemRemoved(position);
        searchIngredientsAdapter.notifyDataSetChanged();
        //makeSearch();
    }

    @Override
    public void OnAddSearchIngredient(int position, IngredientVM ingredient) {
        ingredient.SetAdded(true);
        currentingredients.remove(currentingredients.size() - 1);
        currentingredients.add(ingredient);
        allingredients.remove(ingredient);
        makeSearch();
        currentingredients.add(instances.newIngredientInstance());
        searchIngredientsAdapter.notifyItemInserted(position);
        searchIngredientsAdapter.notifyItemInserted(currentingredients.size() - 1);
        searchIngredientsAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDishClick(DishVM dish) {
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

    class SearchRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public SearchRequest() {
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
                    dishes = new Gson().fromJson(f, new TypeToken<ArrayList<DishVM>>() {}.getType());
                    searchResultsAdapter.SetData(dishes);
                    searchResultsAdapter.notifyDataSetChanged();
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
                    allingredients = new Gson().fromJson(f, new TypeToken<ArrayList<IngredientVM>>() {}.getType());
                    InitIngredientsSearch();
                    InitSearchresultsAdapter();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

package com.example.povarapp.MenuFragments.FavFragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DishActivity;
import com.example.povarapp.DishListAdapter;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.R;
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

public class FavFragment extends Fragment implements DishListAdapter.OnDishListener {
    private RecyclerView rv_dish_list;
    private Context context;;
    private ArrayList<DishVM> dishes;
    private AppUserVM user;

    private TextView empty_favs_warning_text;
    private SwipeRefreshLayout dish_list_swipe_layout;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_list_fragment, container, false);
        context = (Context) getActivity();
        GetData();
        SetupView(view);
        return view;
    }

    private void GetData() {
        try {
            Bundle args = getArguments();
            user = (AppUserVM) args.getSerializable("user");
            dishes = new ArrayList<>();
            UserFavsRequest userDishesRequest = new UserFavsRequest();
            String json = "{\"id\":" + user.GetId() + "}";
            userDishesRequest.execute("http://192.168.1.45:3030/userfavs", json);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SetupView(View v) {
        rv_dish_list = (RecyclerView) v.findViewById(R.id.rv_dish_list);
        empty_favs_warning_text = (TextView) v.findViewById(R.id.empty_warning_text);
        dish_list_swipe_layout = (SwipeRefreshLayout) v.findViewById(R.id.dish_list_swipe_layout);
        dish_list_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserFavsRequest userDishesRequest = new UserFavsRequest();
                String json = "{\"id\":" + user.GetId() + "}";
                userDishesRequest.execute("http://192.168.1.45:3030/userfavs", json);
            }
        });
    }

    public void SetupData() {
        DishListAdapter adapter = new DishListAdapter(context, this, dishes);
        rv_dish_list.setAdapter(adapter);
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
            myIntent.putExtra("user", user);
            context.startActivity(myIntent);
        }
    }

    class UserFavsRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public UserFavsRequest() {
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
                        empty_favs_warning_text.setVisibility(View.GONE);
                        rv_dish_list.setVisibility(View.VISIBLE);
                        dishes = result;
                        SetupData();
                    }
                    else {
                        empty_favs_warning_text.setVisibility(View.VISIBLE);
                        rv_dish_list.setVisibility(View.GONE);
                        if (user.GetId() <= 0) {
                            empty_favs_warning_text.setText("Вы не авторизованы!");
                        }
                        else {
                            empty_favs_warning_text.setText("Здесь пусто!");
                        }
                    }
                    dish_list_swipe_layout.setRefreshing(false);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

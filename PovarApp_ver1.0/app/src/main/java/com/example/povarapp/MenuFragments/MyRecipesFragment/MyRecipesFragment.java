package com.example.povarapp.MenuFragments.MyRecipesFragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.povarapp.Instances;
import com.example.povarapp.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyRecipesFragment extends Fragment implements DishListAdapter.OnDishListener{
    private RecyclerView rv_dish_list;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DishVM> dishes;
    private FloatingActionButton add_dish_but;
    private AppUserVM user;

    private TextView empty_myrecipes_warning_text;
    private SwipeRefreshLayout dish_list_swipe_layout;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myrecipes_fragment, container, false);
        context = (Context) getActivity();
        GetData();
        SetupView(view);
        //SetupData();
        return view;
    }

    private void GetData() {
        try {
            Bundle args = getArguments();
            user = (AppUserVM) args.getSerializable("user");
            dishes = new ArrayList<>();
            if (user != null) {
                UserDishesRequest userDishesRequest = new UserDishesRequest();
                String json = "{\"id\":" + String.valueOf(user.GetId()) + "}";
                userDishesRequest.execute("http://192.168.1.45:3030/userdishes", json);
            }
            else {
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setText("Ошибка! Пользователь не обнаружен!")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                        .setAnimations(Style.ANIMATIONS_POP).show();
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SetupView(View v) {
        add_dish_but = (FloatingActionButton) v.findViewById(R.id.fab);
        add_dish_but.setOnClickListener(add_dish_but_listener);
        rv_dish_list = (RecyclerView) v.findViewById(R.id.rv_dish_list);
        layoutManager = new LinearLayoutManager(context ,LinearLayoutManager.VERTICAL, false);
        empty_myrecipes_warning_text = (TextView) v.findViewById(R.id.empty_warning_text);
        dish_list_swipe_layout = (SwipeRefreshLayout) v.findViewById(R.id.dish_list_swipe_layout);
        dish_list_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserDishesRequest userDishesRequest = new UserDishesRequest();
                String json = "{\"id\":" + String.valueOf(user.GetId()) + "}";
                userDishesRequest.execute("http://192.168.1.45:3030/userdishes", json);
            }
        });
    }

    public void SetupData() {
        DishListAdapter adapter = new DishListAdapter(context, this, dishes);
        //rv_dish_list.setLayoutManager(layoutManager);
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

    private View.OnClickListener add_dish_but_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.GetId() == 0) {
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setText("Вы не авторизованы!")
                        .setDuration(Style.DURATION_VERY_SHORT)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                        .setAnimations(Style.ANIMATIONS_POP).show();
            }
            else {
                Intent myIntent = new Intent(context, DishActivity.class);
                myIntent.putExtra("dish", new Instances().newDishInstance());
                myIntent.putExtra("dishState", DishState.ADD);
                myIntent.putExtra("user", user);
                context.startActivity(myIntent);
            }
        }
    };

    class UserDishesRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public UserDishesRequest() {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
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
        protected void onPostExecute(final Response response) {
            super.onPostExecute(response);
            final String responseStr;
            if (response == null) return;
            if (response.isSuccessful()) {
                try {
                    responseStr = response.body().string();
                    String f1 = responseStr.substring(1, responseStr.length()-1);
                    String f2 = f1.replace("\\", "");
                    ArrayList<DishVM> result = new Gson().fromJson(f2, new TypeToken<ArrayList<DishVM>>() {}.getType());
                    if (result.size() > 0) {
                        empty_myrecipes_warning_text.setVisibility(View.GONE);
                        rv_dish_list.setVisibility(View.VISIBLE);
                        dishes = result;
                        SetupData();
                    }
                    else {
                        empty_myrecipes_warning_text.setVisibility(View.VISIBLE);
                        rv_dish_list.setVisibility(View.GONE);
                        if (user.GetId() <= 0) {
                            empty_myrecipes_warning_text.setText("Вы не авторизированы!");
                        }
                        else {
                            empty_myrecipes_warning_text.setText("У вас нет рецептов");
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

package com.example.povarapp.MenuFragments.MainFragment.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.povarapp.CategoriesActivity;
import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.DataVM.FavVM;
import com.example.povarapp.R;
import com.example.povarapp.DataVM.CategoryVM;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryVM> categories;
    private Context context;
    private DishDataAdapter horizontalAdapter;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private DishDataAdapter.OnItemClickListener listener;
    private ArrayList<DishVM> dishes_in_category;
    private AppUserVM user;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private int category_counter;

    public CategoryDataAdapter(Context context, DishDataAdapter.OnItemClickListener listener, ArrayList<CategoryVM> categories, AppUserVM user) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.user = user;
        for (category_counter = 0; category_counter < categories.size(); category_counter++) {
            categories.get(category_counter).SetDishes(new ArrayList<DishVM>());
            CategoryDishesRequest categoryDishesRequest = new CategoryDishesRequest();
            String json = "{\"id\":" + String.valueOf(categories.get(category_counter).GetId()) + "}";
            categoryDishesRequest.execute("http://192.168.1.45:3030/category10dishes", json, String.valueOf(category_counter));
        }
    }

    @Override
    public CategoryDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_page_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryDataAdapter.ViewHolder holder, final int position) {
        holder.nameView.setText(categories.get(position).GetName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, CategoriesActivity.class);
                myIntent.putExtra("category", categories.get(position));
                myIntent.putExtra("user", user);
                context.startActivity(myIntent);
            }
        });

        horizontalAdapter = new DishDataAdapter(context, listener, categories.get(position).GetDishes());
        horizontalAdapter.setHasStableIds(true);

        holder.rv_dishes.setAdapter(horizontalAdapter);
        holder.rv_dishes.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv_dishes;
        private LinearLayoutManager manager = new
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        private TextView nameView;
        private ImageButton button;

        ViewHolder(View view){
            super(view);

            rv_dishes = view.findViewById(R.id.rv_dishes);
            rv_dishes.setHasFixedSize(true);
            rv_dishes.setNestedScrollingEnabled(false);
            rv_dishes.setLayoutManager(manager);
            rv_dishes.setItemAnimator(new DefaultItemAnimator());
            rv_dishes.setHasFixedSize(true);
            rv_dishes.setItemViewCacheSize(20);
            rv_dishes.setDrawingCacheEnabled(true);
            rv_dishes.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            nameView = (TextView) view.findViewById(R.id.category_name);
            button = (ImageButton) view.findViewById(R.id.getcategory_dishes);
        }
    }

    class CategoryDishesRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public CategoryDishesRequest() {
            client = new OkHttpClient();
        }

        private int counter = 0;

        @Override
        protected Response doInBackground(String... args) {
            try {
                RequestBody body = RequestBody.create(args[1], JSON);
                Request request = new Request.Builder()
                        .url(args[0])
                        .post(body)
                        .build();

                this.counter = Integer.parseInt(args[2]);

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
                    ArrayList<DishVM> dishes = new Gson().fromJson(f, new TypeToken<ArrayList<DishVM>>() {}.getType());
                    if (dishes.size() > 0) {
                        categories.get(this.counter).SetDishes(dishes);
                        notifyDataSetChanged();
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

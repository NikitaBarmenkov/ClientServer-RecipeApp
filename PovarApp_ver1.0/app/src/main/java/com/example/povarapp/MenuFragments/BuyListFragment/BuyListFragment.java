package com.example.povarapp.MenuFragments.BuyListFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.DataVM.BuyItemVM;
import com.example.povarapp.DataVM.DishVM;
import com.example.povarapp.Enums.DishState;
import com.example.povarapp.MenuFragments.ProfileFragments.SignInFragment;
import com.example.povarapp.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BuyListFragment extends Fragment implements BuyListAdapter.CheckListener {
    private RecyclerView rv_buy_ings;
    private ArrayList<BuyItemVM> buyItems;
    private Button delete_buylist_but;

    private BuyListAdapter adapter;

    private ConstraintLayout buylist_layout;
    private TextView empty_buylist_warning_text;

    private AppUserVM user;
    private int changedItemPosition;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.buy_list, container, false);
        GetData();
        SetView(view);
        return view;
    }

    private void GetData() {
        try{
            Bundle args = getArguments();
            user = (AppUserVM) args.getSerializable("user");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        buyItems = new ArrayList<>();
        BuyListRequest buyListRequest = new BuyListRequest();
        String json = "{\"id\":" + user.GetId() + "}";
        buyListRequest.execute("http://192.168.1.45:3030/userbuylist", json);
    }

    private void SetView(View v) {
        delete_buylist_but = (Button) v.findViewById(R.id.delete_buylist_but);
        rv_buy_ings = (RecyclerView) v.findViewById(R.id.rv_buy_ings);

        buylist_layout = (ConstraintLayout) v.findViewById(R.id.buylist_layout);
        empty_buylist_warning_text = (TextView) v.findViewById(R.id.empty_buylist_warning_text);

        delete_buylist_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBuyListRequest deleteBuyListRequest = new DeleteBuyListRequest();
                String json = "{\"id\":" + user.GetId() + "}";
                deleteBuyListRequest.execute("http://192.168.1.45:3030/deletebuylist", json);
            }
        });
    }

    private void setData() {
        String[] quantity_arr;
        String[] unit_arr;
        String result = "";
        for (BuyItemVM item : buyItems) {
            quantity_arr = item.GetQuantity().split("/");
            unit_arr = item.GetUnit().split("/");
            result = "";
            for (int i = 0; i < unit_arr.length; i++) {
                if (i == 0) {
                    result += quantity_arr[i] + " " + unit_arr[i];
                }
                else {
                    result += " + " + quantity_arr[i] + " " + unit_arr[i];
                }
            }
            item.SetQuantity_unit(result);
        }
        adapter = new BuyListAdapter(getContext(), this, buyItems);
        rv_buy_ings.setAdapter(adapter);
    }

    @Override
    public void OnCheckBoxclick(BuyItemVM item, int position) {
        UpdateBuyListRequest updateBuyListRequest = new UpdateBuyListRequest();
        String json = new Gson().toJson(item);
        changedItemPosition = position;
        updateBuyListRequest.execute("http://192.168.1.45:3030/updatebuylistitem", json);
    }

    private void ShowMessage(String message) {
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                .setText(message)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    class BuyListRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public BuyListRequest() {
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
                    buyItems = new Gson().fromJson(f, new TypeToken<ArrayList<BuyItemVM>>() {}.getType());
                    if (buyItems.size() == 0) {
                        empty_buylist_warning_text.setVisibility(View.VISIBLE);
                        buylist_layout.setVisibility(View.GONE);
                    }
                    else {
                        setData();
                        empty_buylist_warning_text.setVisibility(View.GONE);
                        buylist_layout.setVisibility(View.VISIBLE);
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class DeleteBuyListRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public DeleteBuyListRequest() {
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
                    buyItems = new Gson().fromJson(f, new TypeToken<ArrayList<BuyItemVM>>() {}.getType());
                    if (buyItems.size() == 0) {
                        empty_buylist_warning_text.setVisibility(View.VISIBLE);
                        buylist_layout.setVisibility(View.GONE);
                    }
                    else {
                        setData();
                        empty_buylist_warning_text.setVisibility(View.GONE);
                        buylist_layout.setVisibility(View.VISIBLE);
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class UpdateBuyListRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public UpdateBuyListRequest() {
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
                    ArrayList<BuyItemVM> result = new Gson().fromJson(f, new TypeToken<ArrayList<BuyItemVM>>() {}.getType());
                    if (result.size() > 0) {
                        buyItems.get(changedItemPosition).SetChecked(result.get(0).GetChecked());
                        Collections.sort(buyItems);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                ShowMessage("Ошибка");
            }
        }
    }
}

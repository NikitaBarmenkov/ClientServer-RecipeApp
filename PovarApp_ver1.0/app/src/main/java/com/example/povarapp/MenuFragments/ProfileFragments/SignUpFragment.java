package com.example.povarapp.MenuFragments.ProfileFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment {
    private EditText signup_name_text;
    private EditText signup_password_text;
    private EditText signup_email_text;
    private Button signup_but;
    private Button signup_signin_but;

    private SignUpListener listener;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface SignUpListener {
        public void SignUpClick(AppUserVM user);
        public void SignUpBackClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_signup_fragment, container, false);
        Bundle args = getArguments();
        listener = (SignUpListener) args.getSerializable("listener");
        SetView(view);
        SetButtonsClickListeners();
        return view;
    }

    public void SetView(View v) {
        signup_name_text = (EditText) v.findViewById(R.id.signup_name_text);
        signup_password_text = (EditText) v.findViewById(R.id.signup_password_text);
        signup_email_text = (EditText) v.findViewById(R.id.signup_email_text);
        signup_but = (Button) v.findViewById(R.id.signup_signup_but);
        signup_signin_but = (Button) v.findViewById(R.id.signup_signin_but);
        signup_name_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new SearchUserRequest().execute("http://192.168.1.45:3030/searchuser", "{\"searchText\":" + "\"" + signup_name_text.getText().toString() + "\"}");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void SetButtonsClickListeners() {
        signup_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signup_name_text.getError() == null) {
                    AppUserVM user = new AppUserVM();
                    user.SetId(-1);
                    user.SetName(signup_name_text.getText().toString());
                    user.SetPassword(signup_password_text.getText().toString());
                    user.SetEmail(signup_email_text.getText().toString());
                    listener.SignUpClick(user);
                }
            }
        });
        signup_signin_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.SignUpBackClick();
            }
        });
    }

    class SearchUserRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public SearchUserRequest() {
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
                ex.getMessage();
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
                    ArrayList<AppUserVM> users = new Gson().fromJson(f, new TypeToken<ArrayList<AppUserVM>>() {}.getType());//JSON to JAVA
                    if (users.isEmpty()) {
                        signup_name_text.setError(null);
                    }
                    else {
                        signup_name_text.setError("Пользователь с таким именем уже существует!");
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

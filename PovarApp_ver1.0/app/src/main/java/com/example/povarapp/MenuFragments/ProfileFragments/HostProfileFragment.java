package com.example.povarapp.MenuFragments.ProfileFragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.Enums.MainState;
import com.example.povarapp.MainActivity;
import com.example.povarapp.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HostProfileFragment extends Fragment implements SignInFragment.SignInListener, SignUpFragment.SignUpListener, ProfileFragment.ProfileListener, Serializable {
    private MainState state;
    private Fragment signInFragment;
    private Fragment signUpFragment;
    private Fragment profileFragment;
    private FragmentManager fragmentManager;

    private AppUserVM activeUser;
    private Context context;

    private MainActivity main;

    private IActiveUser listener;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface IActiveUser {
        public void ChangeUser(AppUserVM user);
    }

    public HostProfileFragment() {
        Bundle args = new Bundle();
        args.putSerializable("listener", this);
        signInFragment = new SignInFragment();
        signInFragment.setArguments(args);
        signUpFragment = new SignUpFragment();
        signUpFragment.setArguments(args);
        profileFragment = new ProfileFragment();
        profileFragment.setArguments(args);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.listener = (IActiveUser) args.getSerializable("listener");
        context = (Context) getActivity();
        main = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.host_profile_fragment, container, false);
        activeUser = (AppUserVM) args.getSerializable("user");
        if (activeUser.GetId() > 0) {
            ((ProfileFragment) profileFragment).SetUser(activeUser);
            SetFragment(MainState.PR_VIEW);
        }
        else SetFragment(MainState.SIGNIN);
        return view;
    }

    public void SetFragment(MainState state) {
        if (fragmentManager != null) {
            switch (state) {
                case SIGNIN:
                    fragmentManager.beginTransaction().replace(R.id.fl_content, signInFragment).commit();
                    break;
                case SIGNUP:
                    fragmentManager.beginTransaction().replace(R.id.fl_content, signUpFragment).commit();
                    break;
                case PR_VIEW:
                    fragmentManager.beginTransaction().replace(R.id.fl_content, profileFragment).commit();
                    break;
                default:
                    break;
            }
        }
    }

    private void ShowMessage(String mess) {
        SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                .setText(mess)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    @Override
    public void SignInClick(AppUserVM user) {
        LoginRequest loginRequest = new LoginRequest();
        String json = new Gson().toJson(user);
        loginRequest.execute("http://192.168.1.45:3030/userlogin", json);
    }

    @Override
    public void OpenSignUpClick() {
        SetFragment(MainState.SIGNUP);
    }

    @Override
    public void SignUpClick(AppUserVM user) {
        RegisterRequet registerRequet = new RegisterRequet();
        String json = new Gson().toJson(user);
        registerRequet.execute("http://192.168.1.45:3030/createuser", json);
    }

    @Override
    public void SignUpBackClick() {
        SetFragment(MainState.SIGNIN);
    }

    @Override
    public void LogOutClick(AppUserVM user) {
        main.ChangeUser(user);
        SetFragment(MainState.SIGNIN);
    }

    @Override
    public void SaveUserClick(AppUserVM user) {
        SaveUserRequest saveUserRequest = new SaveUserRequest();
        String json = new Gson().toJson(user);
        saveUserRequest.execute("http://192.168.1.45:3030/updateuser", json);
    }

    class LoginRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        int code;
        public LoginRequest() {
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
                    if (users.size() > 0) {
                        SetFragment(MainState.PR_VIEW);
                        ((ProfileFragment) profileFragment).SetUser(users.get(0));
                        //((ProfileFragment)profileFragment).SetUserData();
                        main.ChangeUser(users.get(0));
                    }
                    else {
                        SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                                .setText("Такого пользователя не существует!")
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

        }
    }

    class RegisterRequet extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public RegisterRequet() {
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
                    SetFragment(MainState.PR_VIEW);
                    ((ProfileFragment)profileFragment).SetUser(users.get(0));
                    //((ProfileFragment)profileFragment).SetUserData();
                    main.ChangeUser(users.get(0));
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class SaveUserRequest extends AsyncTask<String, Void, Response> {
        private OkHttpClient client;
        public SaveUserRequest() {
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
                    AppUserVM user = new Gson().fromJson(f, AppUserVM.class);//JSON to JAVA
                    ((ProfileFragment)profileFragment).SetUser(user);
                    ((ProfileFragment)profileFragment).SetUserData();
                    main.ChangeUser(user);
                    ShowMessage("Данные успешно обновлены");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}

package com.example.povarapp.MenuFragments.ProfileFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.povarapp.DataVM.AppUserVM;
import com.example.povarapp.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

public class ProfileFragment extends Fragment {
    private EditText userprofile_name_text;
    private EditText userprofile_password_text;
    private EditText userprofile_email_text;
    private Button profile_logout_but;
    private Button profile_save_but;
    private Button profile_edit_but;
    private Button profile_back_but;

    private ProfileListener listener;
    private AppUserVM user;
    private Context context;

    public interface ProfileListener {
        public void LogOutClick(AppUserVM user);
        public void SaveUserClick(AppUserVM user);
    }

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.listener = (ProfileListener) args.getSerializable("listener");
        View view = inflater.inflate(R.layout.profile_user_fragment, container, false);
        context = (Context) getActivity();
        SetView(view);
        SetUserData();
        SetButtonsClickListeners();
        return view;
    }

    private void SetView(View v) {
        userprofile_name_text = (EditText) v.findViewById(R.id.userprofile_name_text);
        userprofile_password_text = (EditText) v.findViewById(R.id.userprofile_password_text);
        userprofile_email_text = (EditText) v.findViewById(R.id.userprofile_email_text);
        profile_logout_but = (Button) v.findViewById(R.id.profile_logout_but);
        profile_save_but = (Button) v.findViewById(R.id.profile_save_but);
        profile_edit_but = (Button) v.findViewById(R.id.profile_edit_but);
        profile_back_but = (Button) v.findViewById(R.id.profile_back_but);
    }

    public void SetUser(AppUserVM user) {
        this.user = user;
    }

    public void SetUserData() {
        userprofile_name_text.setText(new String(user.GetName()));
        userprofile_password_text.setText(new String(user.GetPassword()));
        userprofile_email_text.setText(new String(user.GetEmail()));

        SetViewToNotEdited();
    }

    private void ShowMessage(String mess) {
        SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                .setText(mess)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    public void SetButtonsClickListeners() {
        profile_logout_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUserVM user = new AppUserVM();
                user.SetId(0);
                user.SetName("");
                user.SetPassword("");
                listener.LogOutClick(user);
            }
        });
        profile_save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userprofile_name_text.getText()) ||
                TextUtils.isEmpty(userprofile_password_text.getText())) {
                    ShowMessage("Поля не заполнены!");
                }
                else {
                    user.SetName(userprofile_name_text.getText().toString());
                    user.SetPassword(userprofile_password_text.getText().toString());
                    user.SetEmail(userprofile_email_text.getText().toString());
                    listener.SaveUserClick(user);
                }
            }
        });

        profile_edit_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_save_but.setVisibility(View.VISIBLE);
                profile_logout_but.setVisibility(View.GONE);
                profile_edit_but.setVisibility(View.GONE);
                profile_back_but.setVisibility(View.VISIBLE);

                userprofile_name_text.setEnabled(true);
                userprofile_password_text.setEnabled(true);
                userprofile_email_text.setEnabled(true);
            }
        });

        profile_back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetViewToNotEdited();
            }
        });
    }

    private void SetViewToNotEdited() {
        profile_save_but.setVisibility(View.GONE);
        profile_logout_but.setVisibility(View.VISIBLE);
        profile_edit_but.setVisibility(View.VISIBLE);
        profile_back_but.setVisibility(View.GONE);

        userprofile_name_text.setEnabled(false);
        userprofile_password_text.setEnabled(false);
        userprofile_email_text.setEnabled(false);
    }
}

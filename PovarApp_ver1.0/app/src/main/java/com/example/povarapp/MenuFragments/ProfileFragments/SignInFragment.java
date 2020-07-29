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
import com.mikepenz.iconics.utils.Utils;

public class SignInFragment extends Fragment {
    private EditText signin_name_text;
    private EditText signin_password_text;
    private Button signin_signin_but;
    private Button signin_signup_but;

    private SignInListener listener;
    private Context context;

    public interface SignInListener {
        public void SignInClick(AppUserVM user);
        public void OpenSignUpClick();
    }

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.listener = (SignInListener) args.getSerializable("listener");
        View view = inflater.inflate(R.layout.profile_signin_fragment, container, false);
        context = (Context) getActivity();
        SetView(view);
        SetButtonsClickListeners();
        return view;
    }

    public void SetView(View v) {
        signin_name_text = (EditText) v.findViewById(R.id.signin_name_text);
        signin_password_text = (EditText) v.findViewById(R.id.signin_password_text);
        signin_signin_but = (Button) v.findViewById(R.id.signin_signin_but);
        signin_signup_but = (Button) v.findViewById(R.id.signin_signup_but);
    }

    public void SetButtonsClickListeners() {
        signin_signin_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUserVM user = new AppUserVM();
                if (TextUtils.isEmpty(signin_name_text.getText()) ||
                        TextUtils.isEmpty(signin_password_text.getText())) {
                    SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                            .setText("Поля не заполнены")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                }
                else {
                    user.SetId(-1);
                    user.SetName(signin_name_text.getText().toString());
                    user.SetPassword(signin_password_text.getText().toString());
                    listener.SignInClick(user);
                }
            }
        });
        signin_signup_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OpenSignUpClick();
            }
        });
    }
}

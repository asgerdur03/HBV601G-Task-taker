package com.example.hbv601G.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbv601G.AuthActivity;
import com.example.hbv601G.R;

import org.w3c.dom.Text;


public class SignupFragment extends Fragment {
    private EditText usernameInput, emailInput, passwordInput;
    private Button signupButton;
    private TextView goToLogin;
    private AuthActivity authActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        emailInput = view.findViewById(R.id.emailInput);

        goToLogin = view.findViewById(R.id.goToLogin);
        signupButton = view.findViewById(R.id.signupButton);
        authActivity = (AuthActivity) getActivity();

        signupButton.setOnClickListener(v -> signupUser());
        goToLogin.setOnClickListener(v -> authActivity.switchToLogin());

        return view;

    }

    private void signupUser(){
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // TODO: make api call that creates new user, and then redirects to login

        // mock
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.equals("demo")) {
            Toast.makeText(getActivity(), "Username already in use", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Signup successful! Please log in.", Toast.LENGTH_SHORT).show();
            authActivity.switchToLogin(); // Redirect to login
        }
    }


}
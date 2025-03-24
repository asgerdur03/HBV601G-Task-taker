package com.example.hbv601G.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hbv601G.AuthActivity;
import com.example.hbv601G.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private Button logoutButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutButton = binding.logoutButton;
        logoutButton.setOnClickListener(v-> logoutUser());
        //nýtt og fallegt
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String currentUsername = prefs.getString("user_email", "");
        String currentPassword = prefs.getString("user_password", "");

        binding.editUsername.setText(currentUsername);
        binding.editPassword.setText(currentPassword);

        binding.updateButton.setOnClickListener(v -> {
            String newUsername = binding.editUsername.getText().toString().trim();
            String newPassword = binding.editPassword.getText().toString().trim();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_email", newUsername);
            editor.putString("user_password", newPassword);
            editor.apply();

            Toast.makeText(getContext(), "Upplýsingar uppfærðar!", Toast.LENGTH_SHORT).show();
        });



        return root;
    }

    private void logoutUser(){
        // Should remove token and redirect to login page
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //editor.clear();
        editor.remove("jwt_token");
        editor.apply();

        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
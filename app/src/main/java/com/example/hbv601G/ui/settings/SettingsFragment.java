package com.example.hbv601G.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hbv601G.AuthActivity;
import com.example.hbv601G.R;
import com.example.hbv601G.databinding.FragmentSettingsBinding;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.UserService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResultLauncher;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Button logoutButton;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutButton = binding.logoutButton;
        logoutButton.setOnClickListener(v -> logoutUser());

        binding.uploadProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        });

        binding.removePictureButton.setOnClickListener(v -> {
            SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("profile_image_uri");
            editor.apply();

            // Reset image to default placeholder
            binding.profileImageView.setImageResource(R.drawable.baseline_person_24);
            Toast.makeText(getContext(), "Mynd fjarlægð", Toast.LENGTH_SHORT).show();
        });

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        String savedImageUri = prefs.getString("profile_image_uri", null);
        if (savedImageUri != null) {
            binding.profileImageView.setImageURI(Uri.parse(savedImageUri));
        }


        String currentUsername = prefs.getString("user_email", "");
        String currentPassword = prefs.getString("user_password", "");



        binding.updateButton.setOnClickListener(v -> {
            String newUsername = binding.editUsername.getText().toString().trim();
            String newPassword = binding.editPassword.getText().toString().trim();
            String email = prefs.getString("user_email", "");

            UserService userService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(UserService.class);

            userService.update(newUsername, email, newPassword).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user_email", email);
                        editor.putString("user_password", newPassword);
                        editor.apply();

                        Toast.makeText(getContext(), "Notandi uppfærður!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Villa við uppfærslu.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Netvilla: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();

                        binding.profileImageView.setImageURI(selectedImageUri);

                        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("profile_image_uri", selectedImageUri.toString()).apply();

                    }
                }
        );
    }

    private void logoutUser() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

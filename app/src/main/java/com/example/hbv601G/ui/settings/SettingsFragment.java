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
import com.example.hbv601G.data.AppDatabase;
import com.example.hbv601G.databinding.FragmentSettingsBinding;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.networking.TokenManager;
import com.example.hbv601G.services.UserService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Button logoutButton;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutButton = binding.logoutButton;
        logoutButton.setOnClickListener(v -> logoutUser());

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // loada myndina frá internal storage
        String imagePath = prefs.getString("profile_image_path", null);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                binding.profileImageView.setImageBitmap(bitmap);
            }
        }


        binding.uploadProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        });

        binding.removePictureButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("profile_image_path");
            editor.apply();

            // Reset image to default placeholder
            binding.profileImageView.setImageResource(R.drawable.baseline_person_24);
            Toast.makeText(getContext(), "Mynd fjarlægð", Toast.LENGTH_SHORT).show();
        });


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

                        try {
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();

                            binding.profileImageView.setImageBitmap(bitmap);

                            // vista myndina í internal storage
                            File file = new File(requireContext().getFilesDir(), "profile_image.jpg");
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.close();

                            SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                            prefs.edit().putString("profile_image_path", selectedImageUri.toString()).apply();
                            Toast.makeText(requireContext(), "Mynd vistuð!", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Villa við mynd", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }

    private void logoutUser() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt_token");
        editor.apply();

        TokenManager tokenManager = new TokenManager(requireContext());
        tokenManager.clearToken();

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            db.categoryDao().deleteAll();
        }).start();

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

package com.example.hbv601G.ui.category;

import android.content.ContentQueryMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.data.AppDatabase;
import com.example.hbv601G.databinding.FragmentCategoryBinding;
import com.example.hbv601G.entities.Category;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.CategoryService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {
    private EditText categoryNameInput, categoryColorInput;
    private Button createCategoryButton;

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();



    private FragmentCategoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        categoryNameInput = root.findViewById(R.id.categoryNameInput);
        categoryColorInput = root.findViewById(R.id.categoryColorInput);
        createCategoryButton = root.findViewById(R.id.createCategoryButton);

        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CategoryAdapter(categoryList, category -> deleteCategory(category));
        binding.categoryRecyclerView.setAdapter(adapter);
        createCategoryButton.setOnClickListener(v -> createCategory());
        loadCategories();

        return root;
    }

     private void loadCategories(){
        CategoryService categoryService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(CategoryService.class);

        categoryService.getCategories().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    JsonObject json = response.body();

                    JsonArray categoryArray = json.getAsJsonArray("categories");

                    List<Category> parsedList = new ArrayList<>();
                    for (JsonElement element: categoryArray){
                        Category category = new Gson().fromJson(element, Category.class);
                        parsedList.add(category);
                    }

                    new Thread(() -> {
                        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
                        appDatabase.categoryDao().insertAll(parsedList);

                    }).start();

                    categoryList = parsedList;
                    adapter.updateList(categoryList);

                } else{
                    Log.e("CategoryDebug", "Error code: " + response.code());
                    Toast.makeText(getContext(), "API failed, loading from local", Toast.LENGTH_SHORT).show();
                    loadFromRoom();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error, fetching from local ", Toast.LENGTH_SHORT).show();
                loadFromRoom();

            }
        });
        adapter.updateList(categoryList);
    }

    private void loadFromRoom() {
        new Thread(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
            List<Category> localCategories = appDatabase.categoryDao().getAll();

            requireActivity().runOnUiThread(() -> {
                categoryList = localCategories;
                adapter.updateList(categoryList);
            });
        }).start();
    }

    private void deleteCategory(Category category){
        CategoryService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(CategoryService.class);

        // ATH: Api deletes if no task is using category
        service.deleteCategory(category.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    categoryList.remove(category);
                    adapter.updateList(categoryList);
                    Toast.makeText(getContext(), "Category deleted, if it has no tasks successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("CategoryDebug", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CategoryDebug", "Error: " + t.getMessage());
            }
        });


        Log.d("CategoryDebug", "Delete category Button clicked");


    }
    private void createCategory(){
        String name = categoryNameInput.getText().toString();
        String color = categoryColorInput.getText().toString();

        if (name.isEmpty() || color.isEmpty()) return;

        Category newCategory = new Category(name, color);

        CategoryService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(CategoryService.class);

        service.createCategory(newCategory).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                    loadCategories();
                    categoryNameInput.setText("");
                    categoryColorInput.setText("");
                    Toast.makeText(getContext(), "Category created successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("CategoryDebug", "Failed: " + response.code());
                    Toast.makeText(getContext(), "Failed to create cateogry", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CategoryDebug", "Error: " + t.getMessage());

            }
        });





    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
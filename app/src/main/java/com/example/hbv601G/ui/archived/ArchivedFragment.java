package com.example.hbv601G.ui.archived;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hbv601G.R;
import com.example.hbv601G.databinding.FragmentArchivedBinding;
import com.example.hbv601G.databinding.FragmentCategoryBinding;
import com.example.hbv601G.ui.category.CategoryViewModel;

public class ArchivedFragment extends Fragment {

    private FragmentArchivedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ArchivedViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ArchivedViewModel.class);

        binding = FragmentArchivedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textArchived;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
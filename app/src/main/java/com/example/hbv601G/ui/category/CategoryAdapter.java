package com.example.hbv601G.ui.category;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnDeleteClickListener listener) {
        this.categoryList = categories;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.name.setText(category.getCategoryName());
        holder.color.setText("Color: " + category.getCategoryColor());

        try{
            int parsedColor = Color.parseColor(category.getCategoryColor());
            holder.cardView.setCardBackgroundColor(parsedColor);

        }catch (IllegalArgumentException e){
            holder.cardView.setCardBackgroundColor(Color.LTGRAY);

        }

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(category);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateList(List<Category> newList){
        categoryList = newList;
        notifyDataSetChanged();

    }


    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, color;
        Button deleteButton;
        CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryName);
            color = itemView.findViewById(R.id.categoryColor);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            cardView = itemView.findViewById(R.id.categoryCardRoot);
        }
    }
}


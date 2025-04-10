package com.example.hbv601G.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.*;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "categoryName")
    private String categoryName;
    @ColumnInfo(name = "color")
    private String color;

    // ekki viss með eftirfarandi
    @Ignore
    private User user;
    @Ignore
    private List<Task> task = new ArrayList<>();


    public Category(String categoryName, String categoryColor){
        this.categoryName=categoryName;
        this.color=categoryColor;
    }

    public Category(User user, String categoryName, String categoryColor) {
        this.user = user;
        this.categoryName = categoryName;
        this.color = categoryColor;
    }

    public Category(long id, String categoryName, String color) {
        this.id = id;
        this.categoryName = categoryName;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String categoryColor) {
        this.color = categoryColor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    @NonNull
    @Override
    public String toString(){
        return "Category: " + getCategoryName() + " " + getUser().getUsername() + " " + getColor() + " " + getId();

    }
}

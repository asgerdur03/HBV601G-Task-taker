package com.example.hbv601G.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private long id;
    private String categoryName;
    private String categoryColor;

    // ekki viss með eftirfarandi

    private User user;
    private List<Task> task = new ArrayList<>();


    public Category(String categoryName, String categoryColor){
        this.categoryName=categoryName;
        this.categoryColor=categoryColor;
    }

    public Category(User user, String categoryName, String categoryColor) {
        this.user = user;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
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

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
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
}

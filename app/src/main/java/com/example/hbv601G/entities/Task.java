package com.example.hbv601G.entities;

import androidx.annotation.NonNull;

public class Task {

    private long id;
    private String taskName;
    private String taskNote;
    private String status;
    private String priority;
    private String dueDate;
    private Boolean favorite = false;
    private Boolean archived = false;

    private long categoryId;

    // annað

    private Category category;
    private User user;

    public Task(String taskName, String taskNote) {
        this.taskName = taskName;
        this.taskNote = taskNote;
    }

    public Task(String taskName, String taskNote, String taskStatus, String taskPriority, String dueDate, long categoryId) {
        this.taskName = taskName;
        this.taskNote = taskNote;
        this.status = taskStatus;
        this.priority = taskPriority;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString(){
        return "Task: " +
                getTaskName() + " " +
                getTaskNote()+ " " +
                getPriority() + " " +
                getStatus() + " " +
                getDueDate();

    }
}

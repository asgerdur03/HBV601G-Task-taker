package com.example.hbv601G.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.ColumnInfo;


import androidx.annotation.NonNull;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey
    public int id;

// fyrir room?
    public String title;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String description;

    public Task(int id, String title, String description, String dueDate, boolean completed, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.userId = userId;
    }

    public Task() {
    }

    public String dueDate;
    public boolean completed;
    public int userId;

    // gamla :

    private String taskName;
    private String taskNote;
    private String status;
    private String priority;
   //  private String dueDate;
    private Boolean favorite = false;
    private Boolean archived = false;

    private long categoryId;

    // annað

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    @Ignore
    private Category category;
    @Ignore
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

   // public void setId(long id) {
   //     this.id = id;
   // }

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

package com.example.hbv601G.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


// ætti að leyfa okkur að nota json skjalið ( í assets ) pre api
public class DummyGognVinnsla {

    private static final String fileName = "dummyData.json";
    private static DummyData data;

    public static void loadData(Context context) {
        try {
            InputStreamReader reader = new InputStreamReader(context.getAssets().open(fileName));
            data = new Gson().fromJson(reader, DummyData.class);
            reader.close();
        } catch (Exception e) {
            Log.e("DummyGognVinnsla", "Error loading data", e);
        }
    }

    public static void saveData(Context context) {
        try {
            String json = new Gson().toJson(data);
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            Log.e("DummyGognVinnsla", "Error saving data", e);
        }
    }

    public static List<Task> getTasks() {
        if (data != null && data.tasks != null)
            return data.tasks;
        return new ArrayList<>();
    }

    public static void addTask(Task task, Context context) {
        if (data != null) {
            data.tasks.add(task);
            saveData(context);
        }
    }

    public static void updateTaskStatus(int taskId, String newStatus, Context context) {
        if (data != null) {
            for (Task task : data.tasks) {
                if (task.id == taskId) {
                    task.status = newStatus;
                    break;
                }
            }
            saveData(context);
        }
    }

    public static void deleteTask(int taskId, Context context) {
        if (data != null) {
            Iterator<Task> iterator = data.tasks.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().id == taskId) {
                    iterator.remove();
                    break;
                }
            }
            saveData(context);
        }
    }

    public static void archiveTask(int taskId, Context context) {
        if (data != null) {
            for (Task task : data.tasks) {
                if (task.id == taskId) {
                    task.archived = true;
                    break;
                }
            }
            saveData(context);
        }
    }

    public static void unarchiveTask(int taskId, Context context) {
        if (data != null) {
            for (Task task : data.tasks) {
                if (task.id == taskId) {
                    task.archived = false;
                    break;
                }
            }
            saveData(context);
        }
    }

    public static List<Task> getArchivedTasks() {
        List<Task> result = new ArrayList<>();
        if (data != null) {
            for (Task task : data.tasks) {
                if (task.archived) result.add(task);
            }
        }
        return result;
    }

    public static List<Task> getActiveTasks() {
        List<Task> result = new ArrayList<>();
        if (data != null) {
            for (Task task : data.tasks) {
                if (!task.archived) result.add(task);
            }
        }
        return result;
    }


    public static class DummyData {
        public User user;
        public List<Task> tasks;
        public List<Category> categories;
    }

    public static class User {
        public String username;
        public String password;
    }

    public static class Task {
        public int id;
        public String taskName;
        public String taskNote;
        public String status;
        public String priority;
        public String dueDate;
        public boolean favorites;
        public boolean archived;
        public int categoryId;
    }

    public static class Category {
        public int id;
        public String name;
        public String color;
    }
}

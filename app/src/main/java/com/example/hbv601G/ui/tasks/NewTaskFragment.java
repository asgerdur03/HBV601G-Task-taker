package com.example.hbv601G.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hbv601G.R;
import com.example.hbv601G.data.DummyGognVinnsla;

import java.util.List;

public class NewTaskFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText titleInput = view.findViewById(R.id.inputTaskTitle);
        EditText noteInput = view.findViewById(R.id.inputTaskNote);
        EditText dueDateInput = view.findViewById(R.id.inputTaskDueDate);
        Button saveButton = view.findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String note = noteInput.getText().toString().trim();
            String dueDate = dueDateInput.getText().toString().trim();

            DummyGognVinnsla.Task newTask = new DummyGognVinnsla.Task();
            newTask.id = generateNextTaskId();
            newTask.taskName = title;
            newTask.taskNote = note;
            newTask.dueDate = dueDate;
            newTask.status = "PENDING";
            newTask.priority = "LOW";
            newTask.archived = false;
            newTask.favorites = false;
            newTask.categoryId = 1;

            DummyGognVinnsla.addTask(newTask, requireContext());

            Toast.makeText(getContext(), "Task created!", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
    }

    private int generateNextTaskId() {
        List<DummyGognVinnsla.Task> tasks = DummyGognVinnsla.getTasks();
        int maxId = 0;
        for (DummyGognVinnsla.Task task : tasks) {
            if (task.id > maxId) maxId = task.id;
        }
        return maxId + 1;
    }
}

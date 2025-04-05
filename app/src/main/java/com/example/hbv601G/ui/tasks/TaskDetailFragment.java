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
public class TaskDetailFragment extends Fragment {

    private DummyGognVinnsla.Task currentTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText titleInput = view.findViewById(R.id.detailTaskTitle);
        EditText noteInput = view.findViewById(R.id.detailTaskNote);
        EditText dueDateInput = view.findViewById(R.id.detailTaskDueDate);
        Button saveButton = view.findViewById(R.id.buttonSaveEdit);
        Button archiveButton = view.findViewById(R.id.buttonArchive);
        Button deleteButton = view.findViewById(R.id.buttonDelete);

        int taskId = getArguments() != null ? getArguments().getInt("taskId", -1) : -1;
        if (taskId == -1) return;

        for (DummyGognVinnsla.Task task : DummyGognVinnsla.getTasks()) {
            if (task.id == taskId) {
                currentTask = task;
                break;
            }
        }

        if (currentTask == null) return;

        titleInput.setText(currentTask.taskName);
        noteInput.setText(currentTask.taskNote);
        dueDateInput.setText(currentTask.dueDate);
        archiveButton.setText(currentTask.archived ? "Unarchive" : "Archive");

        saveButton.setOnClickListener(v -> {
            currentTask.taskName = titleInput.getText().toString();
            currentTask.taskNote = noteInput.getText().toString();
            currentTask.dueDate = dueDateInput.getText().toString();
            DummyGognVinnsla.saveData(requireContext());
            Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
        });

        archiveButton.setOnClickListener(v -> {
            currentTask.archived = !currentTask.archived;
            DummyGognVinnsla.saveData(requireContext());
            archiveButton.setText(currentTask.archived ? "Unarchive" : "Archive");
            Toast.makeText(getContext(), "Task status changed", Toast.LENGTH_SHORT).show();
        });

        deleteButton.setOnClickListener(v -> {
            DummyGognVinnsla.deleteTask(currentTask.id, requireContext());
            Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
    }
}

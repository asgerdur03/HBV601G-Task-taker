package com.example.hbv601G.entities;

import java.time.LocalDate;

public class Task {

    private long id;
    private String taskName;
    private String taskNote;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private LocalDate dueDate;
    private Boolean favorite = false;
    private Boolean archived = false;

    // annað

    private Category category;
    private User user;

}

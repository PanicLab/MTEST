package com.paniclab.services;

import com.paniclab.TaskData;

public interface TaskService<T> {
    void populate(TaskData<T> data);
    TaskData<T> loadPersisted();
}

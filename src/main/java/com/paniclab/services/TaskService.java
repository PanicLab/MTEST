package com.paniclab.services;

public interface TaskService<T> {
    void populate(TaskData<T> data);
    TaskData<T> loadPersisted();
}

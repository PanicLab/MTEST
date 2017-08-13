package com.paniclab.tasks;

import com.paniclab.TaskData;
import com.paniclab.services.MainTaskDataAccessService;
import com.paniclab.services.TaskService;

import java.util.concurrent.Callable;

public class LoadPersistedDataTask implements Callable<TaskData<Integer>> {
    private final String url;

    public LoadPersistedDataTask(String url) {
        this.url = url;
    }

    @Override
    public TaskData<Integer> call() {
        TaskService<Integer> service = new MainTaskDataAccessService(url);
        return service.loadPersisted();
    }
}

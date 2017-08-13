package com.paniclab.tasks;

import com.paniclab.services.TaskData;
import com.paniclab.services.MainTaskDataAccessService;
import com.paniclab.services.TaskService;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class LoadPersistedDataTask implements Callable<TaskData<Integer>> {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final String url;

    public LoadPersistedDataTask(String url) {
        this.url = url;
    }

    @Override
    public TaskData<Integer> call() {
        LOGGER.info("Begin load persisted data task...");
        TaskService<Integer> service = new MainTaskDataAccessService(url);
        return service.loadPersisted();
    }
}

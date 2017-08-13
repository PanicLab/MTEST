package com.paniclab.tasks;

import com.paniclab.services.TaskData;
import com.paniclab.services.MainTaskDataAccessService;
import com.paniclab.services.TaskService;

import java.util.logging.Logger;

public class PopulateDataTask implements Runnable {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final TaskData<Integer> data;
    private final String url;

    public PopulateDataTask(TaskData<Integer> data, String url) {
        this.data = data;
        this.url = url;
    }

    @Override
    public void run() {
        LOGGER.info("Begin populate data task...");
        TaskService<Integer> service = new MainTaskDataAccessService(url);
        service.populate(data);
        LOGGER.info("Populate data task is over.");
    }
}

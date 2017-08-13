package com.paniclab;

import com.paniclab.services.MainTaskDataAccessService;
import com.paniclab.services.TaskService;

public class PopulateDataTask implements Runnable {
    private final TaskData<Integer> data;
    private final String url;

    PopulateDataTask(TaskData<Integer> data, String url) {
        this.data = data;
        this.url = url;
    }

    @Override
    public void run() {
        TaskService<Integer> service = new MainTaskDataAccessService(url);
        service.populate(data);
    }
}

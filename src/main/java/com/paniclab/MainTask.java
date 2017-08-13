package com.paniclab;



import com.paniclab.services.MainTaskDataAccessService;
import com.paniclab.services.TaskService;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;


public class MainTask implements Serializable {
    private static final long TIME_BUDGET = 1000*60*5L;
    private int number;
    private String url;

    public MainTask() {}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.hashCode() != obj.hashCode()) return false;
        if (!(this.getClass().equals(obj.getClass()))) return false;
        MainTask other = MainTask.class.cast(obj);
        return (this.number == other.number) && (this.url.equals(other.url));
    }

    @Override
    public String toString() {
        return "Объект " + getClass().getCanonicalName() + " @" + hashCode() + "." + System.lineSeparator()
                + "Число N = " + number + ". Url = " + url;
    }

    public TaskData<Integer> getTaskData() {
        List<Integer> result = new ArrayList<>(number);
        for (int i = 1 ; i <= number; i++) {
            result.add(i);
        }
        return new TaskDataImpl<>(result);
    }


    public void execute() {
        long start_time = System.currentTimeMillis();
        TaskService<Integer> service = new MainTaskDataAccessService(url);
        TaskData<Integer> data = getTaskData();
        service.populate(data);
        data = service.loadPersisted();

        XMLTask xmlTask = new XMLTask(data);
        xmlTask.run();

        XLSTTask xlstTask = new XLSTTask(Paths.get("files", "1.xml").toString());
        xlstTask.run();
    }
}

package com.paniclab;

import com.paniclab.tasks.*;
import org.w3c.dom.Document;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;


public class MainTask implements Serializable, Callable<Long> {
    private static final String URL = "jdbc:h2:~/M-Test";
    private static final int TIME_BUDGET = 5;
    private static final Path FILE_1_XML = Paths.get("files", "1.xml");
    private final Path FILE_2_XML = Paths.get("files", "2.xml");

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


    public Collection<Integer> getData() {
        List<Integer> result = new ArrayList<>(number);
        for (int i = 1 ; i <= number; i++) {
            result.add(i);
        }
        return result;
    }



    @Override
    public Long call() {

        Runnable populateTask = new PopulateDataTask(getTaskData(), getUrl());
        populateTask.run();

        LoadPersistedDataTask loadTask = new LoadPersistedDataTask(getUrl());
        TaskData<Integer> data = loadTask.call();

        Runnable createTask = new CreateXMLFileTask(data, FILE_1_XML);
        createTask.run();

        Document document = new ParseXMLTask(FILE_1_XML).call();

        Runnable conversionTask = new XLSTConversionTask(document, FILE_2_XML);
        conversionTask.run();

        document = new ParseXMLTask(FILE_2_XML).call();

        SummarizeTask summarizeTask = new SummarizeTask(document);
        return summarizeTask.call();
    }



    public static void main(String[] args) {
        MainTask task = new MainTask();

        task.setNumber(10000);
        task.setUrl(URL);
        if(task.getNumber() >= 1000000) {
            Long result = executeConcurrently(task);
            System.out.println("Sum of elements (parallel) = " + result);

        } else {
            System.out.println("Sum of elements (singe thread) = " + task.call());
        }
    }

    private static Long executeConcurrently(Callable<Long> task) {
        Long result = 0L;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Long> future = executor.submit(task);
        try {
            result = future.get(TIME_BUDGET, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("This task was interrupted.");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("The time for this operation has expired. Task was cancelled.");
            e.printStackTrace();
            future.cancel(true);
        } finally {
            executor.shutdown();
        }
        return result;
    }
}

package com.paniclab;

import java.util.logging.Logger;

public class EntryPoint {
    public static final String URL = "jdbc:h2:~/M-Test";
    private static Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {

        MainTask task = new MainTask();
        logger.info(task.toString());


        task.setNumber(100);
        task.setUrl(URL);
        logger.info(task.toString());

        task.execute();
    }
}

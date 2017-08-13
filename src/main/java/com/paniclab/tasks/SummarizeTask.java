package com.paniclab.tasks;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class SummarizeTask implements Callable<Long> {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Document document;

    public SummarizeTask(Document doc) {
        document = doc;
    }


    @Override
    public Long call() {
        LOGGER.info("Final summarizing operation begin...");
        List<Integer> values;

        NodeList list = document.getElementsByTagName("entry");
        int size = list.getLength();

        values = new ArrayList<>(size);
        String value;
        for (int i = 0; i < size; i++) {
            Node node = list.item(i);
            value = node.getAttributes().getNamedItem("field").getNodeValue();
            //LOGGER.info("Element value:=" + value);
            values.add(Integer.valueOf(value));
        }

        LOGGER.info("Final summarizing operation completed.");
        return values.parallelStream().mapToLong(i -> i).sum();
    }
}

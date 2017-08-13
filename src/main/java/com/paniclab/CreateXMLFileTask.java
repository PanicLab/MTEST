package com.paniclab;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.Logger;

public class CreateXMLFileTask implements Runnable {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Collection<Integer> ENTRIES;
    private final Path path;


    CreateXMLFileTask(TaskData<Integer> taskData, Path path) {
        ENTRIES = taskData.getData();
        this.path = path;
    }


    @Override
    public void run() {
        Document document = createDOMDocument();

        File file = path.toFile();
        LOGGER.info("File path:" + file.getAbsolutePath());

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.transform(new DOMSource(document), new StreamResult(file));
            LOGGER.info("XML document " + path.toString() + " created successfully.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    private Document createDOMDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Element entries = doc.createElement("entries");
        doc.appendChild(entries);

        Element entry;
        for(Integer i: ENTRIES) {
            entry = doc.createElement("entry");
            Element field = doc.createElement("field");
            field.setTextContent(i.toString());
            entry.appendChild(field);
            entries.appendChild(entry);
        }

        return doc;
    }
}

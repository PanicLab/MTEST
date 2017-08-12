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
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Logger;

public class XMLTask {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Collection<Integer> ENTRIES;


    XMLTask(TaskData<Integer> taskData) {
        ENTRIES = taskData.getData();
    }


    public void run() {
        Document document = createXMLDocument();

        File file = Paths.get("files", "1.xml").toFile();
        LOGGER.info("File path:" + file.getAbsolutePath());

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.transform(new DOMSource(document), new StreamResult(file));
            LOGGER.info("XML document created successfully.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    private Document createXMLDocument() {
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
/*        for(int i: ENTRIES) {
            entry = doc.createElement("entry");
            Element field = doc.createElement("field");
            field.setTextContent(String.valueOf(i));
            entry.appendChild(field);
            entries.appendChild(entry);
        }*/

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

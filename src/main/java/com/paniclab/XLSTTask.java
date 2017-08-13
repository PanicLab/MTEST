package com.paniclab;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class XLSTTask {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final String path;

    XLSTTask(String path) {
        this.path = path;
    }

    Document parseXMLFile() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        File file = Paths.get(path).toFile();
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("xml file 1.xml successfully parsed.");
        return doc;
    }

    void run() {
        File out = Paths.get("files", "2.xml").toFile();

        Document document = parseXMLFile();
        NodeList list = document.getElementsByTagName("entry");
        int size = list.getLength();

        String value = "";
        String name = "";
        for (int i = 0; i < size; i++ ) {
            Node node = list.item(i);
            while (node.hasChildNodes()) {
                name = node.getFirstChild().getNodeName();
                LOGGER.info("Child Node name:=" + name);
                value = node.getFirstChild().getTextContent();
                LOGGER.info("Child Node value:=" + value);
                node.removeChild(node.getFirstChild());
            }
            Element.class.cast(node).setAttribute(name, value);
        }

        TransformerFactory factory = TransformerFactory.newInstance();
        DOMSource domSource = new DOMSource(document);
        Transformer transformer = null;

        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        try {
            transformer.transform(domSource, new StreamResult(out));
            LOGGER.info("xml file 2.xml successfully created.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}

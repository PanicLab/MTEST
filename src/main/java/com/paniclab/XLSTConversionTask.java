package com.paniclab;

import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;


public class XLSTConversionTask implements Runnable {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Document document;
    private final Path path;

    XLSTConversionTask(Document doc, Path path) {
        document = doc;
        this.path = path;
    }

    @Override
    public void run() {
        File out = path.toFile();
        NodeList list = document.getElementsByTagName("entry");
        int size = list.getLength();

        String value = "";
        String name = "";
        for (int i = 0; i < size; i++ ) {
            Node node = list.item(i);
            while (node.hasChildNodes()) {
                name = node.getFirstChild().getNodeName();
                //LOGGER.info("Child Node name:=" + name);
                value = node.getFirstChild().getTextContent();
                //LOGGER.info("Child Node value:=" + value);
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
            LOGGER.info("xml file " + path.toString() + " successfully created.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

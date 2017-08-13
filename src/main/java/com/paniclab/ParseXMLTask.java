package com.paniclab;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ParseXMLTask implements Callable<Document> {
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private final Path path;

    public ParseXMLTask(String path) {
        this.path = Paths.get(path);
    }

    public ParseXMLTask(Path path) {
        this.path = path;
    }

    @Override
    public Document call() {
        LOGGER.info("Begin to parse file " + path.toString() + "..." );
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        File file = path.toFile();
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("xml file " + path.toString() + " successfully parsed.");
        return doc;
    }
}

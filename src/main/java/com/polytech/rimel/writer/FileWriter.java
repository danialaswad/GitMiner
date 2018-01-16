package com.polytech.rimel.writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriter implements WriterInterface {

    private final static Logger LOGGER = Logger.getLogger(FileWriter.class.getName());

    private String filename;
    private String source;

    public FileWriter(String filename, String source) {
        this.filename = filename;
        this.source = source;
    }

    public void writeToFile(String data, String path){
        try {
            LOGGER.log(Level.INFO, "Writing data to " + path);
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        writeToFile(source, filename);
    }
}

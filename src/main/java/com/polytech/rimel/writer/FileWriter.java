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

    @Override
    public void write() {
        try {
            LOGGER.log(Level.INFO, "Writing data to " + filename);
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.write(source);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

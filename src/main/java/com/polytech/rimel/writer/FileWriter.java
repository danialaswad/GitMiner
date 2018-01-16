package com.polytech.rimel.writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriter {

    private final static Logger LOGGER = Logger.getLogger(FileWriter.class.getName());
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
}

package com.polytech.rimel.mine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.rimel.git.GitRestClient;
import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.File;
import com.polytech.rimel.writer.FileWriter;
import com.polytech.rimel.writer.SaveFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMiner {

    private final static Logger LOGGER = Logger.getLogger(FileMiner.class.getName());

    private ObjectMapper mapper;
    private String filePath;
    private Path outputPath;

    FileMiner(String filePath, Path outputPath) {
        this.outputPath = outputPath;
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
    }

    void retrieveFileHistory(List<CommitHistory> commitHistories, GitRestClient gitClient){
        for (CommitHistory commitHistory: commitHistories) {
            retrieveFileHistory(commitHistory, gitClient);
        }
    }

    private void retrieveFileHistory(CommitHistory commitHistory, GitRestClient gitClient){
        try {
            LOGGER.log(Level.INFO, "Retrieving the commit " + commitHistory.getSha() + " from github");

            String output = gitClient.retrieveCommit(commitHistory.getUrl());

            CommitHistory cm = mapper.readValue(output,CommitHistory.class);

            retrieveFile(cm, gitClient);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void retrieveFile(CommitHistory commitHistory, GitRestClient gitClient) {
        for (File file : commitHistory.getFiles()) {
            if (file.getFileName().equals(filePath)) {
                try {
                    LOGGER.log(Level.INFO, "Retrieving the source file " + file.getFileName() + " from github");

                    String output = gitClient.retrieveFile(file.getRawUrl());

                    String fileName = file.getFileName().replaceAll("/", "-");

                    // Saving file
                    new SaveFile().execute(new FileWriter(outputPath.toString() + "/" + commitHistory.getCommit().getCommitter().getDate() + fileName, output));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.polytech.rimel.mine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.polytech.rimel.git.GitRestClient;
import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.DockerCompose;
import com.polytech.rimel.model.File;
import com.polytech.rimel.model.Repository;
import com.polytech.rimel.writer.CSVWriter;
import com.polytech.rimel.writer.SaveFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMiner {

    private final static Logger LOGGER = Logger.getLogger(FileMiner.class.getName());

    private ObjectMapper mapper;
    private ObjectMapper yamlMapper;
    private String filePath;
    private CSVWriter writer;

    FileMiner(String filePath, CSVWriter writer) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.writer = writer;
    }

    void retrieveFileHistory(List<CommitHistory> commitHistories, Repository repository, GitRestClient gitClient) {
        for (CommitHistory commitHistory: commitHistories) {
            retrieveFileHistory(commitHistory, repository, gitClient);
        }
    }

    private void retrieveFileHistory(CommitHistory commitHistory, Repository repository, GitRestClient gitClient) {
        try {
            LOGGER.log(Level.INFO, "Retrieving the commit " + commitHistory.getSha() + " from github");

            String output = gitClient.retrieveCommit(commitHistory.getUrl());

            CommitHistory cm = mapper.readValue(output,CommitHistory.class);

            String dockerFile = retrieveFile(cm, gitClient);

            DockerCompose dc = yamlMapper.readValue(dockerFile, DockerCompose.class);
            if (dc.getVersion() == null) {
                dc.setVersion("1");
            }
            new SaveFile().execute(this.writer, repository, cm, dc, dockerFile);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String retrieveFile(CommitHistory commitHistory, GitRestClient gitClient) {
        String output = "";
        for (File file : commitHistory.getFiles()) {
            if (file.getFileName().equals(filePath)) {
                try {
                    LOGGER.log(Level.INFO, "Retrieving the source file " + file.getFileName() + " from github");

                    output = gitClient.retrieveFile(file.getRawUrl());

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return output;
    }
}

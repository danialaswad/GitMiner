package com.polytech.rimel.mine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.rimel.exceptions.GitRestClientException;
import com.polytech.rimel.git.GitRestClient;
import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.Repository;
import com.polytech.rimel.writer.CSVWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Miner {

    private final static Logger LOGGER = Logger.getLogger(Miner.class.getName());

    private GitRestClient gitClient;
    private String owner;
    private String repository;
    private String filePath;
    private CSVWriter csvWriter;

    private ObjectMapper mapper;

    public Miner(){
        this.owner = "";
        this.repository="";
        this.filePath="";
        this.mapper = new ObjectMapper();
        this.gitClient = new GitRestClient();
        this.csvWriter = new CSVWriter();
    }

    public Miner fromOwner(String owner){
        this.owner = owner;
        return this;
    }

    public Miner inRepository(String repository){
        this.repository = repository;
        return this;
    }

    public Miner getFile(String filePath){
        this.filePath = filePath;
        return this;
    }


    public Miner execute(){

        if (owner.equals("")){
            throw new GitRestClientException("Please provide a git owner");
        }
        if (repository.equals("")){
            throw new GitRestClientException("Please provide repository name");
        }
        if (filePath.equals("")){
            throw new GitRestClientException("Please provide file path");
        }

        try {
            LOGGER.log(Level.INFO, "Retrieving commits history for " + filePath + " from " + owner + "/" + repository);

            Repository repo = new Repository(owner, repository, filePath);

            String output = gitClient.retrieveCommits(owner, repository, filePath);

            List<CommitHistory> commitHistoryList = mapper.readValue(output, new TypeReference<List<CommitHistory>>() {
            });

            continueRetrieveCommits(owner, repository, filePath, commitHistoryList);

            new FileMiner(filePath, csvWriter).retrieveFileHistory(commitHistoryList, repo, gitClient);

            LOGGER.log(Level.INFO, "FINISH");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    //TODO
    private void continueRetrieveCommits(String owner, String repository, String filePath, List<CommitHistory> commitHistoryList) {
        try {
            String output = gitClient.retrieveCommits(owner, repository, filePath, commitHistoryList.get(commitHistoryList.size() - 1).getSha());
            List<CommitHistory> tmp = mapper.readValue(output, new TypeReference<List<CommitHistory>>() {
            });
            if (tmp.size() <= 1 && tmp.get(0).getSha().equals(commitHistoryList.get(commitHistoryList.size() - 1).getSha())) {
                return;
            }
            commitHistoryList.addAll(tmp);
            continueRetrieveCommits(owner, repository, filePath, commitHistoryList);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isAccessibility(String outputPath){
        Path path = Paths.get(outputPath);
        return Files.isDirectory(path);
    }
}

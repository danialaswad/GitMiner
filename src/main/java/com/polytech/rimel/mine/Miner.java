package com.polytech.rimel.mine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.rimel.exceptions.GitRestClientException;
import com.polytech.rimel.git.GitRestClient;
import com.polytech.rimel.model.CommitHistory;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Miner {

    private final static Logger LOGGER = Logger.getLogger(Miner.class.getName());
    private static final String BASE_URL = "https://api.github.com/repos/";
    private GitRestClient gitClient;
    private String owner;
    private String repository;
    private String filePath;
    private String outputPath;

    private ObjectMapper mapper;

    public Miner(){
        this.owner = "";
        this.repository="";
        this.filePath="";
        this.outputPath="";
        this.mapper = new ObjectMapper();
        this.gitClient = new GitRestClient();
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
    public Miner toOutput(String outputPath){
        this.outputPath = outputPath;
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
        if (outputPath.equals("")){
            throw new GitRestClientException("Please provide an output path");
        }
        if(!isAccessibility(outputPath)){
            throw new GitRestClientException("Please provide a valid output path");
        }


        try {
            LOGGER.log(Level.INFO, "Retrieving commits history for " + filePath + " from " + owner + "/" + repository);
            HttpURLConnection conn = gitClient.retrieveCommits(owner, repository, filePath);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = IOUtils.toString(br);

            conn.disconnect();

            List<CommitHistory> cm = mapper.readValue(output,new TypeReference<List<CommitHistory>>(){});

            new FileMiner(filePath, outputPath).retrieveFileHistory(cm, gitClient);

            LOGGER.log(Level.INFO, "FINISH");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    private boolean isAccessibility(String outputPath){
        Path path = Paths.get(outputPath);
        return Files.isDirectory(path);
    }
}

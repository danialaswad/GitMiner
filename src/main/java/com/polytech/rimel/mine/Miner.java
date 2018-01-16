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
import java.util.List;

public class Miner {

    private static final String BASE_URL = "https://api.github.com/repos/";
    private GitRestClient gitClient;
    private String owner;
    private String repository;
    private String filePath;

    private ObjectMapper mapper;

    public Miner(){
        this.owner = "";
        this.repository="";
        this.filePath="";
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
            HttpURLConnection conn = gitClient.retrieveCommits(owner, repository, filePath);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = IOUtils.toString(br);

            conn.disconnect();

            List<CommitHistory> repositories = mapper.readValue(output,new TypeReference<List<CommitHistory>>(){});

            new FileMiner().retrieveFileHistory(repositories, gitClient);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

}

package com.polytech.rimel.mine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.rimel.git.GitRestClient;
import com.polytech.rimel.model.CommitHistory;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

public class FileMiner {

    private ObjectMapper mapper;

    public FileMiner(){
        this.mapper = new ObjectMapper();
    }

    public void retrieveFileHistory(List<CommitHistory> commitHistories, GitRestClient gitClient){
        for (CommitHistory commitHistory: commitHistories) {
            retrieveFileHistory(commitHistory, gitClient);
        }
    }

    private void retrieveFileHistory(CommitHistory commitHistory, GitRestClient gitClient){
        try {
            HttpURLConnection conn = gitClient.retrieveCommit(commitHistory.getUrl());

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = IOUtils.toString(br);

            conn.disconnect();

            CommitHistory repositories = mapper.readValue(output,CommitHistory.class);

            System.out.println(repositories.getFiles().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.polytech.rimel.git;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitRestClient {

    private static final String BASE_URL = "https://api.github.com/repos/";

    public GitRestClient(){

    }

    public HttpURLConnection retrieveCommits(String owner, String repository, String filePath) throws IOException {
        URL url = new URL(BASE_URL+owner+"/"+repository+"/commits?="+filePath);
        return establishConnection(url);
    }

    public HttpURLConnection retrieveCommit(String stringURL) throws IOException {
        URL url = new URL(stringURL);
        return establishConnection(url);
    }

    private HttpURLConnection establishConnection(URL url) throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 403){
                return establishConnection(url);
            }

            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        return conn;
    }
}

package com.polytech.rimel.git;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitRestClient {

    private static final String BASE_URL = "https://api.github.com/repos/";
    private static final String GIT_CRED = "client_id=9e0352ec72f276dfc007&client_secret=c995cfbf1359c3762f0526b156c91a03c7eacf7c";

    private final static Logger LOGGER = Logger.getLogger(GitRestClient.class.getName());

    public GitRestClient(){

    }

    public HttpURLConnection retrieveCommits(String owner, String repository, String filePath) throws IOException, InterruptedException {
        URL url = new URL(BASE_URL+owner+"/"+repository+"/commits?="+filePath+"&"+GIT_CRED);
        return establishConnection(url);
    }

    public HttpURLConnection retrieveCommit(String stringURL) throws IOException,InterruptedException {
        URL url = new URL(stringURL+"?"+GIT_CRED);
        return establishConnection(url);
    }

    public HttpURLConnection retrieveFile(String stringURL) throws IOException, InterruptedException {
        URL url = new URL(stringURL);
        return establishConnection(url);
    }

    private HttpURLConnection establishConnection(URL url) throws IOException, InterruptedException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 403){
                LOGGER.log(Level.WARNING, "Unable to connect to github. Response : " + conn.getResponseCode()+ ". System sleep for 60 seconds");
                // Sleep 60 seconds if connection is revoke
                Thread.sleep(60000);
                return establishConnection(url);
            }

            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        return conn;
    }
}

package com.polytech.rimel.git;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class GitRestClient {

    private static final String BASE_URL = "https://api.github.com/repos/";
    private static final String CLIENT_ID= "";
    private static final String CLIENT_SECRET = "";
    private static final String GIT_CRED = "client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+;
    private static final String PER_PAGE = "per_page=100";
    private static final String SHA = "sha=";

    private final static Logger LOGGER = Logger.getLogger(GitRestClient.class.getName());

    public GitRestClient(){

    }

    public String retrieveCommits(String owner, String repository, String filePath) throws IOException, InterruptedException {
        return establishConnection(BASE_URL+owner+"/"+repository+"/commits?path="+filePath+"&"+PER_PAGE+"&"+GIT_CRED);
    }

    public String retrieveCommits(String owner, String repository, String filePath, String sha) throws IOException, InterruptedException {
        return establishConnection(BASE_URL+owner+"/"+repository+"/commits?path="+filePath+"&"+PER_PAGE+"&"+SHA+sha+"&"+GIT_CRED);
    }

    public String retrieveCommit(String stringURL) throws IOException,InterruptedException {
        return establishConnection(stringURL+"?"+GIT_CRED);
    }

    public String retrieveFile(String stringURL) throws IOException, InterruptedException {
        return establishConnection(stringURL);
    }

    private String establishConnection(String url) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/javascript")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "4d2b8b25-2b29-fed6-f125-434710ae15e1")
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

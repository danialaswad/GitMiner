package com.polytech.rimel;

import com.polytech.rimel.writer.FileWriter;
import com.squareup.okhttp.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        try {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/laradock/laradock/commits?path=docker-compose.yml&per_page=100&client_id=9e0352ec72f276dfc007&client_secret=c995cfbf1359c3762f0526b156c91a03c7eacf7c")
                .get()
                .addHeader("content-type", "application/javascript")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "4d2b8b25-2b29-fed6-f125-434710ae15e1")
                .build();


            Response response = client.newCall(request).execute();
            new FileWriter().writeToFile(response.body().string(), "/Users/danial/Desktop/tmp/test2.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

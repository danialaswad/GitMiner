package com.polytech.rimel;

import com.polytech.rimel.mine.GitFileMiner;
import com.polytech.rimel.mine.GitMiner;
import com.polytech.rimel.mine.Miner;

public class MyMiner implements GitMiner{

    public static void main(String[] args) {

        new GitFileMiner().start(new MyMiner());
    }

    @Override
    public void execute() {
        new Miner()
                .fromOwner("scipio3000")
                .inRepository("polytech-soa")
                .getFile("integration/docker-compose.yml")
                .toOutput("/Users/danial/Desktop/tmp/soa")
                .execute();
    }
}

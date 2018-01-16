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
                .fromOwner("danialaswad")
                .inRepository("SI4-S8-FSM")
                .getFile("README.md")
                .execute();
    }
}

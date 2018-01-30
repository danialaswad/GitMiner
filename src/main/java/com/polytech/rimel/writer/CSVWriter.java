package com.polytech.rimel.writer;

import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.DockerCompose;
import com.polytech.rimel.model.Repository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVWriter implements WriterInterface {
    private static final String SAMPLE_CSV_FILE = "analyse.csv";
    private CSVPrinter csvPrinter;

    public CSVWriter() {
        try {

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
            this.csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("owner",
                            "repository-name",
                            "committer-name",
                            "commit-date",
                            "path-to-docker-file",
                            "docker-version",
                            "content"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Repository repository, CommitHistory cm, DockerCompose compose, String dockerFile) {
        try {
            this.csvPrinter.printRecord(repository.getOwner(), repository.getRepository(),
                    cm.getCommit().getCommitter().getName(), cm.getCommit().getCommitter().getDate(),
                    repository.getPath(), compose.getVersion(),
                    dockerFile);

            this.csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

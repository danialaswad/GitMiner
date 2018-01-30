package com.polytech.rimel.writer;

import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.DockerCompose;
import com.polytech.rimel.model.Repository;

public class SaveFile {
    public SaveFile() {
        // Nothing at the moment
    }

    public void execute(WriterInterface writer, Repository repository, CommitHistory cm, DockerCompose dockerCompose, String dockerFile) {
        writer.write(repository, cm, dockerCompose, dockerFile);
    }
}

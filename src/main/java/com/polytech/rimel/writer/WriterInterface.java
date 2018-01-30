package com.polytech.rimel.writer;

import com.polytech.rimel.model.CommitHistory;
import com.polytech.rimel.model.DockerCompose;
import com.polytech.rimel.model.Repository;

public interface WriterInterface {

    void write(Repository repository, CommitHistory cm, DockerCompose compose, String dockerFile);
}

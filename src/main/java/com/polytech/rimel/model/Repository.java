package com.polytech.rimel.model;

public class Repository {
    private String owner;
    private String repository;
    private String path;

    public Repository(String owner, String repository, String path) {
        this.owner = owner;
        this.repository = repository;
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

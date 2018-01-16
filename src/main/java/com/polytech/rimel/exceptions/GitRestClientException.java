package com.polytech.rimel.exceptions;

public class GitRestClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GitRestClientException(String msg) {
        super(msg);
    }

    public GitRestClientException(Exception e) {
        super(e);
    }
}

package com.example.start_jobs.exceptions;

public class StatusAlreadyExistsException extends RuntimeException {
    public StatusAlreadyExistsException(String message) {
        super(message);
    }
}

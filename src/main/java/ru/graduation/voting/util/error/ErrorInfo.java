package ru.graduation.voting.util.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorInfo {

    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String[] details;
    private final String path;

    public ErrorInfo(HttpStatus status, String path, String... details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.details = details;
        this.path = path;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

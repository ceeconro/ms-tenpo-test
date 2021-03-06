package org.tenpo.test.mstenpotest.http;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class HttpErrorDetails {
    private final ZonedDateTime timestamp;
    private final String path;
    private final HttpStatus httpStatus;
    private final String message;


    public HttpErrorDetails(HttpStatus httpStatus, String path, String message) {
        timestamp = ZonedDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }

    public HttpErrorDetails(HttpStatus httpStatus, String path, String message, ZonedDateTime now) {
        timestamp = now;
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getError() {
        return httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }
}

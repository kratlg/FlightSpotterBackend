package com.flightspotter.exception;

import com.flightspotter.enums.AppMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

    private final AppMessage appMessage;
    private final HttpStatus status;

    public AppException(AppMessage appMessage, HttpStatus status) {
        super(appMessage.getMessage());
        this.appMessage = appMessage;
        this.status = status;
    }

    public AppException(AppMessage appMessage) {
        this(appMessage, HttpStatus.BAD_REQUEST);
    }

    public static AppException notFound(AppMessage message) {
        return new AppException(message, HttpStatus.NOT_FOUND);
    }

    public static AppException unauthorized(AppMessage message) {
        return new AppException(message, HttpStatus.UNAUTHORIZED);
    }

    public static AppException forbidden(AppMessage message) {
        return new AppException(message, HttpStatus.FORBIDDEN);
    }

    public static AppException conflict(AppMessage message) {
        return new AppException(message, HttpStatus.CONFLICT);
    }

    public static AppException badRequest(AppMessage message) {
        return new AppException(message, HttpStatus.BAD_REQUEST);
    }

    public static AppException internalError(AppMessage message) {
        return new AppException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

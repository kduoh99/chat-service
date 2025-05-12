package com.study.chatserver.global.error.exception;

public abstract class InvalidGroupException extends RuntimeException {
    public InvalidGroupException(String message) {
        super(message);
    }
}

package com.team.hairdresser.utils.util.exception;

public class NullObjectException extends RuntimeException {
    public NullObjectException(String message) {
        super("NullObjectException : " + message);
    }
}

package com.team.hairdresser.utils.util.exception;

public class PasswordNotGenerateException extends RuntimeException {
    public PasswordNotGenerateException(String message) {
        super("PasswordNotGenerateException : " + message);
    }
}
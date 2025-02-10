package com.joshua.fantasyunleashedapi.utils;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}

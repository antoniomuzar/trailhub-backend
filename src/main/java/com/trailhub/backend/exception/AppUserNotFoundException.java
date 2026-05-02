package com.trailhub.backend.exception;

public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(String userEmail) {
        super("User not found with email= %s".formatted(userEmail));
    }
}
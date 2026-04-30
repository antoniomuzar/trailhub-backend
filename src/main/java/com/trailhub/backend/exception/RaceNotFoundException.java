package com.trailhub.backend.exception;

public class RaceNotFoundException extends RuntimeException{

    public RaceNotFoundException(Long id){
        super("Race not found with id= %d".formatted(id));
    }
}

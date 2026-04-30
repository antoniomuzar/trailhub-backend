package com.trailhub.backend.exception;

public class RaceEntryAlreadyExistsException extends RuntimeException{

    public RaceEntryAlreadyExistsException(Long raceId, Long userId){
        super("Race entry already exists for race id= %d and user id= %d".formatted(raceId,userId));
    }
}
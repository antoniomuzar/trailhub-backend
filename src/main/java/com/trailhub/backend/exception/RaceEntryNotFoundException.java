package com.trailhub.backend.exception;

public class RaceEntryNotFoundException extends RuntimeException{

    public RaceEntryNotFoundException(Long raceId, Long userId){
        super("Race entry not found for race id= %d and user id= %d".formatted(raceId,userId));
    }
}
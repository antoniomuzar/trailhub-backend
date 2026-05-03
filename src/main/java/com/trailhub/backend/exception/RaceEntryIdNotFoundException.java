package com.trailhub.backend.exception;

public class RaceEntryIdNotFoundException extends RuntimeException {

    public RaceEntryIdNotFoundException(Long raceId, Long entryId) {
        super("Race entry not found for race id=%d and entry id=%d".formatted(raceId, entryId));
    }
}

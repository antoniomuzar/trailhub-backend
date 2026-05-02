package com.trailhub.backend.controller;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.service.RaceEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/races/{raceId}/entries")
@Validated
@Slf4j
public class RaceEntryController {

    private final RaceEntryService raceEntryService;

    public RaceEntryController(RaceEntryService raceEntryService) {
        this.raceEntryService = raceEntryService;
    }

    @PostMapping("/me")
    public ResponseEntity<EntryResponseDto> joinRace(@PathVariable Long raceId, Authentication authentication){

        String userEmail = authentication.getName();
        log.debug("Joining race: {}, user: {}", raceId, userEmail);

        EntryResponseDto savedEntry = raceEntryService.joinRace(raceId,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @GetMapping
    public ResponseEntity<Page<EntryResponseDto>> getAllEntries( @PathVariable Long raceId, @ParameterObject Pageable pageable){
        log.debug("Fetching entries for race: {}, pageable: {}", raceId, pageable);

        Page<EntryResponseDto> entries = raceEntryService.getEntriesByRace(pageable, raceId);
        return ResponseEntity.ok(entries);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> leaveRace(@PathVariable Long raceId, Authentication authentication){
        String userEmail = authentication.getName();
        log.debug("Leaving race: {}, user: {}", raceId, userEmail);

        raceEntryService.leaveRace(raceId,userEmail);
        return ResponseEntity.noContent().build();
    }
}

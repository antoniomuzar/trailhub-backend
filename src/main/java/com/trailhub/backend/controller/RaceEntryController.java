package com.trailhub.backend.controller;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.service.RaceEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/races/{raceId}/entries")
@Validated
@Slf4j
public class RaceEntryController {

    private final RaceEntryService raceEntryService;

    public RaceEntryController(RaceEntryService raceEntryService) {
        this.raceEntryService = raceEntryService;
    }

    @GetMapping("/{entryId:\\d+}")
    public ResponseEntity<EntryResponseDto> getEntryById(@PathVariable Long raceId, @PathVariable Long entryId) {
        return ResponseEntity.ok(raceEntryService.getEntryById(raceId, entryId));
    }

    @PostMapping("/me")
    public ResponseEntity<EntryResponseDto> joinRace(@PathVariable Long raceId, Authentication authentication) {

        String userEmail = authentication.getName();

        EntryResponseDto savedEntry = raceEntryService.joinRace(raceId, userEmail);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/races/{raceId}/entries/{entryId}")
                .buildAndExpand(raceId, savedEntry.getEntryId())
                .toUri();
        return ResponseEntity.created(location).body(savedEntry);
    }

    @GetMapping
    public ResponseEntity<Page<EntryResponseDto>> getAllEntries(@PathVariable Long raceId, @ParameterObject Pageable pageable) {

        Page<EntryResponseDto> entries = raceEntryService.getEntriesByRace(pageable, raceId);
        return ResponseEntity.ok(entries);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> leaveRace(@PathVariable Long raceId, Authentication authentication) {
        String userEmail = authentication.getName();

        raceEntryService.leaveRace(raceId, userEmail);
        return ResponseEntity.noContent().build();
    }
}

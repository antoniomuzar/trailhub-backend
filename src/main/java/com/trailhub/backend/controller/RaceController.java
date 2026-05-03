package com.trailhub.backend.controller;

import com.trailhub.backend.dto.race.RaceRequestDto;
import com.trailhub.backend.dto.race.RaceResponseDto;
import com.trailhub.backend.service.RaceService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/races")
@Validated
@Slf4j
public class RaceController {

    private final RaceService raceService;


    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @PostMapping
    public ResponseEntity<RaceResponseDto> createRace(@Valid @RequestBody RaceRequestDto raceRequestDto) {

        RaceResponseDto savedRace = raceService.createRace(raceRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRace.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedRace);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<RaceResponseDto>> getMyRaces(Authentication authentication,
                                                            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(raceService.getRacesForCurrentUser(authentication.getName(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDto> getRaceById(@PathVariable Long id) {

        RaceResponseDto race = raceService.getRaceById(id);
        return ResponseEntity.ok(race);
    }

    @GetMapping
    public ResponseEntity<Page<RaceResponseDto>> getAllRaces(@ParameterObject Pageable pageable) {

        Page<RaceResponseDto> races = raceService.getAllRaces(pageable);
        return ResponseEntity.ok(races);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponseDto> updateRace(@PathVariable Long id, @Valid @RequestBody RaceRequestDto raceRequestDto) {

        RaceResponseDto updatedRace = raceService.updateRace(id, raceRequestDto);
        return ResponseEntity.ok(updatedRace);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {

        raceService.deleteRaceById(id);
        return ResponseEntity.noContent().build();
    }
}
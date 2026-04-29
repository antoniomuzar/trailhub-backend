package com.trailhub.backend.controller;

import com.trailhub.backend.dto.race.RaceRequestDto;
import com.trailhub.backend.dto.race.RaceResponseDto;
import com.trailhub.backend.service.RaceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        log.info("Creating race: {}", raceRequestDto);

        RaceResponseDto savedRace = raceService.createRace(raceRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRace);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDto> getRaceById(@PathVariable Long id){
        log.info("Fetching race by id: {}" , id);

        RaceResponseDto race = raceService.getRaceById(id);

        return  ResponseEntity.ok(race);
    }

    @GetMapping
    public ResponseEntity<Page<RaceResponseDto>> getAllRaces(Pageable pageable){
        log.debug("Fetching all races with pagination: {}", pageable);

        Page<RaceResponseDto> races = raceService.getAllRaces(pageable);
        return ResponseEntity.ok(races);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponseDto> updateRace( @PathVariable Long id, @Valid @RequestBody RaceRequestDto raceRequestDto){
        log.info("Update race: {}", id);

        RaceResponseDto updatedRace = raceService.updateRace(id,raceRequestDto);

        return ResponseEntity.ok(updatedRace);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id){
        log.info("Deleting race: {}", id);

        raceService.deleteRaceById(id);
        return ResponseEntity.noContent().build();
    }
}
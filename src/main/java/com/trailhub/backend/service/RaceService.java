package com.trailhub.backend.service;

import com.trailhub.backend.dto.race.RaceRequestDto;
import com.trailhub.backend.dto.race.RaceResponseDto;
import com.trailhub.backend.exception.RaceNotFoundException;
import com.trailhub.backend.mapper.RaceMapper;
import com.trailhub.backend.model.Race;
import com.trailhub.backend.repository.RaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RaceService {


    private final RaceRepository raceRepository;
    private final RaceMapper raceMapper;

    public RaceService(RaceRepository raceRepository, RaceMapper raceMapper) {
        this.raceRepository = raceRepository;
        this.raceMapper = raceMapper;
    }

    public RaceResponseDto createRace(RaceRequestDto raceRequestDto) {
        log.info("Creating race: {}", raceRequestDto);

        Race race = raceMapper.toEntity(raceRequestDto);
        Race savedRace = raceRepository.save(race);
        return raceMapper.toDto(savedRace);
    }

    public RaceResponseDto updateRace(Long id, RaceRequestDto raceRequestDto) {
        log.info("Update race id: {}", id);

        Race race = getRaceOrThrow(id);

        raceMapper.updateEntity(race, raceRequestDto);

        Race updatedRace = raceRepository.save(race);
        return raceMapper.toDto(updatedRace);


    }

    public RaceResponseDto getRaceById(Long id) {
        log.info("Fetching race by id: {}", id);

        Race race = getRaceOrThrow(id);
        return raceMapper.toDto(race);

    }

    public Page<RaceResponseDto> getAllRaces(Pageable pageable) {
        log.debug("Fetching all races with pagination: {}", pageable);

        Page<Race> racePage = raceRepository.findAll(pageable);

        return racePage.map(raceMapper::toDto);
    }

    public void deleteRaceById(Long id) {
        log.info("Deleting race by id: {}", id);

        Race race = getRaceOrThrow(id);
        raceRepository.delete(race);
    }

    private Race getRaceOrThrow(Long raceId) {

        return raceRepository.findById(raceId)
                .orElseThrow(() -> new RaceNotFoundException(raceId));
    }
}

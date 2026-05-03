package com.trailhub.backend.service;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.exception.AppUserNotFoundException;
import com.trailhub.backend.exception.RaceEntryAlreadyExistsException;
import com.trailhub.backend.exception.RaceEntryIdNotFoundException;
import com.trailhub.backend.exception.RaceEntryNotFoundException;
import com.trailhub.backend.exception.RaceNotFoundException;
import com.trailhub.backend.mapper.RaceEntryMapper;
import com.trailhub.backend.model.AppUser;
import com.trailhub.backend.model.Race;
import com.trailhub.backend.model.RaceEntry;
import com.trailhub.backend.repository.AppUserRepository;
import com.trailhub.backend.repository.RaceEntryRepository;
import com.trailhub.backend.repository.RaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
@Slf4j
public class RaceEntryService {

    private final RaceRepository raceRepository;
    private final RaceEntryRepository raceEntryRepository;
    private final AppUserRepository appUserRepository;
    private final RaceEntryMapper raceEntryMapper;


    public RaceEntryService(RaceRepository raceRepository, RaceEntryRepository raceEntryRepository, AppUserRepository appUserRepository, RaceEntryMapper raceEntryMapper) {
        this.raceRepository = raceRepository;
        this.raceEntryRepository = raceEntryRepository;
        this.appUserRepository = appUserRepository;
        this.raceEntryMapper = raceEntryMapper;
    }

    public EntryResponseDto joinRace(Long raceId, String userEmail) {
        log.info("Join race: raceId={}, user={}", raceId, userEmail);

        Race race = getRaceOrThrow(raceId);
        AppUser appUser = getUserOrThrow(userEmail);

        if (raceEntryRepository.existsByRaceIdAndAppUserId(raceId, appUser.getId())) {
            throw new RaceEntryAlreadyExistsException(raceId, appUser.getId());
        }
        RaceEntry raceEntry = new RaceEntry();
        raceEntry.setRace(race);
        raceEntry.setAppUser(appUser);
        raceEntry.setCreatedAt(LocalDateTime.now());

        RaceEntry savedEntry = raceEntryRepository.save(raceEntry);
        log.info("User joined race: raceId={}, entryId={}, user={}", raceId, savedEntry.getId(), userEmail);
        return raceEntryMapper.toDto(savedEntry);
    }

    @Transactional(readOnly = true)
    public EntryResponseDto getEntryById(Long raceId, Long entryId) {
        log.info("Fetching entry: raceId={}, entryId={}", raceId, entryId);
        getRaceOrThrow(raceId);
        RaceEntry entry = raceEntryRepository.findByIdAndRaceId(entryId, raceId)
                .orElseThrow(() -> new RaceEntryIdNotFoundException(raceId, entryId));
        return raceEntryMapper.toDto(entry);
    }

    public void leaveRace(Long raceId, String userEmail) {
        log.info("Leave race: raceId={}, user={}", raceId, userEmail);

        getRaceOrThrow(raceId);
        AppUser appUser = getUserOrThrow(userEmail);

        if (!raceEntryRepository.existsByRaceIdAndAppUserId(raceId, appUser.getId())) {
            throw new RaceEntryNotFoundException(raceId, appUser.getId());
        }
        raceEntryRepository.deleteByRaceIdAndAppUserId(raceId, appUser.getId());
        log.info("User left race: raceId={}, user={}", raceId, userEmail);
    }

    @Transactional(readOnly = true)
    public Page<EntryResponseDto> getEntriesByRace(Pageable pageable, Long raceId) {
        log.debug("Fetching entries for race: raceId={}, pageable={}", raceId, pageable);

        getRaceOrThrow(raceId);

        Page<RaceEntry> entryPage = raceEntryRepository.findByRaceId(raceId, pageable);
        return entryPage.map(raceEntryMapper::toDto);
    }

    private Race getRaceOrThrow(Long raceId) {

        return raceRepository.findById(raceId)
                .orElseThrow(() -> new RaceNotFoundException(raceId));
    }

    private AppUser getUserOrThrow(String userEmail) {

        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppUserNotFoundException(userEmail));
    }
}

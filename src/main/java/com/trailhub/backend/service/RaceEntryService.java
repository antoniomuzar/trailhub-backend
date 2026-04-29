package com.trailhub.backend.service;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.mapper.RaceEntryMapper;
import com.trailhub.backend.model.AppUser;
import com.trailhub.backend.model.Race;
import com.trailhub.backend.model.RaceEntry;
import com.trailhub.backend.repository.AppUserRepository;
import com.trailhub.backend.repository.RaceEntryRepository;
import com.trailhub.backend.repository.RaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
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

      Race race= getRaceOrThrow(raceId);
      AppUser appUser= getUserOrThrow(userEmail);

        if (raceEntryRepository.existsByRaceIdAndAppUserId(raceId, appUser.getId())) {
            throw new RuntimeException();
        }
        RaceEntry raceEntry = new RaceEntry();
        raceEntry.setRace(race);
        raceEntry.setAppUser(appUser);
        raceEntry.setCreatedAt(LocalDateTime.now());

        RaceEntry savedEntry = raceEntryRepository.save(raceEntry);
        return raceEntryMapper.toDto(savedEntry);
    }

    public void leaveRace(Long raceId, String userEmail){

        getRaceOrThrow(raceId);
        AppUser appUser= getUserOrThrow(userEmail);

        if(!raceEntryRepository.existsByRaceIdAndAppUserId(raceId, appUser.getId())){
            throw new RuntimeException();
        }
        raceEntryRepository.deleteByRaceIdAndAppUserId(raceId, appUser.getId());
    }

    public Page<EntryResponseDto> getEntriesByRace(Pageable pageable, Long raceId){

         getRaceOrThrow(raceId);

        Page<RaceEntry> entryPage = raceEntryRepository.findByRaceId(raceId, pageable);
        return entryPage.map(raceEntryMapper::toDto);
    }

    private Race getRaceOrThrow(Long raceId){

        return  raceRepository.findById(raceId)
                .orElseThrow(RuntimeException::new);
    }

    private AppUser getUserOrThrow(String userEmail){

        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(RuntimeException::new);
    }
}

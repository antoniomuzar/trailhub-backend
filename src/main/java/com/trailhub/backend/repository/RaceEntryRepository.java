package com.trailhub.backend.repository;

import com.trailhub.backend.model.RaceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, Long> {

    boolean existsByRaceIdAndAppUserId(Long raceId, Long appUserId);

    void deleteByRaceIdAndAppUserId(Long raceId, Long appUserId);

    List<RaceEntry> findByRaceId(Long raceId);

    List<RaceEntry> findByAppUserId(Long appUserId);
}

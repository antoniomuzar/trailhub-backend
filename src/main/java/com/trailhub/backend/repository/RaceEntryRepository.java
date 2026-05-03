package com.trailhub.backend.repository;

import com.trailhub.backend.model.RaceEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, Long> {

    @EntityGraph(attributePaths = {"appUser", "race"})
    Optional<RaceEntry> findByIdAndRaceId(Long id, Long raceId);

    @EntityGraph(attributePaths = {"race"})
    Page<RaceEntry> findByAppUser_IdOrderByCreatedAtDesc(Long appUserId, Pageable pageable);

    boolean existsByRaceIdAndAppUserId(Long raceId, Long appUserId);

    void deleteByRaceIdAndAppUserId(Long raceId, Long appUserId);

    @EntityGraph(attributePaths = {"appUser", "race"})
    Page<RaceEntry> findByRaceId(Long raceId, Pageable pageable);
}

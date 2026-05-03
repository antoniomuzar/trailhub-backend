package com.trailhub.backend.mapper;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.dto.entry.ParticipantDto;
import com.trailhub.backend.model.AppUser;
import com.trailhub.backend.model.RaceEntry;
import org.springframework.stereotype.Component;

@Component
public class RaceEntryMapper {

    public EntryResponseDto toDto(RaceEntry entry) {
        EntryResponseDto dto = new EntryResponseDto();
        dto.setEntryId(entry.getId());
        dto.setRaceId(entry.getRace().getId());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setParticipant(toParticipantDto(entry.getAppUser()));
        return dto;
    }

    private ParticipantDto toParticipantDto(AppUser user) {
        ParticipantDto p = new ParticipantDto();
        p.setUserId(user.getId());
        p.setFirstName(user.getFirstName());
        p.setLastName(user.getLastName());
        p.setBirthDate(user.getBirthDate());
        return p;
    }
}

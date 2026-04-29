package com.trailhub.backend.mapper;

import com.trailhub.backend.dto.entry.EntryResponseDto;
import com.trailhub.backend.model.RaceEntry;
import org.springframework.stereotype.Component;

@Component
public class RaceEntryMapper {

    public EntryResponseDto toDto(RaceEntry entry){
        EntryResponseDto dto = new EntryResponseDto();
        dto.setEntryId(entry.getId());
        dto.setRaceId(entry.getRace().getId());
        dto.setUserId(entry.getAppUser().getId());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
}

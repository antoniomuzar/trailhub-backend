package com.trailhub.backend.mapper;

import com.trailhub.backend.dto.race.RaceRequestDto;
import com.trailhub.backend.dto.race.RaceResponseDto;
import com.trailhub.backend.model.Race;
import org.springframework.stereotype.Component;

@Component
public class RaceMapper {


    public Race toEntity(RaceRequestDto dto) {
        Race race = new Race();
        race.setName(dto.getName());
        race.setDistance(dto.getDistance());
        return race;
    }

    public RaceResponseDto toDto(Race race) {
        RaceResponseDto dto = new RaceResponseDto();
        dto.setId(race.getId());
        dto.setName(race.getName());
        dto.setDistance(race.getDistance());
        return dto;
    }

    public void updateEntity(Race race, RaceRequestDto dto) {
        race.setName(dto.getName());
        race.setDistance(dto.getDistance());
    }
}

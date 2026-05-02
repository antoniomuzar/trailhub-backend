package com.trailhub.backend.mapper;

import com.trailhub.backend.dto.race.RaceRequestDto;
import com.trailhub.backend.dto.race.RaceResponseDto;
import com.trailhub.backend.model.Distance;
import com.trailhub.backend.model.Race;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RaceMapperTest {

    private final RaceMapper mapper = new RaceMapper();

    @Test
    void toEntity_mapsNameAndDistance() {
        RaceRequestDto dto = new RaceRequestDto();
        dto.setName("Coastal 10");
        dto.setDistance(Distance.TEN_K);

        Race race = mapper.toEntity(dto);

        assertThat(race.getName()).isEqualTo("Coastal 10");
        assertThat(race.getDistance()).isEqualTo(Distance.TEN_K);
        assertThat(race.getId()).isNull();
    }

    @Test
    void toDto_mapsAllFields() {
        Race race = new Race();
        race.setId(42L);
        race.setName("Night Loop");
        race.setDistance(Distance.HALF_MARATHON);

        RaceResponseDto dto = mapper.toDto(race);

        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getName()).isEqualTo("Night Loop");
        assertThat(dto.getDistance()).isEqualTo(Distance.HALF_MARATHON);
    }

    @Test
    void updateEntity_appliesDtoValues() {
        Race race = new Race();
        race.setId(1L);
        race.setName("Old");
        race.setDistance(Distance.FIVE_K);

        RaceRequestDto dto = new RaceRequestDto();
        dto.setName("New");
        dto.setDistance(Distance.MARATHON);

        mapper.updateEntity(race, dto);

        assertThat(race.getId()).isEqualTo(1L);
        assertThat(race.getName()).isEqualTo("New");
        assertThat(race.getDistance()).isEqualTo(Distance.MARATHON);
    }
}

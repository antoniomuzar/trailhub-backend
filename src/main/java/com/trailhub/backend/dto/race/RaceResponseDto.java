package com.trailhub.backend.dto.race;

import com.trailhub.backend.model.Distance;
import lombok.Data;

@Data
public class RaceResponseDto {

    private Long id;
    private String name;
    private Distance distance;
}

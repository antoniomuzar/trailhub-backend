package com.trailhub.backend.dto.race;

import com.trailhub.backend.model.Distance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RaceRequestDto {


    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Distance is required")
    private Distance distance;
}

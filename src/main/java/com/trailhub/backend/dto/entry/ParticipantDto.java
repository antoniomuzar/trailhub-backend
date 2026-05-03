package com.trailhub.backend.dto.entry;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipantDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}

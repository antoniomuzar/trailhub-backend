package com.trailhub.backend.dto.entry;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntryResponseDto {

    private Long entryId;
    private Long raceId;
    private Long userId;
    private LocalDateTime createdAt;
}

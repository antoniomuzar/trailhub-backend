package com.trailhub.backend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime timestamp, int status) {
}
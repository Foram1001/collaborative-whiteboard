package io.github.foram1001.collaborative_whiteboard.auth.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String email,
        OffsetDateTime createdAt
) {}
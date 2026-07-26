package io.github.foram1001.collaborative_whiteboard.board.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BoardResponse(
        UUID id,
        String name,
        String description,
        UUID ownerId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

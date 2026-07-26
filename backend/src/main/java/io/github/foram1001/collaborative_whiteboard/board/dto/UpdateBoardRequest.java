package io.github.foram1001.collaborative_whiteboard.board.dto;

import jakarta.validation.constraints.Size;

public record UpdateBoardRequest(
        @Size(max = 255, message = "Board name must be under 255 characters")
        String name,

        @Size(max = 2000, message = "Description must be under 2000 characters")
        String description
) {
}

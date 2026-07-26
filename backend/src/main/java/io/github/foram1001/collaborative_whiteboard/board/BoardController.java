package io.github.foram1001.collaborative_whiteboard.board;

import io.github.foram1001.collaborative_whiteboard.board.dto.CreateBoardRequest;
import io.github.foram1001.collaborative_whiteboard.board.dto.BoardResponse;
import io.github.foram1001.collaborative_whiteboard.board.dto.UpdateBoardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @RequestBody @Valid CreateBoardRequest request,
            @AuthenticationPrincipal UUID ownerId) {

        Board board = boardService.createBoard(request.name(), request.description(), ownerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(board));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable UUID id, @AuthenticationPrincipal UUID requesterId) {

        Board board = boardService.getBoardById(id, requesterId);

        return ResponseEntity.ok(toResponse(board));
    }

    @GetMapping
    public ResponseEntity<Page<BoardResponse>> listBoards(
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC)
            Pageable pageable, @AuthenticationPrincipal UUID ownerId) {

        Page<Board> boards = boardService.listBoards(ownerId, pageable);

        Page<BoardResponse> response = boards.map(this::toResponse);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBoardRequest request,
            @AuthenticationPrincipal UUID requesterId) {

        Board board = boardService.updateBoard(id, requesterId, request.name(), request.description());

        return ResponseEntity.ok(toResponse(board));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID id, @AuthenticationPrincipal UUID requesterId) {

        boardService.deleteBoard(id, requesterId);

        return ResponseEntity.noContent().build();
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getOwnerId(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
package io.github.foram1001.collaborative_whiteboard.board;

import io.github.foram1001.collaborative_whiteboard.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(String name, String description, UUID ownerId) {
        Board board = new Board(name, description, ownerId);
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board getBoardById(UUID boardId, UUID requesterId) {
        return boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(requesterId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    @Transactional(readOnly = true)
    public Page<Board> listBoards(UUID ownerId, Pageable pageable) {
        return boardRepository.findAllByOwnerIdOrderByUpdatedAtDesc(ownerId, pageable);
    }

    @Transactional
    public Board updateBoard(UUID boardId, UUID requesterId, String name, String description) {
        Board board = getBoardById(boardId, requesterId); // reuses our IDOR-safe lookup

        if (name != null && !name.isBlank()) {
            board.setName(name);
        }

        if (description != null) {
            board.setDescription(description);
        }

        return board; // no explicit save() call!
    }

    @Transactional
    public void deleteBoard(UUID boardId, UUID requesterId) {
        Board board = getBoardById(boardId, requesterId);
        board.markDeleted();
    }
}
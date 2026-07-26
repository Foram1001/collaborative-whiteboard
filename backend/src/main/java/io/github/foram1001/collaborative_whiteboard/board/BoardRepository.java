package io.github.foram1001.collaborative_whiteboard.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    Page<Board> findAllByOwnerIdOrderByUpdatedAtDesc(UUID ownerId, Pageable pageable);

}
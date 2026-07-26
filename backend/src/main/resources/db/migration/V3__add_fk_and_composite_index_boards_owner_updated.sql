DROP INDEX IF EXISTS idx_boards_owner_id;

CREATE INDEX idx_boards_owner_updated ON boards(owner_id, updated_at DESC);

ALTER TABLE boards
ADD CONSTRAINT fk_boards_owner
FOREIGN KEY (owner_id) REFERENCES users(id);
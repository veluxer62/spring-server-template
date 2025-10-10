CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS board
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    title      VARCHAR   NOT NULL,
    content    VARCHAR   NOT NULL
);

COMMENT ON TABLE board IS '게시판';
COMMENT ON COLUMN board.id IS 'ID';
COMMENT ON COLUMN board.created_at IS '등록일시';
COMMENT ON COLUMN board.updated_at IS '수정일시';
COMMENT ON COLUMN board.title IS '제목';
COMMENT ON COLUMN board.content IS '내용';

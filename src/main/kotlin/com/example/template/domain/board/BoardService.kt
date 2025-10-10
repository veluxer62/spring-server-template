package com.example.template.domain.board

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class BoardService(
    private val boardRepository: BoardRepository,
) {
    @Transactional
    fun create(data: BoardCreationData): Board = boardRepository.save(data.toEntity())

    @Transactional
    fun getById(id: UUID): Board = boardRepository.findById(id).orElseThrow()

    @Transactional
    fun update(data: BoardUpdateData): Board =
        getById(data.id)
            .apply { update(data.title, data.content) }

    @Transactional
    fun deleteById(id: UUID) {
        boardRepository.deleteById(id)
    }

    fun getAllByFilter(
        filter: BoardFilter,
        pageable: Pageable,
    ): Page<Board> = boardRepository.findAll(filter, pageable)
}

data class BoardCreationData(
    val title: String,
    val content: String,
) {
    fun toEntity() = Board(title, content)
}

data class BoardUpdateData(
    val id: UUID,
    val title: String,
    val content: String,
)

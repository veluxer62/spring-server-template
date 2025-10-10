package com.example.template.application.facade

import com.example.template.domain.board.BoardCreationData
import com.example.template.domain.board.BoardFilter
import com.example.template.domain.board.BoardService
import com.example.template.domain.board.BoardUpdateData
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BoardFacade(
    private val boardService: BoardService,
) {
    fun create(data: BoardCreationData) = boardService.create(data)

    fun update(data: BoardUpdateData) = boardService.update(data)

    fun getById(id: UUID) = boardService.getById(id)

    fun deleteById(id: UUID) = boardService.deleteById(id)

    fun getAllByFilter(filter: BoardFilter, pageable: Pageable) = boardService.getAllByFilter(filter, pageable)
}

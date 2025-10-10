package com.example.template.application.presentation

import com.example.template.application.facade.BoardFacade
import com.example.template.domain.board.BoardFilter
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/boards")
class BoardController(
    private val boardFacade: BoardFacade,
) {
    @PostMapping
    fun create(
        @RequestBody request: BoardCreationRequest,
    ): BoardResponse = boardFacade.create(request.toData()).toResponse()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: BoardUpdateRequest,
    ): BoardResponse = boardFacade.update(request.toData(id)).toResponse()

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID,
    ): BoardResponse = boardFacade.getById(id).toResponse()

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: UUID,
    ) {
        boardFacade.deleteById(id)
    }

    @GetMapping
    fun getAllByFilter(
        @RequestParam
        title: String? = null,
        @RequestParam
        content: String? = null,
        @ParameterObject
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
    ): PageResponse<BoardResponse> {
        val filter = BoardFilter(title, content)

        return boardFacade.getAllByFilter(filter, pageable)
            .map { it.toResponse() }
            .toResponse()
    }
}

data class BoardUpdateRequest(
    val title: String,
    val content: String,
)

data class BoardCreationRequest(
    val title: String,
    val content: String,
)

data class BoardResponse(
    val id: UUID,
    val title: String,
    val content: String,
)

package com.example.template.application.presentation

import com.example.template.domain.board.Board

fun Board.toResponse(): BoardResponse =
    BoardResponse(
        id = id,
        title = title,
        content = content,
    )

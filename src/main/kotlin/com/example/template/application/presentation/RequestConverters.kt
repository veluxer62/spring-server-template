package com.example.template.application.presentation

import com.example.template.domain.board.BoardCreationData
import com.example.template.domain.board.BoardUpdateData
import java.util.UUID

fun BoardCreationRequest.toData(): BoardCreationData = BoardCreationData(title, content)

fun BoardUpdateRequest.toData(id: UUID): BoardUpdateData = BoardUpdateData(id, title, content)

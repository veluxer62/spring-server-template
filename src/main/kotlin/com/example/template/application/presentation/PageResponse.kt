package com.example.template.application.presentation

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)

fun <T> Page<T>.toResponse() = PageResponse(content, number, size, totalElements, totalPages)

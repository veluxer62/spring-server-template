package com.example.template.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class OffsetPageRequest(
    private val limit: Int = 10,
    private val offset: Int = 0,
    private val sort: Sort = Sort.unsorted(),
) : Pageable {
    init {
        if (limit < 1) {
            throw kotlin.IllegalArgumentException("limit는 0보다 큰 값이어야 합니다.")
        }

        if (offset < 0) {
            throw kotlin.IllegalArgumentException("offset은 0이상이어야 합니다.")
        }
    }

    override fun getPageNumber(): Int = offset / limit

    override fun getPageSize(): Int = limit

    override fun getOffset(): Long = offset.toLong()

    override fun getSort(): Sort = sort

    override fun next(): Pageable = OffsetPageRequest(limit = limit, offset = offset + limit, sort = sort)

    override fun hasPrevious(): Boolean = offset >= limit

    override fun first(): Pageable = OffsetPageRequest(limit = limit, sort = sort)

    override fun previousOrFirst(): Pageable =
        when {
            hasPrevious() -> previous()
            else -> first()
        }

    private fun previous(): Pageable = OffsetPageRequest(limit = limit, offset = offset - limit, sort = sort)

    override fun withPage(pageNumber: Int): Pageable =
        OffsetPageRequest(limit = limit, offset = pageNumber * limit, sort = sort)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OffsetPageRequest

        if (limit != other.limit) return false
        if (offset != other.offset) return false
        if (sort != other.sort) return false

        return true
    }

    override fun hashCode(): Int {
        var result = limit
        result = 31 * result + offset
        result = 31 * result + sort.hashCode()
        return result
    }
}

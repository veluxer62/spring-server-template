package com.example.template.domain.board

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface BoardRepository :
    JpaRepository<Board, UUID>,
    JpaSpecificationExecutor<Board>

data class BoardFilter(
    val title: String? = null,
    val content: String? = null,
) : Specification<Board> {
    override fun toPredicate(
        root: Root<Board?>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder,
    ): Predicate {
        val predicates = mutableListOf<Predicate>()

        title?.let {
            predicates.add(criteriaBuilder.like(root.get(Board_.title), "%$it%"))
        }

        content?.let {
            predicates.add(criteriaBuilder.like(root.get(Board_.content), "%$it%"))
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}

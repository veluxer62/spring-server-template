package com.example.template.domain.board

import com.example.template.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Board(
    title: String,
    content: String,
) : BaseEntity() {
    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var content: String = content
        protected set

    fun update(
        title: String,
        content: String,
    ) {
        this.title = title
        this.content = content
    }
}

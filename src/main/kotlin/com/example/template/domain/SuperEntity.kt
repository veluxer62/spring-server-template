package com.example.template.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import jakarta.persistence.PreUpdate
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@MappedSuperclass
abstract class PrimaryKeyEntity(
    @Id
    private val id: UUID = generateId(),
) : Persistable<UUID> {
    @Transient
    private var transientIsNew = true

    override fun getId(): UUID = id

    override fun isNew(): Boolean = transientIsNew

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Any =
        if (obj is HibernateProxy) {
            obj.hibernateLazyInitializer.identifier
        } else {
            (obj as PrimaryKeyEntity).id
        }

    override fun hashCode() = Objects.hashCode(id)

    @PostPersist
    @PostLoad
    protected fun load() {
        transientIsNew = false
    }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    id: UUID = generateId(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = createdAt,
) : PrimaryKeyEntity(id) {
    @Column(nullable = false)
    @Suppress("CanBePrimaryConstructorProperty")
    val createdAt: LocalDateTime = createdAt

    @Column(nullable = false)
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @PreUpdate
    fun onPreUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}

fun generateId(): UUID = UUID.randomUUID()

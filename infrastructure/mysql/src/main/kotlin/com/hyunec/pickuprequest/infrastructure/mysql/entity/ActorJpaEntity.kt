package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.common.model.Actor
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "actor")
@Entity
class ActorJpaEntity(
    val domainId: String,
    @Enumerated(EnumType.STRING)
    val type: Actor.Type,
    val name: String
) : BaseEntity() {
    constructor(actor: Actor) : this(
        domainId = actor.id,
        type = actor.type,
        name = actor.name
    )

    fun toDomainEntity(): Actor {
        return Actor(
            id = domainId,
            type = type,
            name = name
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActorJpaEntity

        if (domainId != other.domainId) return false
        if (type != other.type) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = domainId.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

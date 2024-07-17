package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import jakarta.persistence.*

@Table(name = "actor")
@Entity
class ActorJpaEntity(
    @Column(unique = true)
    val domainId: String,
    @Enumerated(EnumType.STRING)
    val type: Actor.Type,
    val name: String
) : BaseEntity() {
    fun toDomainEntity(): Actor {
        return Actor(
            id = domainId,
            type = type,
            name = name
        )
    }
}

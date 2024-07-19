package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*
import java.time.Instant

@Table(name = "pickup_history")
@Entity
class PickupHistoryJpaEntity(
    @ManyToOne
    val actor: ActorJpaEntity,
    @Enumerated(EnumType.STRING)
    val status: Pickup.Status,
    val description: String?,
    val at: Instant
) : BaseEntity() {
    fun toDomainEntity(): Pickup.History {
        return Pickup.History(
            actor = actor.toDomainEntity(),
            status = status,
            desc = description,
            createdAt = at
        )
    }
}

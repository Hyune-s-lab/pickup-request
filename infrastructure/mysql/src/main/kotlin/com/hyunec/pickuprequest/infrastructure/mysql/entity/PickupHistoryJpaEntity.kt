package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*
import java.time.Instant

@Table(name = "pickup_history")
@Entity
class PickupHistoryJpaEntity(
    @ManyToOne(cascade = [CascadeType.ALL])
    val actor: ActorJpaEntity,
    @Enumerated(EnumType.STRING)
    val status: Pickup.Status,
    val description: String,
    val at: Instant
) : BaseEntity() {
    constructor(history: Pickup.History) : this(
        actor = ActorJpaEntity(history.actor),
        status = history.status,
        description = history.desc,
        at = history.at
    )

    fun toDomainEntity(): Pickup.History {
        return Pickup.History(
            actor = actor.toDomainEntity(),
            status = status,
            desc = description,
            at = at
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PickupHistoryJpaEntity

        if (actor != other.actor) return false
        if (status != other.status) return false
        if (description != other.description) return false
        if (at != other.at) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = actor.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + at.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

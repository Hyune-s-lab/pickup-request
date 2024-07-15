package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.infrastructure.mysql.entity.*
import org.springframework.stereotype.Component

@Component
class PickupMapper(
    private val labelJpaRepository: LabelJpaRepository,
    private val storeJpaRepository: StoreJpaRepository,
    private val actorJpaRepository: ActorJpaRepository
) {
    fun toJpaEntity(store: Store): StoreJpaEntity {
        return storeJpaRepository.findByDomainId(store.id) ?: StoreJpaEntity(
            domainId = store.id,
            name = store.name,
            address = store.address
        )
    }

    fun toJpaEntity(actor: Actor): ActorJpaEntity {
        return actorJpaRepository.findByDomainId(actor.id) ?: ActorJpaEntity(
            domainId = actor.id,
            type = actor.type,
            name = actor.name
        )
    }

    fun toJpaEntity(history: Pickup.History): PickupHistoryJpaEntity {
        return PickupHistoryJpaEntity(
            actor = toJpaEntity(history.actor),
            status = history.status,
            description = history.desc,
            at = history.at
        )
    }

    fun toJpaEntity(label: Pickup.Label): LabelJpaEntity {
        return labelJpaRepository.findByDomainId(label.id) ?: LabelJpaEntity(
            domainId = label.id,
            qrcode = label.qrcode,
            volume = label.volume,
            images = label.images.map { LabelImageJpaEmbeddable(it) }
        )
    }

    fun merge(pickupJpaEntity: PickupJpaEntity, pickup: Pickup) {
        if (pickupJpaEntity.status != pickup.status) {
            pickupJpaEntity.status = pickup.status

            pickupJpaEntity.label = pickup.label?.let { toJpaEntity(it) }

            if (pickupJpaEntity.histories.size < pickup.histories.size) {
                pickup.histories
                    .subList(pickupJpaEntity.histories.size, pickup.histories.size)
                    .map { toJpaEntity(it) }
                    .run { pickupJpaEntity.histories.addAll(this) }
            }
        }
    }
}

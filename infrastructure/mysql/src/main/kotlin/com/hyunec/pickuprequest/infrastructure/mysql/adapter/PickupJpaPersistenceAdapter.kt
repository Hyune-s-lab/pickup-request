package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupHistoryJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.StoreJpaEntity

class PickupJpaPersistenceAdapter(
    private val pickupJpaRepository: PickupJpaRepository
) : PickupPersistencePort {
    override fun save(pickup: Pickup): String {
        val pickupJpaEntity = PickupJpaEntity(
            domainId = pickup.pickupId,
            store = StoreJpaEntity(pickup.store),
            histories = pickup.histories.map(::PickupHistoryJpaEntity).toMutableList()
        )
        val savedPickup = pickupJpaRepository.save(pickupJpaEntity)
        return savedPickup.domainId
    }

    override fun update(pickup: Pickup): String {
        TODO("Not yet implemented")
    }

    override fun findByPickupId(pickupId: String): Pickup? {
        val pickupJpaEntity = pickupJpaRepository.findByDomainId(pickupId)
        return pickupJpaEntity?.let {
            Pickup(
                pickupId = it.domainId,
                store = it.store.toDomainEntity(),
                histories = it.histories.map(PickupHistoryJpaEntity::toDomainEntity).toMutableList()
            )
        }
    }
}

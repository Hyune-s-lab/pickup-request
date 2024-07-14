package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PickupJpaPersistenceAdapter(
    private val pickupJpaRepository: PickupJpaRepository
) : PickupPersistencePort {
    @Transactional
    override fun save(pickup: Pickup): String {
        return pickupJpaRepository.save(PickupJpaEntity(pickup)).domainId
    }

    @Transactional
    override fun update(pickup: Pickup): String {
        return pickupJpaRepository.findByDomainId(pickup.pickupId)?.let {
            it.merge(pickup)
            it.domainId
        } ?: throw EntityNotFoundException("pickupId=${pickup.pickupId}")
    }

    @Transactional(readOnly = true)
    override fun findByPickupId(pickupId: String): Pickup? {
        return pickupJpaRepository.findByDomainId(pickupId)?.toDomainEntity()
    }
}

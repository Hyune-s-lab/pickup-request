package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
class PickupJpaPersistenceAdapter(
    private val pickupJpaRepository: PickupJpaRepository,
    private val pickupHistoryQuerydslRepository: PickupQuerydslRepository,

    private val pickupMapper: PickupMapper
) : PickupPersistencePort {
    @Transactional
    override fun save(pickup: Pickup): String {
        val pickupJpaEntity = PickupJpaEntity(
            domainId = pickup.id,
            status = pickup.status,
            store = pickupMapper.toJpaEntity(pickup.store),
            histories = pickup.histories.map { pickupMapper.toJpaEntity(it) }.toMutableList()
        )
        return pickupJpaRepository.save(pickupJpaEntity).domainId
    }

    @Transactional
    override fun update(pickup: Pickup): String {
        return pickupJpaRepository.findByDomainId(pickup.id)?.let {
            pickupMapper.merge(it, pickup)
            it.domainId
        } ?: throw EntityNotFoundException("pickupId=${pickup.id}")
    }

    @Transactional(readOnly = true)
    override fun findByDomainId(domainId: String): Pickup? {
        return pickupJpaRepository.findByDomainId(domainId)?.toDomainEntity()
    }

    @Transactional(readOnly = true)
    override fun findAllBy(
        storeId: String?,
        requestActorId: String?,
        startAt: Instant?,
        endAt: Instant?
    ): List<Pickup> {
        return pickupHistoryQuerydslRepository.findAllBy(storeId, requestActorId, startAt, endAt).map { it.toDomainEntity() }
    }
}

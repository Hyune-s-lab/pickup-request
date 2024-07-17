package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Store
import com.hyunec.pickuprequest.domain.pickup.port.StorePersistencePort
import com.hyunec.pickuprequest.infrastructure.mysql.entity.StoreJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class StoreJpaPersistenceAdapter(
    private val storeJpaRepository: StoreJpaRepository
) : StorePersistencePort {
    @Transactional
    override fun save(store: Store): String {
        val storeJpaEntity = StoreJpaEntity(
            domainId = store.id,
            name = store.name,
            address = store.address
        )
        return storeJpaRepository.save(storeJpaEntity).domainId
    }

    override fun findAll(): List<Store> {
        return storeJpaRepository.findAll().map { it.toDomainEntity() }
    }

    @Transactional(readOnly = true)
    override fun findByDomainId(domainId: String): Store? {
        return storeJpaRepository.findByDomainId(domainId)?.toDomainEntity()
    }
}

package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PickupJpaRepository : JpaRepository<PickupJpaEntity, Long> {
    fun findByDomainId(domainId: String): PickupJpaEntity?
}

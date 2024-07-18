package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.infrastructure.mysql.entity.StoreJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StoreJpaRepository : JpaRepository<StoreJpaEntity, Long> {
    fun findByDomainId(domainId: String): StoreJpaEntity?
}

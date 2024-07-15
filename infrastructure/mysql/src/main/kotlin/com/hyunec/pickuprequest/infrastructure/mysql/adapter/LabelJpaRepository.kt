package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.infrastructure.mysql.entity.LabelJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LabelJpaRepository : JpaRepository<LabelJpaEntity, Long> {
    fun findByDomainId(domainId: String): LabelJpaEntity?
}

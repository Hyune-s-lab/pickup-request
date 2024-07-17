package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.infrastructure.mysql.entity.ActorJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActorJpaRepository : JpaRepository<ActorJpaEntity, Long> {
    fun findByDomainId(domainId: String): ActorJpaEntity?
    fun findByType(type: Actor.Type): List<ActorJpaEntity>
}

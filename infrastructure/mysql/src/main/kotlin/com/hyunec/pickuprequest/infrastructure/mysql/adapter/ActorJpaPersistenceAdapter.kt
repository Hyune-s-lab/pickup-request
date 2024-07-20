package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.port.ActorPersistencePort
import com.hyunec.pickuprequest.infrastructure.mysql.entity.ActorJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ActorJpaPersistenceAdapter(
    private val actorJpaRepository: ActorJpaRepository
) : ActorPersistencePort {
    @Transactional
    override fun save(actor: Actor): String {
        val actorJpaEntity = ActorJpaEntity(
            domainId = actor.id,
            type = actor.type,
            name = actor.name

        )
        return actorJpaRepository.save(actorJpaEntity).domainId
    }

    override fun findAll(): List<Actor> {
        return actorJpaRepository.findAll().map { it.toDomainEntity() }
    }

    @Transactional(readOnly = true)
    override fun findByDomainId(domainId: String): Actor? {
        return actorJpaRepository.findByDomainId(domainId)?.toDomainEntity()
    }

    override fun findByType(type: Actor.Type): List<Actor> {
        return actorJpaRepository.findByType(type).map { it.toDomainEntity() }
    }
}

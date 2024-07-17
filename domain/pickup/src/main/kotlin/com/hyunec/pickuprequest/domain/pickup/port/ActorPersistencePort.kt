package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Actor

interface ActorPersistencePort {

    fun save(actor: Actor): String

    fun findAll(): List<Actor>
    fun findByDomainId(domainId: String): Actor?
    fun findByType(type: Actor.Type): List<Actor>
}

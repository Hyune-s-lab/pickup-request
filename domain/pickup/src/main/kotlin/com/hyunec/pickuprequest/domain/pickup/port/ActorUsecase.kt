package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Actor

interface ActorUsecase {

    fun create(type: Actor.Type, name: String): String

    fun findAll(): List<Actor>
    fun findByActorId(actorId: String): Actor
}

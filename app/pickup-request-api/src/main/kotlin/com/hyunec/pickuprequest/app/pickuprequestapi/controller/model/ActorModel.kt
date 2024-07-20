package com.hyunec.pickuprequest.app.pickuprequestapi.controller.model

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import java.time.Instant

data class ActorModel(
    val id: String,
    val type: Actor.Type,
    val name: String,
    val createdAt: Instant
) {
    constructor(actor: Actor) : this(
        actor.id,
        actor.type,
        actor.name,
        actor.createdAt
    )
}

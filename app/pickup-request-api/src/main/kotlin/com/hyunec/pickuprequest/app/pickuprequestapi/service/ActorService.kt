package com.hyunec.pickuprequest.app.pickuprequestapi.service

import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.ActorPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.ActorUsecase
import org.springframework.stereotype.Service

@Service
class ActorService(
    private val actorPersistencePort: ActorPersistencePort
) : ActorUsecase {
    override fun create(type: Actor.Type, name: String): String {
        return actorPersistencePort.save(Actor(ULIDGenerator.take(), type, name))
    }

    override fun findAll(): List<Actor> {
        return actorPersistencePort.findAll()
    }

    override fun findByActorId(actorId: String): Actor {
        return actorPersistencePort.findByDomainId(actorId)
            ?: throw EntityNotFoundException("actorId=$actorId")
    }
}

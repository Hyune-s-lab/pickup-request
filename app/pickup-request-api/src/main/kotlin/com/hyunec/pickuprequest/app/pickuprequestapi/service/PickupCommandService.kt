package com.hyunec.pickuprequest.app.pickuprequestapi.service

import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.ActorPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.PickupCommandUsecase
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PickupCommandService(
    private val pickupPersistencePort: PickupPersistencePort,

    private val actorPersistencePort: ActorPersistencePort
) : PickupCommandUsecase {
    override fun request(command: PickupCommand.Request): String {
        return pickupPersistencePort.save(Pickup(ULIDGenerator.take(), command))
    }

    override fun accept(command: PickupCommand.Accept): String {
        return pickupPersistencePort.findByDomainId(command.pickupId)?.let {
            it.accept(command)
            pickupPersistencePort.update(it)
        } ?: throw EntityNotFoundException("pickupId=${command.pickupId}")
    }

    override fun process(command: PickupCommand.Process): String {
        return pickupPersistencePort.findByDomainId(command.pickupId)?.let {
            it.process(command)
            pickupPersistencePort.update(it)
        } ?: throw EntityNotFoundException("pickupId=${command.pickupId}")
    }

    /**
     * approve -> complete 는 자동으로 수행 됩니다.
     */
    override fun approve(command: PickupCommand.Approve): String {
        val systemAutoActor = actorPersistencePort.findByType(Actor.Type.SYSTEM_AUTO).first()

        return pickupPersistencePort.findByDomainId(command.pickupId)?.let {
            it.approve(command)
            pickupPersistencePort.update(it)

            val completeCommand = PickupCommand.Complete(
                actor = systemAutoActor,
                pickupId = command.pickupId
            )
            it.complete(completeCommand)
            pickupPersistencePort.update(it)
        } ?: throw EntityNotFoundException("pickupId=${command.pickupId}")
    }

    override fun complete(command: PickupCommand.Complete): String {
        return pickupPersistencePort.findByDomainId(command.pickupId)?.let {
            it.complete(command)
            pickupPersistencePort.update(it)
        } ?: throw EntityNotFoundException("pickupId=${command.pickupId}")
    }

    override fun cancel(command: PickupCommand.Cancel): String {
        TODO("Not yet implemented")
    }
}

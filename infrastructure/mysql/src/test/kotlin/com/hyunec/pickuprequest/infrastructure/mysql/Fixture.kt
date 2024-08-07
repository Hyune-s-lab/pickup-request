package com.hyunec.pickuprequest.infrastructure.mysql

import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.entity.Store
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import net.datafaker.Faker

object Fixture {
    @PublishedApi
    internal val faker = Faker()

    fun pickupId(): String {
        return ULIDGenerator.take()
    }

    fun actor(actorType: Actor.Type): Actor {
        return Actor(
            id = ULIDGenerator.take(),
            type = actorType,
            name = faker.name().fullName()
        )
    }

    fun store(): Store {
        return Store(
            id = ULIDGenerator.take(),
            name = faker.name().fullName(),
            address = faker.address().fullAddress()
        )
    }

    fun label(): Pickup.Label {
        return Pickup.Label(
            id = ULIDGenerator.take(),
            qrcode = faker.barcode().ean13().toString(),
            volume = 16000,
            images = listOf(
                Pickup.Label.Image(faker.internet().image().toString())
            )
        )
    }

    inline fun <reified T : PickupCommand> pickupCommand(
        pickupId: String? = null,
        store: Store? = null,

        label: Pickup.Label? = null,

        actor: Actor,
        desc: String = ""
    ): T {
        return when (T::class) {
            PickupCommand.Request::class -> PickupCommand.Request(actor, desc, store!!) as T
            PickupCommand.Accept::class -> PickupCommand.Accept(actor, desc, pickupId!!) as T
            PickupCommand.Process::class -> PickupCommand.Process(actor, desc, pickupId!!, label!!) as T
            PickupCommand.Approve::class -> PickupCommand.Approve(actor, desc, pickupId!!) as T
            PickupCommand.Complete::class -> PickupCommand.Complete(actor, desc, pickupId!!) as T
            PickupCommand.Cancel::class -> PickupCommand.Cancel(actor, desc, pickupId!!) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    data class Sample(
        val actors: Map<Actor.Type, Actor> = mapOf(
            Actor.Type.PICKUP_DRIVER to actor(Actor.Type.PICKUP_DRIVER),
            Actor.Type.PARTNER_STORE_OWNER to actor(Actor.Type.PARTNER_STORE_OWNER),
            Actor.Type.SYSTEM_AUTO to actor(Actor.Type.SYSTEM_AUTO)
        ),
        val store: Store = store(),
        val label: Pickup.Label = label()
    )
}

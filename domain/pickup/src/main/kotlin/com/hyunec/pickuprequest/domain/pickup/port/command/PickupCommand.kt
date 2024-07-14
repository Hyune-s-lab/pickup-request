package com.hyunec.pickuprequest.domain.pickup.port.command

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup

sealed class PickupCommand {
    abstract val actor: Actor
    abstract val desc: String
    abstract val pickupId: String

    data class Request(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String,

        val store: Store,
    ) : PickupCommand()

    data class Accept(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String
    ) : PickupCommand()

    data class Process(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String,
        val label: Pickup.Label
    ) : PickupCommand()

    data class Approve(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String,
    ) : PickupCommand()

    data class Complete(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String,
    ) : PickupCommand()

    data class Cancel(
        override val actor: Actor,
        override val desc: String,
        override val pickupId: String,
    ) : PickupCommand()
}

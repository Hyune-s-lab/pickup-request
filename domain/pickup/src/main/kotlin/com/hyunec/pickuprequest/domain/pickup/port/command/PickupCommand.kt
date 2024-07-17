package com.hyunec.pickuprequest.domain.pickup.port.command

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.entity.Store

sealed class PickupCommand {
    abstract val actor: Actor
    abstract val desc: String?

    data class Request(
        override val actor: Actor,
        override val desc: String? = null,

        val store: Store,
    ) : PickupCommand()

    data class Accept(
        override val actor: Actor,
        override val desc: String? = null,

        val pickupId: String
    ) : PickupCommand()

    data class Process(
        override val actor: Actor,
        override val desc: String? = null,

        val pickupId: String,
        val label: Pickup.Label
    ) : PickupCommand()

    data class Approve(
        override val actor: Actor,
        override val desc: String? = null,

        val pickupId: String,
    ) : PickupCommand()

    data class Complete(
        override val actor: Actor,
        override val desc: String? = null,

        val pickupId: String,
    ) : PickupCommand()

    data class Cancel(
        override val actor: Actor,
        override val desc: String? = null,

        val pickupId: String,
    ) : PickupCommand()
}

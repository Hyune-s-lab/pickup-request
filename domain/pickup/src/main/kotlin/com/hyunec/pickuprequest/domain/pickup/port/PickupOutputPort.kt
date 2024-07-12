package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.PickupLabel
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.Pickup
import java.time.Instant

interface PickupOutputPort {
    fun request(requestBy: Actor, requestedAt: Instant, store: Store, desc: String): Pickup

    fun accept(pickupId: String, acceptBy: Actor, acceptedAt: Instant, desc: String): Pickup

    fun process(
        pickupId: String,
        processBy: Actor,
        processedAt: Instant,
        label: PickupLabel,
        desc: String
    ): Pickup

    fun approve(pickupId: String, approveBy: Actor, approvedAt: Instant, desc: String): Pickup

    fun cancel(pickupId: String, cancelBy: Actor, canceledAt: Instant, desc: String): Pickup

    fun findByPickupId(pickupId: String): Pickup
}

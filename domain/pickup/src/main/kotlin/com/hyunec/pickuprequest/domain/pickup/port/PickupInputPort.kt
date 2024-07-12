package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.PickupLabel
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.Pickup

interface PickupInputPort {

    fun request(requestBy: Actor, store: Store, desc: String): Pickup

    fun accept(pickupId: String, acceptBy: Actor, desc: String): Pickup

    fun process(pickupId: String, processBy: Actor, label: PickupLabel, desc: String): Pickup

    fun approve(pickupId: String, approveBy: Actor, desc: String): Pickup

    fun cancel(pickupId: String, cancelBy: Actor, desc: String): Pickup

    fun findByPickupId(pickupId: String): Pickup
}

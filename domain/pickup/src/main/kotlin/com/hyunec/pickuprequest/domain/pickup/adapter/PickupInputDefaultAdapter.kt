package com.hyunec.pickuprequest.domain.pickup.adapter

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.PickupLabel
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.PickupInputPort
import com.hyunec.pickuprequest.domain.pickup.port.PickupOutputPort
import java.time.Instant

class PickupInputDefaultAdapter(
    private val outputPort: PickupOutputPort
) : PickupInputPort {
    override fun request(requestBy: Actor, store: Store, desc: String): Pickup {
        return outputPort.request(requestBy, Instant.now(), store, desc)
    }

    override fun accept(pickupId: String, acceptBy: Actor, desc: String): Pickup {
        return outputPort.accept(pickupId, acceptBy, Instant.now(), desc)
    }

    override fun process(pickupId: String, processBy: Actor, label: PickupLabel, desc: String): Pickup {
        return outputPort.process(pickupId, processBy, Instant.now(), label, desc)
    }

    override fun approve(pickupId: String, approveBy: Actor, desc: String): Pickup {
        return outputPort.approve(pickupId, approveBy, Instant.now(), desc)
    }

    override fun cancel(pickupId: String, cancelBy: Actor, desc: String): Pickup {
        return outputPort.cancel(pickupId, cancelBy, Instant.now(), desc)
    }

    override fun findByPickupId(pickupId: String): Pickup {
        return outputPort.findByPickupId(pickupId)
    }
}

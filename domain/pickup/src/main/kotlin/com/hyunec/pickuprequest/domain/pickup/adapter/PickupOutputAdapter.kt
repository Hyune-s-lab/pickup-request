package com.hyunec.pickuprequest.domain.pickup.adapter

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.PickupLabel
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.PickupOutputPort
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class PickupOutputAdapter(
    private val pickups: MutableMap<String, Pickup> = ConcurrentHashMap()
) : PickupOutputPort {
    override fun request(requestBy: Actor, requestedAt: Instant, store: Store, desc: String): Pickup {
        return
    }

    override fun accept(pickupId: String, acceptBy: Actor, acceptedAt: Instant, desc: String): Pickup {
        TODO("Not yet implemented")
    }

    override fun process(
        pickupId: String,
        processBy: Actor,
        processedAt: Instant,
        label: PickupLabel,
        desc: String
    ): Pickup {
        TODO("Not yet implemented")
    }

    override fun approve(pickupId: String, approveBy: Actor, approvedAt: Instant, desc: String): Pickup {
        TODO("Not yet implemented")
    }

    override fun cancel(pickupId: String, cancelBy: Actor, canceledAt: Instant, desc: String): Pickup {
        TODO("Not yet implemented")
    }

    override fun findByPickupId(pickupId: String): Pickup {
        TODO("Not yet implemented")
    }
}

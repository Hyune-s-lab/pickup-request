package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.Pickup

interface PickupPersistencePort {
    fun save(pickup: Pickup): String

    fun update(pickup: Pickup): String

    fun findByPickupId(pickupId: String): Pickup?
}

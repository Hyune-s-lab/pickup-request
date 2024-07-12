package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.Pickup

interface PickupQueryUsecase {

    fun findByPickupId(pickupId: String): Pickup?
}

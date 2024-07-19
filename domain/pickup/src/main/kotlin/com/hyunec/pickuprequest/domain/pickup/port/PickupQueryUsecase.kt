package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup

interface PickupQueryUsecase {

    fun findByDomainId(domainId: String): Pickup
}

package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup

interface PickupPersistencePort {
    fun save(pickup: Pickup): String

    fun update(pickup: Pickup): String

    fun findByDomainId(domainId: String): Pickup?
}

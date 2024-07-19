package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import java.time.Instant

interface PickupPersistencePort {
    fun save(pickup: Pickup): String

    fun update(pickup: Pickup): String

    fun findByDomainId(domainId: String): Pickup?

    fun findAllBy(storeId: String?, startAt: Instant?, endAt: Instant?): List<Pickup>
}

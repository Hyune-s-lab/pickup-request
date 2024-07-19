package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import java.time.Instant

interface PickupQueryUsecase {

    fun findByDomainId(domainId: String): Pickup
    fun findAllBy(storeId: String?, startAt: Instant?, endAt: Instant?): List<Pickup>
}

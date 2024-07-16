package com.hyunec.pickuprequest.app.pickuprequestapi.service

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.PickupQueryUsecase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PickupQueryService(
    private val pickupPersistenceAdapter: PickupPersistencePort
) : PickupQueryUsecase {
    override fun findByDomainId(domainId: String): Pickup? {
        return pickupPersistenceAdapter.findByDomainId(domainId)
    }
}

package com.hyunec.pickuprequest.app.pickuprequestapi.service

import com.hyunec.pickuprequest.common.util.KLogging
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.PickupQueryUsecase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Transactional(readOnly = true)
@Service
class PickupQueryService(
    private val pickupPersistenceAdapter: PickupPersistencePort
) : PickupQueryUsecase {
    override fun findByDomainId(domainId: String): Pickup {
        return pickupPersistenceAdapter.findByDomainId(domainId)
            ?: throw EntityNotFoundException("pickupId=$domainId")
    }

    override fun findAllBy(storeId: String?, startAt: Instant?, endAt: Instant?): List<Pickup> {
        log.info("findAllBy(storeId: $storeId, startedAt: $startAt, endedAt: $endAt)")
        return pickupPersistenceAdapter.findAllBy(storeId, startAt, endAt)
    }

    companion object : KLogging()
}

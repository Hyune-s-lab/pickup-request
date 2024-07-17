package com.hyunec.pickuprequest.app.pickuprequestapi.service

import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Store
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.StorePersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.StoreUsecase
import org.springframework.stereotype.Service

@Service
class StoreService(
    private val storePersistencePort: StorePersistencePort
) : StoreUsecase {
    override fun create(name: String, address: String): String {
        return storePersistencePort.save(Store(ULIDGenerator.take(), name, address))
    }

    override fun findByStoreId(storeId: String): Store {
        return storePersistencePort.findByDomainId(storeId)
            ?: throw EntityNotFoundException("storeId=$storeId")
    }
}

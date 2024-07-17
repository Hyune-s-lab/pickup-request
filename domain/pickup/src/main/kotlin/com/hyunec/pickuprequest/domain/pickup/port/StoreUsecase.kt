package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Store

interface StoreUsecase {

    fun create(name: String, address: String): String

    fun findByStoreId(storeId: String): Store
}

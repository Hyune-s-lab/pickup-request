package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.entity.Store

interface StorePersistencePort {

    fun save(store: Store): String

    fun findAll(): List<Store>
    fun findByDomainId(domainId: String): Store?
}

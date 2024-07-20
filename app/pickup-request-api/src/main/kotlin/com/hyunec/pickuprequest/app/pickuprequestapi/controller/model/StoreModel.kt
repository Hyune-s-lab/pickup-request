package com.hyunec.pickuprequest.app.pickuprequestapi.controller.model

import com.hyunec.pickuprequest.domain.pickup.entity.Store
import java.time.Instant

data class StoreModel(
    val id: String,
    val name: String,
    val address: String,
    val createdAt: Instant
) {
    constructor(store: Store) : this(
        store.id,
        store.name,
        store.address,
        store.createdAt
    )
}

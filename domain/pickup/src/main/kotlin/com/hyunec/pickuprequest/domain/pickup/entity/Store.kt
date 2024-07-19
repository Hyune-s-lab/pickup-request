package com.hyunec.pickuprequest.domain.pickup.entity

import java.time.Instant

data class Store(
    val id: String,
    val name: String,
    val address: String,
    val createdAt: Instant = Instant.now()
)

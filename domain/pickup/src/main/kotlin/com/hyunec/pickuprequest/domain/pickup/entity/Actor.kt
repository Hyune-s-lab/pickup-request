package com.hyunec.pickuprequest.domain.pickup.entity

import java.time.Instant

data class Actor(
    val id: String,
    val type: Type,
    val name: String,
    val createdAt: Instant = Instant.now()
) {
    enum class Type(
        val desc: String
    ) {
        PARTNER_ADMIN("가맹사 어드민"),
        PARTNER_STORE_OWNER("가맹사 점주"),
        PICKUP_DRIVER("수거 기사"),
        MASTER_ADMIN("본사 어드민"),

        SYSTEM_AUTO("자동 시스템"),
        SYSTEM_BATCH("배치 시스템"),
        ;
    }
}

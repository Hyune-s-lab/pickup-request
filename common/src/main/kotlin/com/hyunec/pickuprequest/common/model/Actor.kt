package com.hyunec.pickuprequest.common.model

data class Actor(
    val actorId: String,
    val type: Type,
    val name: String
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

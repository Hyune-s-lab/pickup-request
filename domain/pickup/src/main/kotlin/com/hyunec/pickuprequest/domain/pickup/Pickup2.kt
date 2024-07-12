package com.hyunec.pickuprequest.domain.pickup

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.PickupLabel
import com.hyunec.pickuprequest.common.model.Store
import java.time.Instant

data class Pickup2(
    val id: String,
    val status: Status,
    val store: Store,
    val desc: String,

    val requestActor: Actor,
    val requestedAt: Instant,

    val acceptedActor: Actor,
    val acceptedAt: Instant,

    val processedActor: Actor,
    val processedAt: Instant,
    val label: PickupLabel,

    val approvedActor: Actor,
    val approvedAt: Instant
) {
    enum class Status(
        val desc: String
    ) {
        REQUESTED("요청됨"),
        ACCEPTED("수락됨"),
        PROCESSED("처리됨"),
        COMPLETED("완료됨"),
        APPROVED("승인됨"),
        CANCELED("취소됨")
    }
}

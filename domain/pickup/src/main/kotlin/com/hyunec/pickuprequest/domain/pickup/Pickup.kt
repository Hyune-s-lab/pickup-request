package com.hyunec.pickuprequest.domain.pickup

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import java.net.URL
import java.time.Instant

data class Pickup(
    val pickupId: String,
    val status: Status,
    val store: Store,

    val label: Label? = null,

    val history: List<History> = emptyList()
) {
    constructor(command: PickupCommand.Request) : this(
        pickupId = command.pickupId,
        status = Status.REQUESTED,
        store = command.store,

        history = mutableListOf(
            History(
                actor = command.actor,
                status = Status.REQUESTED,
                desc = command.desc,
                at = Instant.now()
            )
        )
    )

    enum class Status(val desc: String) {
        REQUESTED("요청됨") {
            override fun convertibleStatuses() = listOf(ACCEPTED, CANCELED)
        },
        ACCEPTED("수락됨") {
            override fun convertibleStatuses() = listOf(PROCESSED, CANCELED)
        },
        PROCESSED("처리됨") {
            override fun convertibleStatuses() = listOf(APPROVED, CANCELED)
        },
        APPROVED("승인됨") {
            override fun convertibleStatuses() = listOf(COMPLETED, CANCELED)
        },
        COMPLETED("완료됨") {
            override fun convertibleStatuses() = emptyList<Status>()
        },
        CANCELED("취소됨") {
            override fun convertibleStatuses() = emptyList<Status>()
        };

        abstract fun convertibleStatuses(): List<Status>

        fun convertibleTo(newStatus: Status): Boolean {
            return convertibleStatuses().contains(newStatus)
        }
    }

    data class History(
        val actor: Actor,
        val status: Status,
        val desc: String,
        val at: Instant
    )

    data class Label(
        val id: String,
        val qrcode: String,
        val volume: Int,
        val images: List<URL>,
    )
}

package com.hyunec.pickuprequest.domain.pickup.entity

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.common.util.PreconditionsSupporter.require
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import java.time.Instant

data class Pickup(
    val id: String,
    val store: Store,

    var label: Label? = null,

    val histories: MutableList<History> = mutableListOf()
) {
    val status: Status
        get() = histories.last().status

    constructor(pickupId: String, command: PickupCommand.Request) : this(
        id = pickupId,
        store = command.store,

        histories = mutableListOf(
            History(
                actor = command.actor,
                status = Status.REQUESTED,
                desc = command.desc,
                at = Instant.now()
            )
        )
    )

    fun accept(command: PickupCommand.Accept): Pickup {
        require<IllegalStateException>(this.status.convertibleTo(Status.ACCEPTED)) { "status: $status is not convertible to ${Status.ACCEPTED}" }

        this.histories.add(
            History(
                actor = command.actor,
                status = Status.ACCEPTED,
                desc = command.desc,
                at = Instant.now()
            )
        )

        return this
    }

    fun process(command: PickupCommand.Process): Pickup {
        require<IllegalStateException>(this.status.convertibleTo(Status.PROCESSED)) { "status: $status is not convertible to ${Status.PROCESSED}" }

        this.label = command.label
        this.histories.add(
            History(
                actor = command.actor,
                status = Status.PROCESSED,
                desc = command.desc,
                at = Instant.now()
            )
        )

        return this
    }

    fun approve(command: PickupCommand.Approve): Pickup {
        require<IllegalStateException>(this.status.convertibleTo(Status.APPROVED)) { "status: $status is not convertible to ${Status.APPROVED}" }

        this.histories.add(
            History(
                actor = command.actor,
                status = Status.APPROVED,
                desc = command.desc,
                at = Instant.now()
            )
        )

        return this
    }

    fun complete(command: PickupCommand.Complete): Pickup {
        require<IllegalStateException>(this.status.convertibleTo(Status.COMPLETED)) { "status: $status is not convertible to ${Status.COMPLETED}" }

        this.histories.add(
            History(
                actor = command.actor,
                status = Status.COMPLETED,
                desc = command.desc,
                at = Instant.now()
            )
        )

        return this
    }

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
        val desc: String?,
        val at: Instant
    )

    data class Label(
        val id: String,
        val qrcode: String,
        val volume: Int,
        val images: List<Image>,
    ) {
        data class Image(
            val url: String
        )
    }
}

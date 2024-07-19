package com.hyunec.pickuprequest.app.pickuprequestapi.controller.model

import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup.Status
import java.time.Instant

data class PickupModel(
    val id: String,
    val store: StoreModel,

    var label: LabelModel? = null,

    val histories: List<HistoryModel>
) {
    constructor(pickup: Pickup) : this(
        id = pickup.id,
        store = StoreModel(pickup.store),
        label = pickup.label?.let { LabelModel(it) },
        histories = pickup.histories.map { HistoryModel(it) }
    )

    data class HistoryModel(
        val actor: Actor,
        val status: Status,
        val desc: String?,
        val createdAt: Instant
    ) {
        constructor(history: Pickup.History) : this(
            actor = history.actor,
            status = history.status,
            desc = history.desc,
            createdAt = history.createdAt
        )
    }
}

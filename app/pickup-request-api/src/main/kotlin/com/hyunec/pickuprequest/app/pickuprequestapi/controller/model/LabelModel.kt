package com.hyunec.pickuprequest.app.pickuprequestapi.controller.model

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup

data class LabelModel(
    val id: String,
    val qrcode: String
) {
    constructor(label: Pickup.Label) : this(
        id = label.id,
        qrcode = label.qrcode
    )
}

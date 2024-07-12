package com.hyunec.pickuprequest.common.model

import java.net.URL

data class PickupLabel(
    val id: String,
    val qrcode: String,
    val volume: Int,
    val images: List<URL>,
)

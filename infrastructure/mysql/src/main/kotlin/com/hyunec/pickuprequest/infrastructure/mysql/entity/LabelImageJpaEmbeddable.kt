package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.Embeddable

@Embeddable
class LabelImageJpaEmbeddable(
    val url: String,
) {
    constructor(labelImage: Pickup.Label.Image) : this(
        url = labelImage.url
    )

    fun toDomainEntity() = Pickup.Label.Image(
        url = url
    )
}

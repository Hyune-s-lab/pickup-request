package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "label_image")
@Entity
class LabelImageJpaEntity(
    val url: String,
) : BaseEntity() {
    constructor(labelImage: Pickup.Label.Image) : this(
        url = labelImage.url
    )

    fun toDomainEntity() = Pickup.Label.Image(
        url = url
    )
}

package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*

@Table(name = "label")
@Entity
class LabelJpaEntity(
    val domainId: String,
    val qrcode: String,
    val volume: Int,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "label_id")
    val images: List<LabelImageJpaEntity>,
) : BaseEntity() {
    constructor(label: Pickup.Label) : this(
        label.labelId,
        label.qrcode,
        label.volume,
        label.imageUrls.map(::LabelImageJpaEntity)
    )

    fun toDomainEntity() = Pickup.Label(
        labelId = domainId,
        qrcode = qrcode,
        volume = volume,
        imageUrls = images.map { it.url }
    )
}

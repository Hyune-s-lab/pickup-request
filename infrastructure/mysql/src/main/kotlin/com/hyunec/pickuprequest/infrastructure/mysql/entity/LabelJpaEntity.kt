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
        label.id,
        label.qrcode,
        label.volume,
        label.images.map(::LabelImageJpaEntity)
    )

    fun toDomainEntity() = Pickup.Label(
        id = domainId,
        qrcode = qrcode,
        volume = volume,
        images = images.map(LabelImageJpaEntity::toDomainEntity)
    )
}

package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*
import java.time.Instant

@Table(name = "label")
@Entity
class LabelJpaEntity(
    @Column(unique = true)
    val domainId: String,
    val qrcode: String,
    val volume: Int,
    val createdAt: Instant = Instant.now(),

    @ElementCollection
    @CollectionTable(name = "label_image", joinColumns = [JoinColumn(name = "label_id")])
    val images: List<LabelImageJpaEmbeddable>,
) : BaseEntity() {
    fun toDomainEntity() = Pickup.Label(
        id = domainId,
        qrcode = qrcode,
        volume = volume,
        createdAt = createdAt,
        images = images.map(LabelImageJpaEmbeddable::toDomainEntity)
    )
}

package com.hyunec.pickuprequest.infrastructure.mysql.entity

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
) : BaseEntity()

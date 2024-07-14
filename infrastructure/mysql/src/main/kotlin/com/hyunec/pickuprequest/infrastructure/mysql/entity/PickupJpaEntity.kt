package com.hyunec.pickuprequest.infrastructure.mysql.entity

import jakarta.persistence.*

@Table(name = "pickup")
@Entity
class PickupJpaEntity(
    val domainId: String,

    @ManyToOne(cascade = [CascadeType.ALL])
    val store: StoreJpaEntity,

    @OneToOne(cascade = [CascadeType.ALL])
    var label: LabelJpaEntity? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "pickup_id")
    val histories: MutableList<PickupHistoryJpaEntity> = mutableListOf()
) : BaseEntity()

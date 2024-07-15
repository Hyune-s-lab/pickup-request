package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*

@Table(name = "pickup")
@Entity
class PickupJpaEntity(
    val domainId: String,

    @Enumerated(EnumType.STRING)
    var status: Pickup.Status,

    @ManyToOne(cascade = [CascadeType.ALL])
    val store: StoreJpaEntity,

    @OneToOne(cascade = [CascadeType.ALL])
    var label: LabelJpaEntity? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pickup_id")
    val histories: MutableList<PickupHistoryJpaEntity> = mutableListOf()
) : BaseEntity() {
    fun toDomainEntity() = Pickup(
        id = domainId,
        store = store.toDomainEntity(),
        label = label?.toDomainEntity(),
        histories = histories.map(PickupHistoryJpaEntity::toDomainEntity).toMutableList()
    )
}

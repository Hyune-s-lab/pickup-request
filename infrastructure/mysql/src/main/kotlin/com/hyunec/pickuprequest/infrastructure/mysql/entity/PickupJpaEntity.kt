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
    constructor(pickup: Pickup) : this(
        domainId = pickup.id,
        status = pickup.status,
        store = StoreJpaEntity(pickup.store),
        histories = pickup.histories.map(::PickupHistoryJpaEntity).toMutableList()
    )

    fun toDomainEntity() = Pickup(
        id = domainId,
        store = store.toDomainEntity(),
        label = label?.toDomainEntity(),
        histories = histories.map(PickupHistoryJpaEntity::toDomainEntity).toMutableList()
    )

    fun merge(pickup: Pickup) {
        if (this.status != pickup.status) {
            status = pickup.status
            label = pickup.label?.let { LabelJpaEntity(it) }

            if (histories.size < pickup.histories.size) {
                pickup.histories
                    .subList(histories.size, pickup.histories.size)
                    .map { PickupHistoryJpaEntity(it) }
                    .run { histories.addAll(this) }
            }
        }
    }
}

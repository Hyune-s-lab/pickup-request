package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import jakarta.persistence.*

@Table(name = "pickup")
@Entity
class PickupJpaEntity(
    val domainId: String,

    @Enumerated(EnumType.STRING)
    var status: Pickup.Status,

    @ManyToOne
    val store: StoreJpaEntity,

    @OneToOne(cascade = [CascadeType.PERSIST])
    var label: LabelJpaEntity? = null,

    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true, fetch = FetchType.EAGER)
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

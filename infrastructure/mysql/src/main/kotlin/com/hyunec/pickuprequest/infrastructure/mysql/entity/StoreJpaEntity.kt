package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.common.model.Store
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "store")
@Entity
class StoreJpaEntity(
    val domainId: String,
    val name: String,
    val address: String
) : BaseEntity() {
    constructor(store: Store) : this(
        domainId = store.id,
        name = store.name,
        address = store.address
    )

    fun toDomainEntity(): Store {
        return Store(
            id = domainId,
            name = name,
            address = address
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreJpaEntity

        if (domainId != other.domainId) return false
        if (name != other.name) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = domainId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }
}

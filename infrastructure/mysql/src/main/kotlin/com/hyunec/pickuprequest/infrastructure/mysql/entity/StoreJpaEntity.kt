package com.hyunec.pickuprequest.infrastructure.mysql.entity

import com.hyunec.pickuprequest.domain.pickup.entity.Store
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant

@Table(name = "store")
@Entity
class StoreJpaEntity(
    @Column(unique = true)
    val domainId: String,
    val name: String,
    val address: String,
    val createdAt: Instant = Instant.now()
) : BaseEntity() {
    fun toDomainEntity(): Store {
        return Store(
            id = domainId,
            name = name,
            address = address,
            createdAt = createdAt
        )
    }
}

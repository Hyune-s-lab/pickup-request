package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.QPickupHistoryJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.QPickupJpaEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class PickupQuerydslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findAllBy(storeId: String?, startAt: Instant?, endAt: Instant?): List<PickupJpaEntity> {
        val qPickup = QPickupJpaEntity.pickupJpaEntity
        val qHistory = QPickupHistoryJpaEntity.pickupHistoryJpaEntity

        val query = jpaQueryFactory.selectFrom(qPickup)
            .leftJoin(qPickup.histories, qHistory)
            .fetchJoin()

        storeId?.let {
            query.where(qPickup.store.domainId.eq(it))
        }

        startAt?.let {
            query.where(qHistory.at.goe(it))
        }

        endAt?.let {
            query.where(qHistory.at.loe(it))
        }

        return query.distinct().fetch()
    }
}

package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.infrastructure.mysql.entity.PickupJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.QPickupHistoryJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.QPickupJpaEntity
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class PickupQuerydslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findAllBy(
        storeId: String?,
        requestActorId: String?,
        startAt: Instant?,
        endAt: Instant?
    ): List<PickupJpaEntity> {
        val qPickup = QPickupJpaEntity.pickupJpaEntity
        val qHistory = QPickupHistoryJpaEntity.pickupHistoryJpaEntity

        val query = jpaQueryFactory.selectFrom(qPickup)
            .leftJoin(qPickup.histories, qHistory)
            .fetchJoin()

        val conditions: MutableList<BooleanExpression> = mutableListOf()

        // storeId와 날짜 조건 추가
        storeId?.let {
            val storeConditions = qPickup.store.domainId.eq(it)
            startAt?.let { start -> storeConditions.and(qHistory.at.goe(start)) }
            endAt?.let { end -> storeConditions.and(qHistory.at.loe(end)) }
            conditions.add(storeConditions)
        }

        // requestActorId와 날짜 조건에 부합하는 pickupId 찾기
        val pickupIds: List<Long> = requestActorId?.let { actorId ->
            val actorConditions = qHistory.status.eq(Pickup.Status.REQUESTED)
                .and(qHistory.actor.domainId.eq(actorId))
            startAt?.let { start -> actorConditions.and(qHistory.at.goe(start)) }
            endAt?.let { end -> actorConditions.and(qHistory.at.loe(end)) }
            jpaQueryFactory.select(qPickup.id)
                .from(qPickup)
                .leftJoin(qPickup.histories, qHistory)
                .where(actorConditions)
                .fetch()
        } ?: emptyList()

        // 조건에 부합하는 pickupId가 있을 경우 해당 pickup 조회
        if (pickupIds.isNotEmpty()) {
            query.where(qPickup.id.`in`(pickupIds))
        } else {
            if (conditions.isNotEmpty()) {
                query.where(conditions.reduce { acc, condition -> acc.and(condition) })
            }
        }

        return query.distinct().fetch()
    }
}

package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture.actor
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture.pickupCommand
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture.pickupId
import com.hyunec.pickuprequest.infrastructure.mysql.TestDefaultSupport
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("PickupPersistencePort 구현체 테스트 - PickupJpaPersistenceAdapter")
class PickupJpaPersistenceAdapterTest(
    @Autowired private val sut: PickupJpaPersistenceAdapter,
    @Autowired private val pickupJpaRepository: PickupJpaRepository
) : TestDefaultSupport() {

    @RepeatedTest(10)
    fun `저장된 pickup 은 db 에서 조회할 수 있다`() {
        val pickupRequested = Fixture.pickupRequested()

        sut.save(pickupRequested)

        with(pickupJpaRepository.findByDomainId(pickupRequested.pickupId)!!) {
            domainId shouldBe pickupRequested.pickupId
            status shouldBe Pickup.Status.REQUESTED
            store shouldNotBe null
            label shouldBe null
            histories.size shouldBe 1
        }
    }

    @RepeatedTest(10)
    fun `저장된 pickup 에 update 를 할 수 있다`() {
        val pickupRequested = Fixture.pickupRequested()
        sut.save(pickupRequested)

        val pickupAccepted = pickupCommand<PickupCommand.Accept>(
            pickupId = pickupId(),
            actor = actor(Actor.Type.PARTNER_STORE_OWNER),
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupRequested.accept(it) }

        sut.update(pickupAccepted)

        with(pickupJpaRepository.findByDomainId(pickupRequested.pickupId)!!) {
            domainId shouldBe pickupRequested.pickupId
            status shouldBe Pickup.Status.ACCEPTED
            store shouldNotBe null
            label shouldBe null
            histories.size shouldBe 2
        }
    }

    @RepeatedTest(10)
    fun `존재하지 않는 pickupId 로 update 를 하면 EntityNotFoundException 을 발생한다`() {
        val pickup = Fixture.pickupRequested()

        val exception = shouldThrow<EntityNotFoundException> {
            sut.update(pickup)
        }

        exception.message shouldContain pickup.pickupId
    }

    @RepeatedTest(10)
    fun `존재하지 않는 pickupId 로 조회하면 null 을 반환한다`() {
        sut.findByPickupId("not-exist-pickupId") shouldBe null
    }
}

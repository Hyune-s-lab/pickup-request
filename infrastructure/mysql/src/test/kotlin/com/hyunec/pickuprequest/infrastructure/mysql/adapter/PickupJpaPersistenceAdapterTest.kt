package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture
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

        with(pickupJpaRepository.findByDomainId(pickupRequested.id)!!) {
            domainId shouldBe pickupRequested.id
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

        val pickupDriverActor = pickupRequested.histories.last().actor
        val partnerStoreOwnerActor = Fixture.actor(Actor.Type.PARTNER_STORE_OWNER)

        val pickupAccepted = pickupCommand<PickupCommand.Accept>(
            pickupId = pickupId(),
            actor = partnerStoreOwnerActor,
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupRequested.accept(it) }

        sut.update(pickupAccepted)

        with(pickupJpaRepository.findByDomainId(pickupRequested.id)!!) {
            domainId shouldBe pickupRequested.id
            status shouldBe Pickup.Status.ACCEPTED
            store shouldNotBe null
            label shouldBe null
            histories.size shouldBe 2
        }

        val pickupProcessed = pickupCommand<PickupCommand.Process>(
            pickupId = pickupId(),
            actor = pickupDriverActor,
            desc = Fixture.faker.lorem().sentence(),
            label = Fixture.label()
        ).let { pickupAccepted.process(it) }

        sut.update(pickupProcessed)

        with(pickupJpaRepository.findByDomainId(pickupRequested.id)!!) {
            domainId shouldBe pickupRequested.id
            status shouldBe Pickup.Status.PROCESSED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 3
        }

        val pickupApproved = pickupCommand<PickupCommand.Approve>(
            pickupId = pickupId(),
            actor = partnerStoreOwnerActor,
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupProcessed.approve(it) }

        sut.update(pickupApproved)

        with(pickupJpaRepository.findByDomainId(pickupRequested.id)!!) {
            domainId shouldBe pickupRequested.id
            status shouldBe Pickup.Status.APPROVED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 4
        }

        val pickupComplete = pickupCommand<PickupCommand.Complete>(
            pickupId = pickupId(),
            actor = Fixture.actor(Actor.Type.SYSTEM_AUTO),
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupApproved.complete(it) }

        sut.update(pickupComplete)

        with(pickupJpaRepository.findByDomainId(pickupRequested.id)!!) {
            domainId shouldBe pickupRequested.id
            status shouldBe Pickup.Status.COMPLETED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 5
        }
    }

    @RepeatedTest(10)
    fun `존재하지 않는 pickupId 로 update 를 하면 EntityNotFoundException 을 발생한다`() {
        val pickup = Fixture.pickupRequested()

        val exception = shouldThrow<EntityNotFoundException> {
            sut.update(pickup)
        }

        exception.message shouldContain pickup.id
    }

    @RepeatedTest(10)
    fun `존재하지 않는 pickupId 로 조회하면 null 을 반환한다`() {
        sut.findByDomainId("not-exist-pickupId") shouldBe null
    }
}

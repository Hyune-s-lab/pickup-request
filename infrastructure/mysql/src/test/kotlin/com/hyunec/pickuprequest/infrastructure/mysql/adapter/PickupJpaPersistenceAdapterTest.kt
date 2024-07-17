package com.hyunec.pickuprequest.infrastructure.mysql.adapter

import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.exception.EntityNotFoundException
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture
import com.hyunec.pickuprequest.infrastructure.mysql.Fixture.pickupCommand
import com.hyunec.pickuprequest.infrastructure.mysql.TestDefaultSupport
import com.hyunec.pickuprequest.infrastructure.mysql.entity.ActorJpaEntity
import com.hyunec.pickuprequest.infrastructure.mysql.entity.StoreJpaEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PickupPersistencePort 구현체 테스트 - PickupJpaPersistenceAdapter")
class PickupJpaPersistenceAdapterTest(
    @Autowired private val sut: PickupJpaPersistenceAdapter,

    @Autowired private val pickupJpaRepository: PickupJpaRepository,
    @Autowired private val storeJpaRepository: StoreJpaRepository,
    @Autowired private val actorJpaRepository: ActorJpaRepository
) : TestDefaultSupport() {

    private val fixture = Fixture.Sample()

    @BeforeAll
    fun beforeAll() {
        fixture.actors.forEach {
            actorJpaRepository.save(
                ActorJpaEntity(
                    domainId = it.value.id,
                    type = it.value.type,
                    name = it.value.name
                )
            )
        }

        fixture.store.let {
            storeJpaRepository.save(
                StoreJpaEntity(
                    domainId = it.id,
                    name = it.name,
                    address = it.address
                )
            )
        }
    }

    @RepeatedTest(10)
    fun `저장된 pickup 은 db 에서 조회할 수 있다`() {
        val pickup = Fixture.pickupCommand<PickupCommand.Request>(
            store = fixture.store,
            actor = fixture.actors[Actor.Type.PARTNER_STORE_OWNER]!!,
            desc = Fixture.faker.lorem().sentence()
        ).let { Pickup(ULIDGenerator.take(), it) }

        sut.save(pickup)

        with(pickupJpaRepository.findByDomainId(pickup.id)!!) {
            domainId shouldBe pickup.id
            status shouldBe Pickup.Status.REQUESTED
            store shouldNotBe null
            label shouldBe null
            histories.size shouldBe 1
        }
    }

    @RepeatedTest(10)
    fun `저장된 pickup 에 update 를 할 수 있다`() {
        val pickup = Fixture.pickupCommand<PickupCommand.Request>(
            store = fixture.store,
            actor = fixture.actors[Actor.Type.PARTNER_STORE_OWNER]!!,
            desc = Fixture.faker.lorem().sentence()
        ).let { Pickup(ULIDGenerator.take(), it) }
        sut.save(pickup)

        val pickupDriverActor = fixture.actors[Actor.Type.PICKUP_DRIVER]!!
        val partnerStoreOwnerActor = fixture.actors[Actor.Type.PARTNER_STORE_OWNER]!!
        val systemAutoActor = fixture.actors[Actor.Type.SYSTEM_AUTO]!!

        //
        val pickupAccepted = pickupCommand<PickupCommand.Accept>(
            pickupId = pickup.id,
            actor = pickupDriverActor,
            desc = Fixture.faker.lorem().sentence()
        ).let { pickup.accept(it) }

        sut.update(pickupAccepted)

        with(pickupJpaRepository.findByDomainId(pickup.id)!!) {
            domainId shouldBe pickup.id
            status shouldBe Pickup.Status.ACCEPTED
            store shouldNotBe null
            label shouldBe null
            histories.size shouldBe 2
        }

        //
        val pickupProcessed = pickupCommand<PickupCommand.Process>(
            pickupId = pickup.id,
            actor = pickupDriverActor,
            desc = Fixture.faker.lorem().sentence(),
            label = Fixture.label()
        ).let { pickupAccepted.process(it) }

        sut.update(pickupProcessed)

        with(pickupJpaRepository.findByDomainId(pickup.id)!!) {
            domainId shouldBe pickup.id
            status shouldBe Pickup.Status.PROCESSED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 3
        }

        //
        val pickupApproved = pickupCommand<PickupCommand.Approve>(
            pickupId = pickup.id,
            actor = partnerStoreOwnerActor,
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupProcessed.approve(it) }

        sut.update(pickupApproved)

        with(pickupJpaRepository.findByDomainId(pickup.id)!!) {
            domainId shouldBe pickup.id
            status shouldBe Pickup.Status.APPROVED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 4
        }

        //
        val pickupComplete = pickupCommand<PickupCommand.Complete>(
            pickupId = pickup.id,
            actor = systemAutoActor,
            desc = Fixture.faker.lorem().sentence()
        ).let { pickupApproved.complete(it) }

        sut.update(pickupComplete)

        with(pickupJpaRepository.findByDomainId(pickup.id)!!) {
            domainId shouldBe pickup.id
            status shouldBe Pickup.Status.COMPLETED
            store shouldNotBe null
            label shouldNotBe null
            histories.size shouldBe 5
        }
    }

    @RepeatedTest(10)
    fun `존재하지 않는 pickupId 로 update 를 하면 EntityNotFoundException 을 발생한다`() {
        val pickup = Fixture.pickupCommand<PickupCommand.Request>(
            store = fixture.store,
            actor = fixture.actors[Actor.Type.PARTNER_STORE_OWNER]!!,
            desc = Fixture.faker.lorem().sentence()
        ).let { Pickup(ULIDGenerator.take(), it) }

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

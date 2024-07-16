package com.hyunec.pickuprequest.domain.pickup.scenario

import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.domain.pickup.Fixture
import com.hyunec.pickuprequest.domain.pickup.TestDefaultSupport
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest

@DisplayName("scenario 테스트")
class ScenarioTest : TestDefaultSupport() {

    @RepeatedTest(10)
    fun `가맹사 점주의 수거 요청 scenario`() {
        val pickupId = Fixture.pickupId()
        val pickupDriverActor = Fixture.actor(Actor.Type.PICKUP_DRIVER)
        val partnerStoreOwnerActor = Fixture.actor(Actor.Type.PARTNER_STORE_OWNER)
        val systemAutoActor = Fixture.actor(Actor.Type.SYSTEM_AUTO)
        val store = Fixture.store()
        val label = Fixture.label()

        lateinit var pickup: Pickup

        // 1. 가맹사 점주: 점포를 지정하여 REQUESTED
        val requestCommand = Fixture.pickupCommand<PickupCommand.Request>(
            store = store,
            actor = partnerStoreOwnerActor,
            desc = faker.lorem().sentence()
        )

        pickup = Pickup(pickupId, requestCommand)

        with(pickup) {
            status shouldBe Pickup.Status.REQUESTED
            store shouldBe store
            histories.size shouldBe 1
            histories.last().actor shouldBe partnerStoreOwnerActor
        }

        // 2. 수거 기사: ACCEPTED
        val acceptCommand = Fixture.pickupCommand<PickupCommand.Accept>(
            pickupId = pickupId,
            actor = pickupDriverActor,
            desc = faker.lorem().sentence()
        )

        pickup.accept(acceptCommand)

        with(pickup) {
            status shouldBe Pickup.Status.ACCEPTED
            histories.size shouldBe 2
            histories.last().actor shouldBe pickupDriverActor
        }

        // 3. 수거 기사: 라벨을 기록하여 PROCESSED
        val processCommand = Fixture.pickupCommand<PickupCommand.Process>(
            pickupId = pickupId,
            label = label,
            actor = pickupDriverActor,
            desc = faker.lorem().sentence()
        )

        pickup.process(processCommand)

        with(pickup) {
            status shouldBe Pickup.Status.PROCESSED
            label shouldBe label
            histories.size shouldBe 3
            histories.last().actor shouldBe pickupDriverActor
        }

        // 4. 가맹사 점주: APPROVED
        val approveCommand = Fixture.pickupCommand<PickupCommand.Approve>(
            pickupId = pickupId,
            actor = partnerStoreOwnerActor,
            desc = faker.lorem().sentence()
        )

        pickup.approve(approveCommand)

        with(pickup) {
            status shouldBe Pickup.Status.APPROVED
            histories.size shouldBe 4
            histories.last().actor shouldBe partnerStoreOwnerActor
        }

        // 5. 자동 시스템: COMPLETED
        val completeCommand = Fixture.pickupCommand<PickupCommand.Complete>(
            pickupId = pickupId,
            actor = systemAutoActor
        )

        pickup.complete(completeCommand)

        with(pickup) {
            status shouldBe Pickup.Status.COMPLETED
            histories.size shouldBe 5
            histories.last().actor shouldBe systemAutoActor
        }
    }

    @RepeatedTest(10)
    fun `가맹사 어드민의 수거 요청 scenario`() {
        val pickupId = Fixture.pickupId()
        val pickupDriverActor = Fixture.actor(Actor.Type.PICKUP_DRIVER)
        val partnerAdminActor = Fixture.actor(Actor.Type.PARTNER_ADMIN)
        val partnerStoreOwnerActor = Fixture.actor(Actor.Type.PARTNER_STORE_OWNER)
        val systemAutoActor = Fixture.actor(Actor.Type.SYSTEM_AUTO)
        val store = Fixture.store()
        val label = Fixture.label()

        lateinit var pickup: Pickup

        // 1. 가맹사 어드민: 점포를 지정하여 REQUESTED
        val requestCommand = Fixture.pickupCommand<PickupCommand.Request>(
            store = store,
            actor = partnerAdminActor,
            desc = faker.lorem().sentence()
        )

        pickup = Pickup(pickupId, requestCommand)

        with(pickup) {
            status shouldBe Pickup.Status.REQUESTED
            store shouldBe store
            histories.size shouldBe 1
            histories.last().actor shouldBe partnerAdminActor
        }

        // 2. 수거 기사: ACCEPTED
        val acceptCommand = Fixture.pickupCommand<PickupCommand.Accept>(
            pickupId = pickupId,
            actor = pickupDriverActor,
            desc = faker.lorem().sentence()
        )

        pickup.accept(acceptCommand)

        with(pickup) {
            status shouldBe Pickup.Status.ACCEPTED
            histories.size shouldBe 2
            histories.last().actor shouldBe pickupDriverActor
        }

        // 3. 수거 기사: 라벨을 기록하여 PROCESSED
        val processCommand = Fixture.pickupCommand<PickupCommand.Process>(
            pickupId = pickupId,
            label = label,
            actor = pickupDriverActor,
            desc = faker.lorem().sentence()
        )

        pickup.process(processCommand)

        with(pickup) {
            status shouldBe Pickup.Status.PROCESSED
            label shouldBe label
            histories.size shouldBe 3
            histories.last().actor shouldBe pickupDriverActor
        }

        // 4. 가맹사 점주: APPROVED
        val approveCommand = Fixture.pickupCommand<PickupCommand.Approve>(
            pickupId = pickupId,
            actor = partnerStoreOwnerActor,
            desc = faker.lorem().sentence()
        )

        pickup.approve(approveCommand)

        with(pickup) {
            status shouldBe Pickup.Status.APPROVED
            histories.size shouldBe 4
            histories.last().actor shouldBe partnerStoreOwnerActor
        }

        // 5. 자동 시스템: COMPLETED
        val completeCommand = Fixture.pickupCommand<PickupCommand.Complete>(
            pickupId = pickupId,
            actor = systemAutoActor
        )

        pickup.complete(completeCommand)

        with(pickup) {
            status shouldBe Pickup.Status.COMPLETED
            histories.size shouldBe 5
            histories.last().actor shouldBe systemAutoActor
        }
    }
}

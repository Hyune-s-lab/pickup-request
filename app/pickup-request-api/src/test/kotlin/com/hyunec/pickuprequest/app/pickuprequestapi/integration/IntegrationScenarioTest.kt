package com.hyunec.pickuprequest.app.pickuprequestapi.integration

import com.hyunec.pickuprequest.app.pickuprequestapi.Fixture
import com.hyunec.pickuprequest.app.pickuprequestapi.TestDefaultSupport
import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.PickupCommandUsecase
import com.hyunec.pickuprequest.domain.pickup.port.PickupPersistencePort
import com.hyunec.pickuprequest.domain.pickup.port.PickupQueryUsecase
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired

class IntegrationScenarioTest(
    @Autowired private val pickupCommandUsecase: PickupCommandUsecase,
    @Autowired private val pickupQueryUsecase: PickupQueryUsecase,

    @Autowired private val pickupPersistencePort: PickupPersistencePort
) : TestDefaultSupport() {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.DisplayName::class)
    @Nested
    inner class `가맹사 점주의 수거 요청 scenario` {
        private val dummy = Fixture.Dummy()
        private lateinit var pickupId: String
        private lateinit var target: Pickup

        @Test
        fun `1) 가맹사 점주 - 점포를 지정하여 REQUESTED`() {
            val requestCommand = Fixture.pickupCommand<PickupCommand.Request>(
                store = dummy.store,
                actor = dummy.actors[Actor.Type.PARTNER_STORE_OWNER]!!,
                desc = faker.lorem().sentence()
            )

            pickupId = pickupCommandUsecase.request(requestCommand)
            
            target = pickupPersistencePort.findByDomainId(pickupId)!!
            with(target) {
                status shouldBe Pickup.Status.REQUESTED
                store shouldBe store
                histories.size shouldBe 1
                histories.last().actor shouldBe dummy.actors[Actor.Type.PARTNER_STORE_OWNER]!!
            }
        }

        @Test
        fun `2)수거 기사 - ACCEPTED`() {
            val acceptCommand = Fixture.pickupCommand<PickupCommand.Accept>(
                pickupId = pickupId,
                actor = dummy.actors[Actor.Type.PICKUP_DRIVER]!!,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.accept(acceptCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.ACCEPTED
                histories.size shouldBe 2
                histories.last().actor shouldBe dummy.actors[Actor.Type.PICKUP_DRIVER]!!
            }
        }

        @Test
        fun `3) 수거 기사 - 라벨을 기록하여 PROCESSED`() {
            val processCommand = Fixture.pickupCommand<PickupCommand.Process>(
                pickupId = pickupId,
                label = dummy.label,
                actor = dummy.actors[Actor.Type.PICKUP_DRIVER]!!,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.process(processCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.PROCESSED
                label shouldBe label
                histories.size shouldBe 3
                histories.last().actor shouldBe dummy.actors[Actor.Type.PICKUP_DRIVER]!!
            }
        }

        @Test
        fun `4) 가맹사 점주 - APPROVED`() {
            val approveCommand = Fixture.pickupCommand<PickupCommand.Approve>(
                pickupId = pickupId,
                actor = dummy.actors[Actor.Type.PARTNER_STORE_OWNER]!!,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.approve(approveCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.APPROVED
                histories.size shouldBe 4
                histories.last().actor shouldBe dummy.actors[Actor.Type.PARTNER_STORE_OWNER]!!
            }
        }

        @Test
        fun `5) 자동 시스템 - COMPLETED`() {
            val completeCommand = Fixture.pickupCommand<PickupCommand.Complete>(
                pickupId = pickupId,
                actor = dummy.actors[Actor.Type.SYSTEM_AUTO]!!
            )

            pickupCommandUsecase.complete(completeCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.COMPLETED
                histories.size shouldBe 5
                histories.last().actor shouldBe dummy.actors[Actor.Type.SYSTEM_AUTO]!!
            }
        }

        @Test
        fun `6) pickupQueryUsecase 로 조회해서 확인`() {
            pickupQueryUsecase.findByDomainId(pickupId) shouldBe target
        }
    }
}

package com.hyunec.pickuprequest.app.pickuprequestapi.integration

import com.hyunec.pickuprequest.app.pickuprequestapi.Fixture
import com.hyunec.pickuprequest.app.pickuprequestapi.TestDefaultSupport
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.*
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationScenarioTest(
    @Autowired private val pickupCommandUsecase: PickupCommandUsecase,
    @Autowired private val pickupQueryUsecase: PickupQueryUsecase,

    @Autowired private val pickupPersistencePort: PickupPersistencePort,

    @Autowired private val actorPersistencePort: ActorPersistencePort,
    @Autowired private val storePersistencePort: StorePersistencePort
) : TestDefaultSupport() {

    @BeforeAll
    fun beforeAll() {
        repeat(5) {
            Actor.Type.entries.forEach {
                actorPersistencePort.save(Fixture.actor(it))
            }
        }

        repeat(5) {
            storePersistencePort.save(Fixture.store())
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.DisplayName::class)
    @Nested
    inner class `가맹사 점주의 수거 요청 scenario` {
        private val pickupDriverActor = actorPersistencePort.findAll().find { it.type == Actor.Type.PICKUP_DRIVER }!!
        private val partnerStoreOwnerActor =
            actorPersistencePort.findAll().find { it.type == Actor.Type.PARTNER_STORE_OWNER }!!
        private val systemAutoActor = actorPersistencePort.findAll().find { it.type == Actor.Type.SYSTEM_AUTO }!!
        private val store = storePersistencePort.findAll().first()
        private val label = Fixture.label()

        private lateinit var pickupId: String
        private lateinit var target: Pickup

        @Test
        fun `1) 가맹사 점주 - 점포를 지정하여 REQUESTED`() {
            val requestCommand = Fixture.pickupCommand<PickupCommand.Request>(
                store = store,
                actor = partnerStoreOwnerActor,
                desc = faker.lorem().sentence()
            )

            pickupId = pickupCommandUsecase.request(requestCommand)

            target = pickupPersistencePort.findByDomainId(pickupId)!!
            with(target) {
                status shouldBe Pickup.Status.REQUESTED
                store shouldBe store
                histories.size shouldBe 1
                histories.last().actor shouldBe partnerStoreOwnerActor
            }
        }

        @Test
        fun `2)수거 기사 - ACCEPTED`() {
            val acceptCommand = Fixture.pickupCommand<PickupCommand.Accept>(
                pickupId = pickupId,
                actor = pickupDriverActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.accept(acceptCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.ACCEPTED
                histories.size shouldBe 2
                histories.last().actor shouldBe pickupDriverActor
            }
        }

        @Test
        fun `3) 수거 기사 - 라벨을 기록하여 PROCESSED`() {
            val processCommand = Fixture.pickupCommand<PickupCommand.Process>(
                pickupId = pickupId,
                label = label,
                actor = pickupDriverActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.process(processCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.PROCESSED
                label shouldBe label
                histories.size shouldBe 3
                histories.last().actor shouldBe pickupDriverActor
            }
        }

        @Test
        fun `4) 가맹사 점주 - APPROVED 를 하면 자동으로 COMPLETED 까지 실행`() {
            val approveCommand = Fixture.pickupCommand<PickupCommand.Approve>(
                pickupId = pickupId,
                actor = partnerStoreOwnerActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.approve(approveCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.COMPLETED
                histories.size shouldBe 5
                histories[histories.size - 2].actor shouldBe partnerStoreOwnerActor
                histories[histories.size - 2].status shouldBe Pickup.Status.APPROVED
                histories.last().actor shouldBe systemAutoActor
                histories.last().status shouldBe Pickup.Status.COMPLETED
            }
        }

        @Test
        fun `5) pickupQueryUsecase 로 조회해서 확인`() {
            pickupQueryUsecase.findByDomainId(pickupId) shouldBe target
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.DisplayName::class)
    @Nested
    inner class `가맹사 어드민의 수거 요청 scenario` {
        private val pickupDriverActor = actorPersistencePort.findAll().find { it.type == Actor.Type.PICKUP_DRIVER }!!
        private val partnerAdminActor = actorPersistencePort.findAll().find { it.type == Actor.Type.PARTNER_ADMIN }!!
        private val partnerStoreOwnerActor =
            actorPersistencePort.findAll().find { it.type == Actor.Type.PARTNER_STORE_OWNER }!!
        private val systemAutoActor = actorPersistencePort.findAll().find { it.type == Actor.Type.SYSTEM_AUTO }!!
        private val store = storePersistencePort.findAll().first()
        private val label = Fixture.label()

        private lateinit var pickupId: String
        private lateinit var target: Pickup

        @Test
        fun `1) 가맹사 어드민 - 점포를 지정하여 REQUESTED`() {
            val requestCommand = Fixture.pickupCommand<PickupCommand.Request>(
                store = store,
                actor = partnerAdminActor,
                desc = faker.lorem().sentence()
            )

            pickupId = pickupCommandUsecase.request(requestCommand)

            target = pickupPersistencePort.findByDomainId(pickupId)!!
            with(target) {
                status shouldBe Pickup.Status.REQUESTED
                store shouldBe store
                histories.size shouldBe 1
                histories.last().actor shouldBe partnerAdminActor
            }
        }

        @Test
        fun `2)수거 기사 - ACCEPTED`() {
            val acceptCommand = Fixture.pickupCommand<PickupCommand.Accept>(
                pickupId = pickupId,
                actor = pickupDriverActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.accept(acceptCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.ACCEPTED
                histories.size shouldBe 2
                histories.last().actor shouldBe pickupDriverActor
            }
        }

        @Test
        fun `3) 수거 기사 - 라벨을 기록하여 PROCESSED`() {
            val processCommand = Fixture.pickupCommand<PickupCommand.Process>(
                pickupId = pickupId,
                label = label,
                actor = pickupDriverActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.process(processCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.PROCESSED
                label shouldBe label
                histories.size shouldBe 3
                histories.last().actor shouldBe pickupDriverActor
            }
        }

        @Test
        fun `4) 가맹사 점주 - APPROVED 를 하면 자동으로 COMPLETED 까지 실행`() {
            val approveCommand = Fixture.pickupCommand<PickupCommand.Approve>(
                pickupId = pickupId,
                actor = partnerStoreOwnerActor,
                desc = faker.lorem().sentence()
            )

            pickupCommandUsecase.approve(approveCommand)
            target = pickupPersistencePort.findByDomainId(pickupId)!!

            with(target) {
                status shouldBe Pickup.Status.COMPLETED
                histories.size shouldBe 5
                histories[histories.size - 2].actor shouldBe partnerStoreOwnerActor
                histories[histories.size - 2].status shouldBe Pickup.Status.APPROVED
                histories.last().actor shouldBe systemAutoActor
                histories.last().status shouldBe Pickup.Status.COMPLETED
            }
        }

        @Test
        fun `5) pickupQueryUsecase 로 조회해서 확인`() {
            pickupQueryUsecase.findByDomainId(pickupId) shouldBe target
        }
    }
}

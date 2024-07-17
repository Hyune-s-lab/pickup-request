package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.service.ActorService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupCommandService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupQueryService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.StoreService
import com.hyunec.pickuprequest.common.util.id.ULIDGenerator
import com.hyunec.pickuprequest.domain.pickup.entity.Pickup
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import org.springframework.web.bind.annotation.*

@RestController
class PickupController(
    private val pickupCommandService: PickupCommandService,
    private val pickupQueryService: PickupQueryService,

    private val actorService: ActorService,
    private val storeService: StoreService
) {
    @PostMapping("/pickups")
    fun request(@RequestBody request: Request.Request): Request.Response {
        val pickupId = pickupCommandService.request(
            PickupCommand.Request(
                actor = actorService.findByActorId(request.actorId),
                store = storeService.findByStoreId(request.storeId),
                desc = request.desc,
            )
        )

        return Request.Response(pickupId)
    }

    @PatchMapping("/pickups/{pickupId}/accept")
    fun accept(
        @PathVariable pickupId: String,
        @RequestBody request: Accept.Request
    ): Accept.Response {
        return pickupCommandService.accept(
            PickupCommand.Accept(
                actor = actorService.findByActorId(request.actorId),
                pickupId = pickupId,
                desc = request.desc,
            )
        ).let { Accept.Response(it) }
    }

    @PatchMapping("/pickups/{pickupId}/process")
    fun process(
        @PathVariable pickupId: String,
        @RequestBody request: Process.Request
    ): Process.Response {
        return pickupCommandService.process(
            PickupCommand.Process(
                actor = actorService.findByActorId(request.actorId),
                pickupId = pickupId,
                desc = request.desc,
                label = getLabel()
            )
        ).let { Process.Response(it) }
    }

    @PatchMapping("/pickups/{pickupId}/approve")
    fun approve(
        @PathVariable pickupId: String,
        @RequestBody request: Approve.Request
    ): Approve.Response {
        return pickupCommandService.approve(
            PickupCommand.Approve(
                actor = actorService.findByActorId(request.actorId),
                pickupId = pickupId,
                desc = request.desc,
            )
        ).let { Approve.Response(it) }
    }

    // todo
    private fun getLabel(): Pickup.Label {
        return Pickup.Label(
            id = ULIDGenerator.take(),
            qrcode = ULIDGenerator.take(),
            volume = 16000,
            images = listOf(
                Pickup.Label.Image("https://example.com/image.jpg")
            )
        )
    }

    class Request {
        data class Request(
            val actorId: String,
            val storeId: String,
            val desc: String?
        )

        data class Response(
            val pickupId: String
        )
    }

    class Accept {
        data class Request(
            val actorId: String,
            val desc: String?
        )

        data class Response(
            val pickupId: String
        )
    }

    class Process {
        data class Request(
            val actorId: String,
            val desc: String?
        )

        data class Response(
            val pickupId: String
        )
    }

    class Approve {
        data class Request(
            val actorId: String,
            val desc: String?
        )

        data class Response(
            val pickupId: String
        )
    }
}

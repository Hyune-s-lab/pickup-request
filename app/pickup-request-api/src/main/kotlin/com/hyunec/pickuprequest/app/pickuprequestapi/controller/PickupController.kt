package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.service.ActorService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupCommandService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupQueryService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.StoreService
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
}

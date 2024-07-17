package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupCommandService
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupQueryService
import com.hyunec.pickuprequest.common.model.Actor
import com.hyunec.pickuprequest.common.model.Store
import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PickupController(
    private val pickupCommandService: PickupCommandService,
    private val pickupQueryService: PickupQueryService
) {
    @PostMapping("/pickups")
    fun request(@RequestBody request: Request): Response {

        val pickupId = pickupCommandService.request(
            PickupCommand.Request(
                actor = getActor(request.actorId),
                store = getStore(request.storeId),
                desc = request.desc,
            )
        )

        return Response(pickupId)
    }

    // todo
    private fun getActor(actorId: String): Actor {
        return Actor(actorId, Actor.Type.PARTNER_STORE_OWNER, "actorName")
    }

    // todo
    private fun getStore(storeId: String): Store {
        return Store(storeId, "storeName", "storeAddress")
    }

    data class Request(
        val actorId: String,
        val storeId: String,
        val desc: String?
    )

    data class Response(
        val pickupId: String
    )
}

package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.ActorModel
import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.StoreModel
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
                actor = Actor(request.actor.id, request.actor.type, "noname"),
                store = Store(request.store.id, request.store.name, request.store.address),
                desc = request.desc ?: "",
                pickupId = "1234"
            )
        )

        return Response(pickupId)
    }

    data class Request(
        val actor: ActorModel,
        val store: StoreModel,
        val desc: String?
    )

    data class Response(
        val pickupId: String
    )
}

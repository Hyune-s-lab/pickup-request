package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.service.ActorService
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ActorController(
    private val actorService: ActorService
) {
    @PostMapping("/actors")
    fun create(@RequestBody request: Request): Response {
        val actorId = actorService.create(request.type, request.name)
        return Response(actorId)
    }

    data class Request(
        val type: Actor.Type,
        val name: String
    )

    data class Response(
        val pickupId: String
    )
}

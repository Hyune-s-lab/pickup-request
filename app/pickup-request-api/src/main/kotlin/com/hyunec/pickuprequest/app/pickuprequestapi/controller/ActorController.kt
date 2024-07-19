package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.ActorModel
import com.hyunec.pickuprequest.app.pickuprequestapi.service.ActorService
import com.hyunec.pickuprequest.domain.pickup.entity.Actor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ActorController(
    private val actorService: ActorService
) {
    @GetMapping("/actors")
    fun findAll(): FindAll.Response {
        val actors = actorService.findAll().map { ActorModel(it) }
        return FindAll.Response(actors)
    }

    @PostMapping("/actors")
    fun create(@RequestBody request: Create.Request): Create.Response {
        val actorId = actorService.create(request.type, request.name)
        return Create.Response(actorId)
    }

    class FindAll {
        data class Response(
            val actors: List<ActorModel>
        )
    }

    class Create {
        data class Request(
            val type: Actor.Type,
            val name: String
        )

        data class Response(
            val actorId: String
        )
    }
}

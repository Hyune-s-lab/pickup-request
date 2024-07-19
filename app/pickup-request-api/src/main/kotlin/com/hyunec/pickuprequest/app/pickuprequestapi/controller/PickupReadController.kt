package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.PickupModel
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PickupReadController(
    private val pickupQueryService: PickupQueryService,
) {
    @GetMapping("/pickups/{pickupId}")
    fun readOne(@PathVariable pickupId: String): ReadOne.Response {
        return ReadOne.Response(
            pickup = PickupModel(pickupQueryService.findByDomainId(pickupId))
        )
    }

    class ReadOne {
        data class Response(
            val pickup: PickupModel
        )
    }
}

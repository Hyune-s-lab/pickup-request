package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.PickupModel
import com.hyunec.pickuprequest.app.pickuprequestapi.service.PickupQueryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

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

    @GetMapping("/pickups")
    fun readAll(
        @RequestParam(required = false) storeId: String?,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam startedAt: Instant?,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam endedAt: Instant?
    ): ReadAll.Response {
        val pickups = pickupQueryService.findAllBy(storeId, startedAt, endedAt).map { PickupModel(it) }
        return ReadAll.Response(
            pickups = pickups,
            totalCount = pickups.size,
        )
    }

    class ReadOne {
        data class Response(
            val pickup: PickupModel
        )
    }

    class ReadAll {
        data class Response(
            val pickups: List<PickupModel>,
            val totalCount: Int
        )
    }
}

package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.service.StoreService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StoreController(
    private val storeService: StoreService
) {
    @PostMapping("/stores")
    fun create(@RequestBody request: Request): Response {
        val storeId = storeService.create(request.name, request.address)
        return Response(storeId)
    }

    data class Request(
        val name: String,
        val address: String
    )

    data class Response(
        val storeId: String
    )
}

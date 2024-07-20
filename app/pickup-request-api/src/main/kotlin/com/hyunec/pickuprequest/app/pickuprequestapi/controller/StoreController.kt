package com.hyunec.pickuprequest.app.pickuprequestapi.controller

import com.hyunec.pickuprequest.app.pickuprequestapi.controller.model.StoreModel
import com.hyunec.pickuprequest.app.pickuprequestapi.service.StoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StoreController(
    private val storeService: StoreService
) {
    @GetMapping("/stores")
    fun findAll(): FindAll.Response {
        val stores = storeService.findAll().map { StoreModel(it) }
        return FindAll.Response(stores)
    }

    @PostMapping("/stores")
    fun create(@RequestBody request: Create.Request): Create.Response {
        val storeId = storeService.create(request.name, request.address)
        return Create.Response(storeId)
    }

    class FindAll {
        data class Response(
            val stores: List<StoreModel>
        )
    }

    class Create {
        data class Request(
            val name: String,
            val address: String
        )

        data class Response(
            val storeId: String
        )
    }
}

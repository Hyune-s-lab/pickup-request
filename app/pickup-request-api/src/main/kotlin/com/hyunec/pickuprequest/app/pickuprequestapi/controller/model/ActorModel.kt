package com.hyunec.pickuprequest.app.pickuprequestapi.controller.model

import com.hyunec.pickuprequest.common.model.Actor

data class ActorModel(
    val id: String,
    val type: Actor.Type,
)

package com.hyunec.pickuprequest.domain.pickup.port

import com.hyunec.pickuprequest.domain.pickup.port.command.PickupCommand

interface PickupCommandUsecase {

    fun request(command: PickupCommand.Request): String

    fun accept(command: PickupCommand.Accept): String

    fun process(command: PickupCommand.Process): String

    fun approve(command: PickupCommand.Approve): String

    fun cancel(command: PickupCommand.Cancel): String
}

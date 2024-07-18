package com.hyunec.pickuprequest.domain.pickup.exception

class EntityNotFoundException : DomainException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}

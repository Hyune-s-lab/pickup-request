package com.hyunec.pickuprequest.common.util

object PreconditionsSupporter {
    @PublishedApi
    internal const val NO_SUITABLE_CONSTRUCTOR_MESSAGE = "Required condition failed and no suitable constructor found."

    inline fun <reified E : RuntimeException> require(value: Boolean, lazyMessage: () -> Any) {
        if (!value) throw throwException<E>(lazyMessage)
    }

    inline fun <reified E : RuntimeException> require(value: Boolean) {
        if (!value) throw throwException<E>()
    }

    inline fun <reified E : RuntimeException> check(value: Boolean, lazyMessage: () -> Any) {
        if (!value) throw throwException<E>(lazyMessage)
    }

    inline fun <reified E : RuntimeException> check(value: Boolean) {
        if (!value) throw throwException<E>()
    }

    @PublishedApi
    internal inline fun <reified E : RuntimeException> throwException(lazyMessage: () -> Any): E {
        val message = lazyMessage().toString()
        val constructor = E::class.constructors.find {
            it.parameters.size == 1 && it.parameters[0].type.classifier == String::class
        }
        return constructor?.call(message)
            ?: throw IllegalArgumentException(NO_SUITABLE_CONSTRUCTOR_MESSAGE)
    }

    @PublishedApi
    internal inline fun <reified E : RuntimeException> throwException(): E {
        val constructor = E::class.constructors.find { it.parameters.isEmpty() }
        return constructor?.call()
            ?: throw IllegalArgumentException(NO_SUITABLE_CONSTRUCTOR_MESSAGE)
    }
}

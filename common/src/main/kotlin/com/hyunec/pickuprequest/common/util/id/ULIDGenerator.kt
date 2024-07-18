package com.hyunec.pickuprequest.common.util.id

import com.github.f4b6a3.ulid.Ulid

object ULIDGenerator : IdGenerator {
    override fun take(): String {
        return Ulid.fast().toString()
    }
}

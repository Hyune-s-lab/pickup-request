package com.hyunec.pickuprequest.domain.pickup

import com.hyunec.pickuprequest.common.util.KLogging
import net.datafaker.Faker
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(value = ["test"])
abstract class TestDefaultSupport {

    companion object : KLogging() {
        @JvmStatic
        protected val faker = Faker()
    }
}

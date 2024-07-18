package com.hyunec.pickuprequest.infrastructure.mysql

import com.hyunec.pickuprequest.common.util.KLogging
import net.datafaker.Faker
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(value = ["test"])
@Import(TestContainerConfig::class)
@SpringBootTest
abstract class TestDefaultSupport {

    companion object : KLogging() {
        @JvmStatic
        protected val faker = Faker()
    }
}

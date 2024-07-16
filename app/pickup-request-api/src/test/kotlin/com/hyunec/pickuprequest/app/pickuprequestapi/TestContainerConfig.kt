package com.hyunec.pickuprequest.app.pickuprequestapi

import com.hyunec.pickuprequest.common.util.KLogging
import org.springframework.boot.test.context.TestConfiguration
import org.testcontainers.containers.MySQLContainer

@TestConfiguration
class TestContainerConfig {

    companion object : KLogging() {

        @JvmField
        val mysqlContainer = MySQLContainer("mysql:8.0.33").apply {
            withDatabaseName("pickup")
            withUsername("root")
            withPassword("u1234")
        }

        init {
            mysqlContainer.start()
            System.setProperty("spring.datasource.url", mysqlContainer.jdbcUrl)
            System.setProperty("spring.datasource.username", mysqlContainer.username)
            System.setProperty("spring.datasource.password", mysqlContainer.password)
        }
    }
}

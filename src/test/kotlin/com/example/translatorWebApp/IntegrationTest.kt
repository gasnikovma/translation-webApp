package com.example.translatorWebApp

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager
import kotlin.test.assertEquals

@Testcontainers
class IntegrationTest {

    companion object {
        @Container
        val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:16").apply {
            withDatabaseName("translation")
            withUsername("postgres")
            withPassword("postgres")
        }

        @JvmStatic
        @DynamicPropertySource
        fun jdbcProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgreSQLContainer.jdbcUrl }
            registry.add("spring.datasource.username") { postgreSQLContainer.username }
            registry.add("spring.datasource.password") { postgreSQLContainer.password }
        }

    }

    @BeforeEach
    fun setup(){
        DriverManager.getConnection(
            postgreSQLContainer.jdbcUrl, postgreSQLContainer.username,
            postgreSQLContainer.password
        ).use {
            val createStatement =
                it.prepareStatement("CREATE TABLE IF NOT EXISTS translations(id SERIAL PRIMARY KEY, ip VARCHAR(45) NOT NULL, original_text TEXT NOT NULL,translated_text TEXT NOT NULL);")
            createStatement.execute()
        }
    }

    @Test
    fun testDatabaseConnection() {
        DriverManager.getConnection(
            postgreSQLContainer.jdbcUrl, postgreSQLContainer.username,
            postgreSQLContainer.password
        ).use {
            val userIp = "192.168.1.1"
            val originalText = "Hello"
            val translatedText = "Привет"
            val insertStatement =
                it.prepareStatement("INSERT INTO translations(ip,original_text,translated_text) VALUES (?,?,?)")
            insertStatement.setString(1,userIp)
            insertStatement.setString(2,originalText)
            insertStatement.setString(3,translatedText)
            insertStatement.execute()

            val selectRequest = it.prepareStatement("SELECT * FROM translations where ip=(?)")
            selectRequest.setString(1,"192.168.1.1")
            val resultSet = selectRequest.resultSet
            resultSet.next()
            assertEquals(resultSet.getString(2),"192.168.1.1")
            assertEquals(resultSet.getString(3),"Hello")
            assertEquals(resultSet.getString(4),"Привет")

        }
    }

}
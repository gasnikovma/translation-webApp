package com.example.translatorWebApp.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DatabaseConfig(
    @Value("\${spring.datasource.url}") val url: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String
) {
    @Bean
    fun datasource(): DataSource {
        val datasource = DriverManagerDataSource()
        datasource.setDriverClassName("org.postgresql.Driver")
        datasource.url = url
        datasource.username = username
        datasource.password = password
        return datasource
    }

}
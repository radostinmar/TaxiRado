package com.taxirado.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("TaxiRado API")
                    .version("1.0.0")
                    .description("REST API for TaxiRado ridesharing platform. Users can register as drivers or passengers, drivers can announce rides, and passengers can book available rides.")
                    .contact(
                        Contact()
                            .name("TaxiRado")
                            .url("https://github.com/radostinmar/TaxiRado")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("Local server")
            )
    }
}

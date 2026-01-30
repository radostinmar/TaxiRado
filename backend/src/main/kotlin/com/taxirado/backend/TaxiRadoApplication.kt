package com.taxirado.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaxiRadoApplication

fun main(args: Array<String>) {
    runApplication<TaxiRadoApplication>(*args)
}

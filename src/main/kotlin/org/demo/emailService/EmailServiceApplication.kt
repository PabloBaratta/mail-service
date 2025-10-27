package org.demo.emailService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class EmailServiceApplication

fun main(args: Array<String>) {
    runApplication<EmailServiceApplication>(*args)
}

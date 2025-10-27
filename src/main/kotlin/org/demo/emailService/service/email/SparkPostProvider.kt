package org.demo.emailService.service.email

import com.sparkpost.Client
import com.sparkpost.exception.SparkPostException
import org.demo.emailService.dto.mail.MailRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SparkPostProvider(
    @Value("\${sparkpost.api.key}") private val apiKey: String
) : EmailProvider {

    private val client = Client(apiKey)

    override fun sendMail(request: MailRequest): Boolean = try {
        client.sendMessage(
           request.from,
            request.to,
            request.subject,
            request.text,
            null
        )
        true
    } catch (ex: SparkPostException) {
        false
    } catch (ex: Exception) {
        false
    }
}
package org.demo.emailService.dto

data class ApiErrorResponse(
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String,
    val errors: Map<String, String>? = null,
)

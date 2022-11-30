package ru.tinkoff.rr.clients.dto

import kotlinx.serialization.Serializable

@Serializable
data class JobRetry(val retries: Int, val jobIds: List<String>)

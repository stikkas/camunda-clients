package ru.tinkoff.rr.clients.dto

import kotlinx.serialization.Serializable

@Serializable
data class CamundaJob(
    val exceptionMessage: String?,
    val id: String,
    val jobDefinitionId: String,
    val processInstanceId: String,
    val processDefinitionId: String,
    val processDefinitionKey: String,
    val executionId: String,
    val failedActivityId: String?,
    val retries: Int,
    val dueDate: String?,
    val suspended: Boolean,
    val priority: Int,
    val tenantId: String?,
    val createTime: String?
)

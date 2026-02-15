package com.slmoney.app.domain.model

import java.time.LocalDateTime

enum class TransactionType { CREDIT, DEBIT }

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val merchantName: String?,
    val categoryId: Long?,
    val accountId: Long,
    val description: String,
    val date: LocalDateTime,
    val balanceAfter: Double?,
    val rawSmsBody: String,
    val smsSender: String,
    val isManual: Boolean = false,
    val recurringGroupId: Long? = null,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

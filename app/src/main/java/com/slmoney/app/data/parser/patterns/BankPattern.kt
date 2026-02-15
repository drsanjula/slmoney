package com.slmoney.app.data.parser

import com.slmoney.app.domain.model.TransactionType
import java.time.LocalDateTime

data class ParsedTransaction(
    val amount: Double,
    val type: TransactionType,
    val merchantName: String?,
    val accountMask: String?,
    val balanceAfter: Double?,
    val date: LocalDateTime?,
    val bankCode: String,
    val rawBody: String,
    val confidence: Float
)

interface BankPattern {
    val senderIds: List<String>
    val bankCode: String
    fun parse(body: String, timestamp: Long): ParsedTransaction?
}

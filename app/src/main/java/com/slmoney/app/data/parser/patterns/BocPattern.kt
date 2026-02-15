package com.slmoney.app.data.parser.patterns

import com.slmoney.app.data.parser.ParsedTransaction
import com.slmoney.app.domain.model.TransactionType
import java.time.LocalDateTime

class BocPattern : BankPattern {
    override val senderIds = listOf("BOC", "Bank of Ceylon")
    override val bankCode = "BOC"

    // Example: Debit of Rs. 1,000.00 from A/c XXXXX1234 on 30/10/2025.
    private val debitRegex = Regex("""(?i)Debit of (?:Rs\.?|LKR)\s*([\d,]+\.?\d*) from A/c\s*[Xx*]*(\d{4})""")
    
    // Example: Your BOC Credit Card ending 1234 has been debited for LKR 5,000.00 at Keells.
    private val cardDebitRegex = Regex("""(?i)Card ending (\d{4}) has been debited for (?:Rs\.?|LKR)\s*([\d,]+\.?\d*) at (.*)""")

    override fun parse(body: String, timestamp: Long): ParsedTransaction? {
        val debitMatch = debitRegex.find(body)
        if (debitMatch != null) {
            return ParsedTransaction(
                amount = debitMatch.groupValues[1].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = null,
                accountMask = debitMatch.groupValues[2],
                balanceAfter = null,
                date = null,
                bankCode = bankCode,
                rawBody = body,
                confidence = 0.9f
            )
        }

        val cardMatch = cardDebitRegex.find(body)
        if (cardMatch != null) {
            return ParsedTransaction(
                amount = cardMatch.groupValues[2].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = cardMatch.groupValues[3].trim(),
                accountMask = cardMatch.groupValues[1],
                balanceAfter = null,
                date = null,
                bankCode = bankCode,
                rawBody = body,
                confidence = 0.95f
            )
        }

        return null
    }
}

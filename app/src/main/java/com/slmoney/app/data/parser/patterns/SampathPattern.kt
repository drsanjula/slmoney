package com.slmoney.app.data.parser.patterns

import com.slmoney.app.data.parser.ParsedTransaction
import com.slmoney.app.domain.model.TransactionType
import java.time.LocalDateTime

class SampathPattern : BankPattern {
    override val senderIds = listOf("Sampath", "SampathBank")
    override val bankCode = "SAMPATH"

    // Example: Rs. 1,500.00 debited from A/c XXX1234. Merchant: Keells Super.
    private val debitRegex = Regex("""(?i)(?:Rs\.?|LKR)\s*([\d,]+\.?\d*) debited from A/c\s*[Xx*]*(\d{4})""")
    private val merchantRegex = Regex("""(?i)Merchant:\s*(.*)""")

    override fun parse(body: String, timestamp: Long): ParsedTransaction? {
        val debitMatch = debitRegex.find(body)
        if (debitMatch != null) {
            val merchantMatch = merchantRegex.find(body)
            return ParsedTransaction(
                amount = debitMatch.groupValues[1].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = merchantMatch?.groupValues[1]?.trim(),
                accountMask = debitMatch.groupValues[2],
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

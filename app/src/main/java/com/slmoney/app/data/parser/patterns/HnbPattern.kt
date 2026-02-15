package com.slmoney.app.data.parser.patterns

import com.slmoney.app.data.parser.ParsedTransaction
import com.slmoney.app.domain.model.TransactionType

class HnbPattern : BankPattern {
    override val senderIds = listOf("HNB", "HNB-Alert")
    override val bankCode = "HNB"

    // Example: HNB: Rs 1,000.00 debited from A/c *1234 at Keells. Bal: Rs 5,000.00
    private val debitRegex = Regex("""(?i)Rs\s*([\d,]+\.?\d*) debited from A/c\s*[Xx*]*(\d{4}) at (.*?)\.""")
    private val balanceRegex = Regex("""(?i)Bal:\s*Rs\s*([\d,]+\.?\d*)""")

    override fun parse(body: String, timestamp: Long): ParsedTransaction? {
        val debitMatch = debitRegex.find(body)
        if (debitMatch != null) {
            val balanceMatch = balanceRegex.find(body)
            return ParsedTransaction(
                amount = debitMatch.groupValues[1].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = debitMatch.groupValues[3].trim(),
                accountMask = debitMatch.groupValues[2],
                balanceAfter = balanceMatch?.groupValues[1]?.replace(",", "")?.toDouble(),
                date = null,
                bankCode = bankCode,
                rawBody = body,
                confidence = 0.95f
            )
        }
        return null
    }
}

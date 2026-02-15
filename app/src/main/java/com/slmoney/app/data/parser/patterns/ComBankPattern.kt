package com.slmoney.app.data.parser.patterns

import com.slmoney.app.data.parser.ParsedTransaction
import com.slmoney.app.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ComBankPattern : BankPattern {
    override val senderIds = listOf("ComBank", "COMBANK", "ComBankSMS")
    override val bankCode = "COMBANK"

    // Example: Debit of Rs. 1,250.43 from your A/c XXXXX1234 on 30-Oct at 08:50. Bal: Rs. 8,521.25.
    private val debitRegex = Regex("""(?i)Debit of (?:Rs\.?|LKR)\s*([\d,]+\.?\d*) from your A/c\s*[Xx*]*(\d{4}) on ([\d-]+) at ([\d:]+)\. Bal: (?:Rs\.?|LKR)\s*([\d,]+\.?\d*)""")
    
    // Example: Rs. 2,650.00 debited from your A/c XXXXX1234 at Uber on 30-Oct.
    private val spendRegex = Regex("""(?i)(?:Rs\.?|LKR)\s*([\d,]+\.?\d*) debited from your A/c\s*[Xx*]*(\d{4}) at (.*?) on ([\d-]+)""")

    override fun parse(body: String, timestamp: Long): ParsedTransaction? {
        val debitMatch = debitRegex.find(body)
        if (debitMatch != null) {
            return ParsedTransaction(
                amount = debitMatch.groupValues[1].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = null, // Simple account debit usually doesn't have merchant in this format
                accountMask = debitMatch.groupValues[2],
                balanceAfter = debitMatch.groupValues[5].replace(",", "").toDouble(),
                date = null, // Will use SMS timestamp as fallback
                bankCode = bankCode,
                rawBody = body,
                confidence = 0.9f
            )
        }

        val spendMatch = spendRegex.find(body)
        if (spendMatch != null) {
            return ParsedTransaction(
                amount = spendMatch.groupValues[1].replace(",", "").toDouble(),
                type = TransactionType.DEBIT,
                merchantName = spendMatch.groupValues[3].trim(),
                accountMask = spendMatch.groupValues[2],
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

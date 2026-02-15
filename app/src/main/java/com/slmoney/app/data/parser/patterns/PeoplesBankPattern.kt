package com.slmoney.app.data.parser.patterns

import com.slmoney.app.data.parser.ParsedTransaction
import com.slmoney.app.domain.model.TransactionType

class PeoplesBankPattern : BankPattern {
    override val senderIds = listOf("PEOPLESBANK", "PeoplesBank")
    override val bankCode = "PEOPLES"

    // Example: PO: Rs. 1,500.00 spent on Card *1234 at Uber.
    private val spendRegex = Regex("""(?i)Rs\.?\s*([\d,]+\.?\d*) spent on Card\s*[Xx*]*(\d{4}) at (.*)""")

    override fun parse(body: String, timestamp: Long): ParsedTransaction? {
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
                confidence = 0.9f
            )
        }
        return null
    }
}

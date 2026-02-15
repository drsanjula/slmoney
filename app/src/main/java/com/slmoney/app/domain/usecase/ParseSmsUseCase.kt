package com.slmoney.app.domain.usecase

import com.slmoney.app.data.parser.SmsParserEngine
import com.slmoney.app.data.parser.TransactionCategorizer
import com.slmoney.app.data.repository.TransactionRepositoryImpl
import com.slmoney.app.domain.model.Transaction
import javax.inject.Inject

class ParseSmsUseCase @Inject constructor(
    private val parser: SmsParserEngine,
    private val repository: TransactionRepositoryImpl,
    private val categorizer: TransactionCategorizer,
    private val detectRecurringUseCase: DetectRecurringUseCase
) {
    suspend operator fun invoke(sender: String, body: String, timestamp: Long): Transaction? {
        val parsed = parser.parse(sender, body, timestamp) ?: return null
        
        // Deduplicate using SMS hash
        val hash = java.security.MessageDigest.getInstance("SHA-256")
            .digest(body.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
            
        if (repository.isDuplicate(hash)) return null

        // TODO: Map account mask to real accountId from AccountRepository
        val dummyAccountId = 1L 

        val transaction = Transaction(
            amount = parsed.amount,
            type = parsed.type,
            merchantName = parsed.merchantName,
            categoryId = null, // Will be categorized next
            accountId = dummyAccountId,
            description = parsed.merchantName ?: "Transaction at ${parsed.bankCode}",
            date = parsed.date!!,
            balanceAfter = parsed.balanceAfter,
            rawSmsBody = body,
            smsSender = sender
        )
        
        repository.insertTransaction(transaction)
        
        // Detect recurring
        detectRecurringUseCase(transaction)
        
        return transaction
    }
}

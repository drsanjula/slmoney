package com.slmoney.app.domain.usecase

import com.slmoney.app.data.local.db.dao.RecurringTransactionDao
import com.slmoney.app.data.local.db.dao.TransactionDao
import com.slmoney.app.data.local.db.entity.RecurringTransactionEntity
import com.slmoney.app.domain.model.Transaction
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class DetectRecurringUseCase @Inject constructor(
    private val transactionDao: TransactionDao,
    private val recurringDao: RecurringTransactionDao
) {
    suspend operator fun invoke(newTransaction: Transaction) {
        if (newTransaction.merchantName == null) return

        // Find existing recurring rule
        val existing = recurringDao.findByMerchant(newTransaction.merchantName)
        if (existing != null) {
            // Update last applied date
            recurringDao.insert(
                existing.copy(
                    lastAppliedDateMillis = newTransaction.date.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    nextExpectedDateMillis = newTransaction.date.plusMonths(1).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                )
            )
            return
        }

        // Logic to detect NEW recurring: find a previous transaction from this merchant with similar amount ~30 days ago
        // This is a simplified version for now
        // TODO: Implement more robust fuzzy matching
    }
}

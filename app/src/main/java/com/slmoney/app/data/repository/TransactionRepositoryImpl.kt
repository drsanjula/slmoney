package com.slmoney.app.data.repository

import com.slmoney.app.data.local.db.dao.TransactionDao
import com.slmoney.app.data.local.db.entity.TransactionEntity
import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<Transaction>> = dao.getAll().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return dao.insert(transaction.toEntity())
    }

    suspend fun isDuplicate(hash: String): Boolean {
        return dao.findByHash(hash) != null
    }

    private fun TransactionEntity.toDomain() = Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf(type),
        merchantName = merchantName,
        categoryId = categoryId,
        accountId = accountId,
        description = description,
        date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateMillis), ZoneId.systemDefault()),
        balanceAfter = balanceAfter,
        rawSmsBody = rawSmsBody,
        smsSender = smsSender,
        isManual = isManual,
        recurringGroupId = recurringGroupId,
        notes = notes,
        createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.systemDefault())
    )

    private fun Transaction.toEntity() = TransactionEntity(
        amount = amount,
        type = type.name,
        merchantName = merchantName,
        categoryId = categoryId,
        accountId = accountId,
        description = description,
        dateMillis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        balanceAfter = balanceAfter,
        rawSmsBody = rawSmsBody,
        smsSender = smsSender,
        isManual = isManual,
        recurringGroupId = recurringGroupId,
        notes = notes,
        createdAtMillis = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        smsHash = java.security.MessageDigest.getInstance("SHA-256")
            .digest(rawSmsBody.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    )
}

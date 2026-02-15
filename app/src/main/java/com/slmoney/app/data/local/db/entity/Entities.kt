package com.slmoney.app.data.local.db.entity

import androidx.room.*

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String,                   // "CREDIT" | "DEBIT"
    val merchantName: String?,
    val categoryId: Long?,
    val accountId: Long,
    val description: String,
    val dateMillis: Long,               // epoch millis
    val balanceAfter: Double?,
    val rawSmsBody: String,
    val smsSender: String,
    val isManual: Boolean,
    val recurringGroupId: Long?,
    val notes: String?,
    val createdAtMillis: Long,
    val smsHash: String                 // SHA-256 for dedup
)

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bankName: String,
    val bankCode: String,
    val accountMask: String,
    val accountType: String,
    val currentBalance: Double?,
    val currency: String,
    val lastUpdatedMillis: Long?
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val isDefault: Boolean,
    val parentId: Long?
)

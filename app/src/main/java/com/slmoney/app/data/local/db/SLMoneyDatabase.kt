package com.slmoney.app.data.local.db

import androidx.room.*
import com.slmoney.app.data.local.db.dao.*
import com.slmoney.app.data.local.db.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        RecurringTransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SLMoneyDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun categoryDao(): CategoryDao
}

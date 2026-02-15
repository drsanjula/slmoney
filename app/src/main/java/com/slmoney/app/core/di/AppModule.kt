package com.slmoney.app.core.di

import android.content.Context
import androidx.room.Room
import com.slmoney.app.data.local.db.SLMoneyDatabase
import com.slmoney.app.data.local.db.dao.AccountDao
import com.slmoney.app.data.local.db.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SLMoneyDatabase {
        return Room.databaseBuilder(
            context,
            SLMoneyDatabase::class.java,
            "slmoney_db"
        ).build()
    }

    @Provides
    fun provideTransactionDao(db: SLMoneyDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideAccountDao(db: SLMoneyDatabase): AccountDao = db.accountDao()

    @Provides
    fun provideRecurringTransactionDao(db: SLMoneyDatabase): com.slmoney.app.data.local.db.dao.RecurringTransactionDao = db.recurringTransactionDao()

    @Provides
    fun provideCategoryDao(db: SLMoneyDatabase): com.slmoney.app.data.local.db.dao.CategoryDao = db.categoryDao()
}

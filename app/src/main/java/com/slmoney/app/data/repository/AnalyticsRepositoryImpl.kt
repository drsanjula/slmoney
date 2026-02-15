package com.slmoney.app.data.repository

import com.slmoney.app.data.local.db.dao.TransactionDao
import com.slmoney.app.domain.model.Category
import com.slmoney.app.domain.model.CategoryAmount
import com.slmoney.app.domain.model.SpendingSummary
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) {
    suspend fun getSpendingSummary(startDate: LocalDate, endDate: LocalDate): SpendingSummary {
        val startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // Mock values for now since we haven't implemented the complex queries yet
        val totalIncome = 150000.0
        val totalExpense = 45000.0
        
        val breakdown = listOf(
            CategoryAmount(
                category = Category(name = "Food", iconName = "restaurant", colorHex = "#FF5722"),
                amount = 18000.0,
                percentage = 0.4f,
                transactionCount = 12
            ),
            CategoryAmount(
                category = Category(name = "Transport", iconName = "directions_car", colorHex = "#2196F3"),
                amount = 9000.0,
                percentage = 0.2f,
                transactionCount = 8
            )
        )

        return SpendingSummary(
            period = "Current Month",
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netSavings = totalIncome - totalExpense,
            categoryBreakdown = breakdown,
            dailySpending = emptyList()
        )
    }
}

package com.slmoney.app.domain.usecase

import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import javax.inject.Inject

class CalculateFinancialHealthUseCase @Inject constructor() {
    
    operator fun invoke(transactions: List<Transaction>): Int {
        if (transactions.isEmpty()) return 0
        
        val income = transactions.filter { it.type == TransactionType.CREDIT }.sumOf { it.amount }
        val expenses = transactions.filter { it.type == TransactionType.DEBIT }.sumOf { it.amount }
        
        if (income == 0.0) return 0
        
        val savingsRate = (income - expenses) / income
        
        // Base score starts at 50
        var score = 50
        
        // Add points for savings rate (up to 30 points)
        score += (savingsRate * 30).toInt().coerceIn(0, 30)
        
        // Add points for consistency (dummy for now, but could check # of days with activity)
        score += 10
        
        // Add points for category diversity
        val categoryCount = transactions.mapNotNull { it.categoryId }.distinct().size
        score += (categoryCount * 2).coerceIn(0, 10)
        
        return score.coerceIn(0, 100)
    }
}

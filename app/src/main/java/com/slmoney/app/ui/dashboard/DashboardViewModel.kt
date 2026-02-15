package com.slmoney.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slmoney.app.data.repository.TransactionRepositoryImpl
import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepositoryImpl
) : ViewModel() {

    val recentTransactions: StateFlow<List<Transaction>> = transactionRepository
        .getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val spendingSummary: StateFlow<SpendingSummaryState> = transactionRepository
        .getAllTransactions()
        .map { transactions ->
            val income = transactions.filter { it.type == TransactionType.CREDIT }.sumOf { it.amount }
            val expense = transactions.filter { it.type == TransactionType.DEBIT }.sumOf { it.amount }
            SpendingSummaryState(income, expense)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SpendingSummaryState()
        )
}

data class SpendingSummaryState(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val balance: Double = income - expense
)

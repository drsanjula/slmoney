package com.slmoney.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slmoney.app.data.repository.TransactionRepositoryImpl
import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepositoryImpl,
    private val healthUseCase: com.slmoney.app.domain.usecase.CalculateFinancialHealthUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    val recentTransactions: StateFlow<List<Transaction>> = kotlinx.coroutines.flow.combine(
        transactionRepository.getAllTransactions(),
        _searchQuery
    ) { transactions, query ->
        if (query.isBlank()) transactions
        else transactions.filter { 
            it.merchantName?.contains(query, ignoreCase = true) == true ||
            it.description.contains(query, ignoreCase = true)
        }
    }
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
            val healthScore = healthUseCase(transactions)
            SpendingSummaryState(income, expense, healthScore = healthScore)
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
    val balance: Double = income - expense,
    val healthScore: Int = 0
)

package com.slmoney.app.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slmoney.app.data.repository.TransactionRepositoryImpl
import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ManualEntryViewModel @Inject constructor(
    private val repository: TransactionRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManualEntryState())
    val uiState = _uiState.asStateFlow()

    fun onAmountChange(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun onMerchantChange(merchant: String) {
        _uiState.value = _uiState.value.copy(merchant = merchant)
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val amountValue = _uiState.value.amount.toDoubleOrNull() ?: 0.0
            val transaction = Transaction(
                amount = amountValue,
                type = _uiState.value.type,
                merchantName = _uiState.value.merchant,
                categoryId = null,
                accountId = 1L, // Default account
                description = _uiState.value.merchant,
                date = LocalDateTime.now(),
                balanceAfter = null,
                rawSmsBody = "Manual Entry",
                smsSender = "User",
                isManual = true
            )
            repository.insertTransaction(transaction)
            _uiState.value = ManualEntryState(isSaved = true)
        }
    }
}

data class ManualEntryState(
    val amount: String = "",
    val merchant: String = "",
    val type: TransactionType = TransactionType.DEBIT,
    val isSaved: Boolean = false
)

package com.slmoney.app.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slmoney.app.data.repository.AnalyticsRepositoryImpl
import com.slmoney.app.domain.model.SpendingSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepositoryImpl
) : ViewModel() {

    private val _summary = MutableStateFlow<SpendingSummary?>(null)
    val summary: StateFlow<SpendingSummary?> = _summary.asStateFlow()

    init {
        loadSummary()
    }

    private fun loadSummary() {
        viewModelScope.launch {
            val end = LocalDate.now()
            val start = end.minusMonths(1)
            _summary.value = analyticsRepository.getSpendingSummary(start, end)
        }
    }
}

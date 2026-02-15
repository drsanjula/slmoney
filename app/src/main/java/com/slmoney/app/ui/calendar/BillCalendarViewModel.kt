package com.slmoney.app.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slmoney.app.data.local.db.dao.RecurringTransactionDao
import com.slmoney.app.data.local.db.entity.RecurringTransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BillCalendarViewModel @Inject constructor(
    private val recurringDao: RecurringTransactionDao
) : ViewModel() {

    val upcomingBills: StateFlow<List<RecurringTransactionEntity>> = recurringDao
        .getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

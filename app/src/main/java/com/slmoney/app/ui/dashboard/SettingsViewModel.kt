package com.slmoney.app.ui.dashboard

import androidx.lifecycle.ViewModel
import com.slmoney.app.core.preferences.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsManager: SettingsManager
) : ViewModel()

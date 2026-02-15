package com.slmoney.app.domain.model

import java.time.LocalDateTime

enum class AccountType { SAVINGS, CURRENT, CREDIT_CARD, WALLET }

data class Account(
    val id: Long = 0,
    val bankName: String,
    val bankCode: String,
    val accountMask: String,
    val accountType: AccountType,
    val currentBalance: Double?,
    val currency: String = "LKR",
    val lastUpdated: LocalDateTime?,
    val iconRes: Int? = null
)

package com.slmoney.app.data.parser

import com.slmoney.app.data.parser.patterns.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankPatternRegistry @Inject constructor() {
    private val patterns = listOf(
        ComBankPattern(),
        BocPattern(),
        SampathPattern()
    )

    fun findPattern(sender: String): BankPattern? {
        return patterns.find { pattern ->
            pattern.senderIds.any { it.equals(sender, ignoreCase = true) }
        }
    }
    
    fun getAllPatterns(): List<BankPattern> = patterns
}

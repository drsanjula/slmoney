package com.slmoney.app.data.parser

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsParserEngine @Inject constructor(
    private val registry: BankPatternRegistry,
    private val categorizer: TransactionCategorizer
) {
    /**
     * Parse a single SMS from a sender.
     * Returns a ParsedTransaction if successful, null otherwise.
     */
    fun parse(sender: String, body: String, timestamp: Long): ParsedTransaction? {
        val pattern = registry.findPattern(sender) ?: return null
        
        return pattern.parse(body, timestamp)?.let { parsed ->
            // If date is missing from SMS body, use the message timestamp
            val finalDate = parsed.date ?: LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
            )
            
            parsed.copy(date = finalDate)
        }
    }
}

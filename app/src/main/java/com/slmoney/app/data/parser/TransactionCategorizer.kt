package com.slmoney.app.data.parser

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionCategorizer @Inject constructor() {
    private val keywords = mapOf(
        "Food & Dining" to listOf(
            "keells", "cargills", "arpico", "laugfs", "kfc",
            "mcdonalds", "pizza hut", "dominos", "uber eats",
            "pickme food", "foodpanda", "eliyada", "brew 1867"
        ),
        "Transport" to listOf(
            "uber", "pickme", "grab", "ceypetco", "ioc",
            "lanka ioc", "fuel", "parking"
        ),
        "Utilities" to listOf(
            "dialog", "mobitel", "hutch", "slt", "leco",
            "ceb", "waterboard", "nwsdb"
        ),
        "Subscriptions" to listOf(
            "netflix", "spotify", "youtube", "apple",
            "google play", "amazon prime", "disney"
        ),
        "Shopping" to listOf(
            "odel", "fashion bug", "cool planet",
            "daraz", "aliexpress", "amazon"
        ),
        "Health" to listOf(
            "nawaloka", "asiri", "durdans", "lankahospitals",
            "pharmacy", "healthguard"
        ),
        "Education" to listOf(
            "university", "coursera", "udemy", "school fees"
        )
    )

    fun categorize(merchantName: String?): String? {
        if (merchantName == null) return null
        val lower = merchantName.lowercase()
        return keywords.entries.firstOrNull { (_, list) ->
            list.any { lower.contains(it) }
        }?.key
    }
}

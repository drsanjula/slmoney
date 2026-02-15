# SL Money — Technical Specification

## Table of Contents
1. [Project Overview](#project-overview)
2. [File Structure](#file-structure)
3. [Data Types](#data-types)
4. [Database Schema](#database-schema)
5. [Core Function Signatures](#core-function-signatures)
6. [Step-by-Step Logic for Complex Parts](#step-by-step-logic)
7. [Material 3 Expressive Theme Setup](#m3-expressive-theme)
8. [Dependency List](#dependencies)

---

## 1. Project Overview

**App Name:** SL Money  
**Package:** `com.slmoney.app`  
**Min SDK:** 26 (Android 8.0)  
**Target SDK:** 35 (Android 15)  
**Language:** Kotlin 2.0  
**UI:** Jetpack Compose + Material 3 Expressive  
**Architecture:** MVVM + Clean Architecture (3-layer)  

---

## 2. File Structure

```
app/
├── build.gradle.kts
├── src/
│   └── main/
│       ├── AndroidManifest.xml
│       ├── java/com/slmoney/app/
│       │   ├── SLMoneyApp.kt                          # Application class + Hilt entry
│       │   ├── MainActivity.kt                        # Single activity, Compose host
│       │   │
│       │   ├── core/
│       │   │   ├── di/
│       │   │   │   ├── AppModule.kt                   # Hilt: DB, DataStore, dispatchers
│       │   │   │   ├── RepositoryModule.kt            # Hilt: repo bindings
│       │   │   │   └── UseCaseModule.kt               # Hilt: use case bindings
│       │   │   ├── util/
│       │   │   │   ├── CurrencyFormatter.kt           # Rs. formatting helpers
│       │   │   │   ├── DateUtils.kt                   # Date grouping, relative time
│       │   │   │   └── Result.kt                      # sealed Result<T> wrapper
│       │   │   └── navigation/
│       │   │       ├── NavGraph.kt                    # Top-level nav graph
│       │   │       ├── Screen.kt                      # Sealed class of routes
│       │   │       └── BottomNavBar.kt                # M3 NavigationBar composable
│       │   │
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   ├── db/
│       │   │   │   │   ├── SLMoneyDatabase.kt         # Room database definition
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── TransactionDao.kt
│       │   │   │   │   │   ├── AccountDao.kt
│       │   │   │   │   │   ├── CategoryDao.kt
│       │   │   │   │   │   ├── BudgetDao.kt
│       │   │   │   │   │   └── RecurringDao.kt
│       │   │   │   │   ├── entity/
│       │   │   │   │   │   ├── TransactionEntity.kt
│       │   │   │   │   │   ├── AccountEntity.kt
│       │   │   │   │   │   ├── CategoryEntity.kt
│       │   │   │   │   │   ├── BudgetEntity.kt
│       │   │   │   │   │   └── RecurringEntity.kt
│       │   │   │   │   └── converter/
│       │   │   │   │       └── Converters.kt          # Room type converters
│       │   │   │   └── datastore/
│       │   │   │       └── UserPreferences.kt         # DataStore proto/prefs
│       │   │   ├── repository/
│       │   │   │   ├── TransactionRepositoryImpl.kt
│       │   │   │   ├── AccountRepositoryImpl.kt
│       │   │   │   ├── BudgetRepositoryImpl.kt
│       │   │   │   └── AnalyticsRepositoryImpl.kt
│       │   │   └── parser/
│       │   │       ├── SmsParserEngine.kt             # Orchestrator
│       │   │       ├── BankPatternRegistry.kt         # Maps bank senders → patterns
│       │   │       ├── ParsedTransaction.kt           # Intermediate parse result
│       │   │       └── patterns/
│       │   │           ├── BankPattern.kt             # Interface for bank patterns
│       │   │           ├── BocPattern.kt              # Bank of Ceylon
│       │   │           ├── ComBankPattern.kt          # Commercial Bank
│       │   │           ├── SampathPattern.kt          # Sampath Bank
│       │   │           ├── HnbPattern.kt              # HNB
│       │   │           ├── PeoplesPattern.kt          # People's Bank
│       │   │           ├── NdbPattern.kt              # NDB
│       │   │           ├── SeylanPattern.kt           # Seylan
│       │   │           ├── DfccPattern.kt             # DFCC
│       │   │           ├── NsbPattern.kt              # NSB
│       │   │           └── GenericPattern.kt          # Fallback regex
│       │   │
│       │   ├── domain/
│       │   │   ├── model/
│       │   │   │   ├── Transaction.kt
│       │   │   │   ├── Account.kt
│       │   │   │   ├── Category.kt
│       │   │   │   ├── Budget.kt
│       │   │   │   ├── RecurringTransaction.kt
│       │   │   │   ├── SpendingSummary.kt
│       │   │   │   ├── CashFlow.kt
│       │   │   │   └── FinancialHealthScore.kt
│       │   │   ├── repository/
│       │   │   │   ├── TransactionRepository.kt       # Interface
│       │   │   │   ├── AccountRepository.kt
│       │   │   │   ├── BudgetRepository.kt
│       │   │   │   └── AnalyticsRepository.kt
│       │   │   └── usecase/
│       │   │       ├── ParseSmsUseCase.kt
│       │   │       ├── GetTransactionsUseCase.kt
│       │   │       ├── GetSpendingSummaryUseCase.kt
│       │   │       ├── GetCashFlowUseCase.kt
│       │   │       ├── DetectRecurringUseCase.kt
│       │   │       ├── CalculateHealthScoreUseCase.kt
│       │   │       ├── CategorizeTransactionUseCase.kt
│       │   │       └── ManageBudgetUseCase.kt
│       │   │
│       │   ├── service/
│       │   │   ├── TransactionNotificationListener.kt # NotificationListenerService
│       │   │   └── SmsReceiver.kt                     # BroadcastReceiver fallback
│       │   │
│       │   └── ui/
│       │       ├── theme/
│       │       │   ├── Theme.kt                       # M3 Expressive dynamic theme
│       │       │   ├── Color.kt                       # Fallback palette
│       │       │   ├── Type.kt                        # Typography scale
│       │       │   ├── Shape.kt                       # Shape system (squircles)
│       │       │   └── Motion.kt                      # Spring specs, animations
│       │       ├── components/
│       │       │   ├── TransactionCard.kt
│       │       │   ├── AccountCarousel.kt
│       │       │   ├── SpendingChart.kt
│       │       │   ├── BudgetProgressBar.kt
│       │       │   ├── QuickStatCard.kt
│       │       │   ├── CategoryChip.kt
│       │       │   ├── TimeRangeSelector.kt
│       │       │   ├── EmptyState.kt
│       │       │   └── AnimatedFab.kt
│       │       ├── dashboard/
│       │       │   ├── DashboardScreen.kt
│       │       │   └── DashboardViewModel.kt
│       │       ├── timeline/
│       │       │   ├── TimelineScreen.kt
│       │       │   └── TimelineViewModel.kt
│       │       ├── analytics/
│       │       │   ├── AnalyticsScreen.kt
│       │       │   └── AnalyticsViewModel.kt
│       │       ├── budget/
│       │       │   ├── BudgetScreen.kt
│       │       │   └── BudgetViewModel.kt
│       │       ├── accounts/
│       │       │   ├── AccountsScreen.kt
│       │       │   └── AccountsViewModel.kt
│       │       ├── detail/
│       │       │   ├── TransactionDetailScreen.kt
│       │       │   └── TransactionDetailViewModel.kt
│       │       ├── settings/
│       │       │   ├── SettingsScreen.kt
│       │       │   └── SettingsViewModel.kt
│       │       └── onboarding/
│       │           ├── OnboardingScreen.kt
│       │           └── OnboardingViewModel.kt
│       │
│       └── res/
│           ├── values/
│           │   ├── strings.xml
│           │   └── themes.xml
│           └── drawable/
│               └── (category icons, bank logos)
│
├── build.gradle.kts (project-level)
└── gradle/
    └── libs.versions.toml                             # Version catalog
```

---

## 3. Data Types

### Domain Models

```kotlin
// ── Transaction ──────────────────────────────────────────
enum class TransactionType { CREDIT, DEBIT }

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val merchantName: String?,          // "UberEats", "Netflix", null if unknown
    val categoryId: Long?,
    val accountId: Long,
    val description: String,            // Raw or cleaned description
    val date: LocalDateTime,
    val balanceAfter: Double?,          // Balance after txn, if available
    val rawSmsBody: String,             // Original SMS for debugging
    val smsSender: String,              // e.g. "ComBank", "BOC"
    val isManual: Boolean = false,      // true if manually entered
    val recurringGroupId: Long? = null, // links to RecurringTransaction
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

// ── Account ──────────────────────────────────────────────
enum class AccountType { SAVINGS, CURRENT, CREDIT_CARD, WALLET }

data class Account(
    val id: Long = 0,
    val bankName: String,               // "BOC", "Commercial Bank"
    val bankCode: String,               // "BOC", "COMBANK"
    val accountMask: String,            // "XXXX1234"
    val accountType: AccountType,
    val currentBalance: Double?,
    val currency: String = "LKR",
    val lastUpdated: LocalDateTime?,
    val iconRes: Int? = null            // drawable resource for bank logo
)

// ── Category ─────────────────────────────────────────────
data class Category(
    val id: Long = 0,
    val name: String,                   // "Food & Dining", "Transport"
    val iconName: String,               // Material icon name
    val colorHex: String,               // "#FF5722"
    val isDefault: Boolean = true,      // false for user-created
    val parentId: Long? = null          // for sub-categories
)

// ── Budget ───────────────────────────────────────────────
enum class BudgetPeriod { WEEKLY, MONTHLY, YEARLY }

data class Budget(
    val id: Long = 0,
    val categoryId: Long?,              // null = overall budget
    val limitAmount: Double,
    val period: BudgetPeriod,
    val spentAmount: Double = 0.0,      // computed dynamically
    val startDate: LocalDate,
    val isActive: Boolean = true
)

// ── RecurringTransaction ─────────────────────────────────
enum class RecurrenceFrequency { DAILY, WEEKLY, BIWEEKLY, MONTHLY, YEARLY }

data class RecurringTransaction(
    val id: Long = 0,
    val merchantName: String,
    val averageAmount: Double,
    val frequency: RecurrenceFrequency,
    val categoryId: Long?,
    val accountId: Long,
    val lastOccurrence: LocalDate,
    val nextExpected: LocalDate,
    val occurrenceCount: Int,
    val isSubscription: Boolean = false  // user-confirmed subscription
)

// ── Analytics Models ─────────────────────────────────────
data class SpendingSummary(
    val period: String,                  // "Feb 2026", "Week 7"
    val totalIncome: Double,
    val totalExpense: Double,
    val netSavings: Double,
    val categoryBreakdown: List<CategoryAmount>,
    val dailySpending: List<DailyAmount>
)

data class CategoryAmount(
    val category: Category,
    val amount: Double,
    val percentage: Float,              // 0.0 – 1.0
    val transactionCount: Int
)

data class DailyAmount(
    val date: LocalDate,
    val amount: Double
)

data class CashFlow(
    val month: YearMonth,
    val income: Double,
    val expense: Double,
    val net: Double
)

data class FinancialHealthScore(
    val score: Int,                     // 0–100
    val savingsRate: Float,             // % of income saved
    val budgetAdherence: Float,         // % budgets within limit
    val spendingTrend: TrendDirection,
    val tips: List<String>
)

enum class TrendDirection { IMPROVING, STABLE, DECLINING }
```

### SMS Parser Intermediate Types

```kotlin
data class ParsedTransaction(
    val amount: Double,
    val type: TransactionType,
    val merchantName: String?,
    val accountMask: String?,           // "XXXX1234"
    val balanceAfter: Double?,
    val date: LocalDateTime?,           // extracted or SMS timestamp
    val bankCode: String,
    val rawBody: String,
    val confidence: Float               // 0.0–1.0 parse confidence
)

data class BankSender(
    val senderIds: List<String>,        // ["ComBank", "COMBANK", "ComBankSMS"]
    val bankCode: String,               // "COMBANK"
    val bankName: String,               // "Commercial Bank"
    val patterns: List<BankPattern>
)
```

---

## 4. Database Schema

### Room Entities

```kotlin
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String,                   // "CREDIT" | "DEBIT"
    val merchantName: String?,
    val categoryId: Long?,
    val accountId: Long,
    val description: String,
    val dateMillis: Long,               // epoch millis
    val balanceAfter: Double?,
    val rawSmsBody: String,
    val smsSender: String,
    val isManual: Boolean,
    val recurringGroupId: Long?,
    val notes: String?,
    val createdAtMillis: Long,
    val smsHash: String                 // SHA-256 of rawSmsBody → dedup
)

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bankName: String,
    val bankCode: String,
    val accountMask: String,
    val accountType: String,
    val currentBalance: Double?,
    val currency: String,
    val lastUpdatedMillis: Long?
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val isDefault: Boolean,
    val parentId: Long?
)

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long?,
    val limitAmount: Double,
    val period: String,
    val startDateMillis: Long,
    val isActive: Boolean
)

@Entity(tableName = "recurring_transactions")
data class RecurringEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val merchantName: String,
    val averageAmount: Double,
    val frequency: String,
    val categoryId: Long?,
    val accountId: Long,
    val lastOccurrenceMillis: Long,
    val nextExpectedMillis: Long,
    val occurrenceCount: Int,
    val isSubscription: Boolean
)
```

### Key DAO Signatures

```kotlin
@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE dateMillis BETWEEN :startMillis AND :endMillis
        ORDER BY dateMillis DESC
    """)
    fun getTransactionsInRange(startMillis: Long, endMillis: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE accountId = :accountId
        ORDER BY dateMillis DESC
    """)
    fun getByAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT categoryId, SUM(amount) as total, COUNT(*) as count
        FROM transactions
        WHERE type = 'DEBIT' AND dateMillis BETWEEN :startMillis AND :endMillis
        GROUP BY categoryId
    """)
    suspend fun getCategoryTotals(startMillis: Long, endMillis: Long): List<CategoryTotal>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE type = :type AND dateMillis BETWEEN :startMillis AND :endMillis
    """)
    suspend fun getTotalByType(type: String, startMillis: Long, endMillis: Long): Double?

    @Query("SELECT * FROM transactions WHERE smsHash = :hash LIMIT 1")
    suspend fun findByHash(hash: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Query("""
        SELECT * FROM transactions
        WHERE merchantName LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
        ORDER BY dateMillis DESC
    """)
    fun search(query: String): Flow<List<TransactionEntity>>
}

data class CategoryTotal(
    val categoryId: Long?,
    val total: Double,
    val count: Int
)
```

---

## 5. Core Function Signatures

### SMS Parser Engine

```kotlin
class SmsParserEngine @Inject constructor(
    private val bankRegistry: BankPatternRegistry,
    private val categorizer: TransactionCategorizer
) {
    /**
     * Parse a single SMS body from a known sender.
     * Returns null if the message is not a transaction alert.
     */
    fun parse(sender: String, body: String, timestamp: Long): ParsedTransaction?

    /**
     * Batch-parse all SMS messages from the device inbox.
     * Used during initial onboarding scan.
     */
    suspend fun parseAllSms(
        contentResolver: ContentResolver
    ): List<ParsedTransaction>
}

interface BankPattern {
    /** Sender IDs this pattern accepts (e.g. ["ComBank", "COMBANK"]) */
    val senderIds: List<String>

    /** Bank identifier code */
    val bankCode: String

    /** Attempt to parse the SMS body. Return null if no match. */
    fun parse(body: String, timestamp: Long): ParsedTransaction?
}
```

### Notification Listener Service

```kotlin
class TransactionNotificationListener : NotificationListenerService() {
    /**
     * Called when a new notification is posted.
     * Filters bank app package names, extracts text, delegates to parser.
     */
    override fun onNotificationPosted(sbn: StatusBarNotification)

    /**
     * Returns set of bank package names to watch.
     */
    private fun getBankPackageNames(): Set<String>

    /**
     * Extract title + text from notification extras.
     */
    private fun extractNotificationText(notification: Notification): String?
}
```

### Use Cases

```kotlin
class ParseSmsUseCase @Inject constructor(
    private val parser: SmsParserEngine,
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository
) {
    /**
     * Parse an SMS, deduplicate, auto-categorize, store.
     * Returns the stored Transaction or null if duplicate/unparseable.
     */
    suspend operator fun invoke(
        sender: String, body: String, timestamp: Long
    ): Transaction?
}

class GetSpendingSummaryUseCase @Inject constructor(
    private val analyticsRepo: AnalyticsRepository
) {
    /**
     * Compute spending summary for the given date range.
     */
    suspend operator fun invoke(
        startDate: LocalDate, endDate: LocalDate
    ): SpendingSummary
}

class DetectRecurringUseCase @Inject constructor(
    private val transactionRepo: TransactionRepository,
    private val recurringRepo: RecurringRepository
) {
    /**
     * Scan all transactions, group by merchant + similar amount,
     * detect frequency patterns, and upsert RecurringTransaction records.
     */
    suspend operator fun invoke(): List<RecurringTransaction>
}

class CalculateHealthScoreUseCase @Inject constructor(
    private val analyticsRepo: AnalyticsRepository,
    private val budgetRepo: BudgetRepository
) {
    /**
     * Calculate a 0–100 financial health score.
     */
    suspend operator fun invoke(): FinancialHealthScore
}

class CategorizeTransactionUseCase @Inject constructor(
    private val categoryRepo: CategoryRepository
) {
    /**
     * Auto-categorize using merchant keyword matching.
     * Returns best-match Category or null for "Uncategorized".
     */
    suspend operator fun invoke(merchantName: String?): Category?
}
```

---

## 6. Step-by-Step Logic for Complex Parts

### 6.1 SMS Parsing Pipeline

```
STEP 1: Receive notification / SMS
  ├── NotificationListenerService.onNotificationPosted(sbn)
  │     → check sbn.packageName against bank package set
  │     → extract text from notification.extras
  └── SmsReceiver.onReceive()  (fallback)
        → extract sender + body from SmsMessage[]

STEP 2: Identify the bank
  → BankPatternRegistry.findBank(sender: String): BankSender?
  → lookup sender against all registered BankSender.senderIds
  → return null if no match (not a bank message)

STEP 3: Parse with bank-specific patterns
  → Try each BankPattern.parse(body, timestamp) in order
  → Each pattern uses regex to extract:
      amount    → (?:Rs\.?\s*|LKR\s*)([\d,]+\.?\d*)
      type      → contains("credited"|"received"|"deposit") → CREDIT
                  contains("debited"|"spent"|"withdrawn"|"purchase") → DEBIT
      merchant  → text after "at " or "to " or "from "
      account   → (?:A/c|Card|account)\s*(?:No\.?\s*)?[Xx*]*(\d{4})
      balance   → (?:Bal|Balance|Avl\.?\s*Bal)[:\s]*([\d,]+\.?\d*)
  → First successful parse wins

STEP 4: Deduplicate
  → Compute SHA-256 hash of rawSmsBody
  → Query TransactionDao.findByHash(hash)
  → If exists → skip (return null)

STEP 5: Auto-categorize
  → CategorizeTransactionUseCase(merchantName)
  → Keyword map lookup:
      "uber"|"pickme"|"grab"     → Transport
      "keells"|"cargills"|"arpico" → Groceries
      "dialog"|"mobitel"|"slt"   → Utilities
      "netflix"|"spotify"|"youtube" → Subscriptions
      (etc.)
  → If no keyword match → "Uncategorized"

STEP 6: Resolve account
  → Match accountMask against existing Account records
  → If no match → create new Account (bankCode, mask, type guess)

STEP 7: Store
  → Map ParsedTransaction → TransactionEntity
  → TransactionDao.insert(entity)
  → Update Account.currentBalance if balanceAfter != null

STEP 8: Notify UI
  → Room Flow automatically emits to ViewModel → Compose recomposes
```

### 6.2 Recurring Transaction Detection Algorithm

```
STEP 1: Fetch all DEBIT transactions from last 90 days

STEP 2: Group by normalized merchant name
  → lowercase, trim, remove extra spaces
  → group: Map<String, List<Transaction>>
  → filter: keep groups with >= 2 transactions

STEP 3: For each merchant group, detect periodicity
  → Sort transactions by date ascending
  → Compute intervals: gaps = dates[i+1] - dates[i] in days
  → Compute median interval

STEP 4: Classify frequency from median interval
  → 1  day          → DAILY
  → 6–8 days        → WEEKLY
  → 13–16 days      → BIWEEKLY
  → 27–35 days      → MONTHLY
  → 350–380 days    → YEARLY
  → else            → not recurring, skip

STEP 5: Validate consistency
  → stdDev of intervals < 0.3 * medianInterval → consistent
  → If inconsistent → skip (irregular spending, not recurring)

STEP 6: Compute average amount across group

STEP 7: Predict next occurrence
  → nextExpected = lastTransaction.date + medianInterval

STEP 8: Upsert RecurringTransaction record
  → If merchant already exists in recurring table → update fields
  → Else → insert new
```

### 6.3 Financial Health Score Calculation

```
INPUT: last 3 months of transactions + active budgets

COMPONENT 1: Savings Rate (40 points max)
  → savingsRate = (totalIncome - totalExpense) / totalIncome
  → score = savingsRate * 200        // 20% saves = 40 pts
  → clamp to [0, 40]

COMPONENT 2: Budget Adherence (30 points max)
  → For each active budget:
      ratio = spentAmount / limitAmount
      if ratio <= 1.0 → adherent
  → adherenceRate = adherentBudgets / totalBudgets
  → score = adherenceRate * 30

COMPONENT 3: Spending Trend (20 points max)
  → Compare expense of current month vs average of prior 2 months
  → trendRatio = currentMonthExpense / avgPriorExpense
  → if trendRatio < 0.9 → 20 pts (improving)
  → if trendRatio 0.9–1.1 → 15 pts (stable)
  → if trendRatio > 1.1 → 5 pts (declining)

COMPONENT 4: Consistency (10 points max)
  → How many days in the month had tracked transactions
  → if coverage > 80% → 10 pts
  → if coverage 50–80% → 6 pts
  → if coverage < 50% → 2 pts

TOTAL: sum of all components → 0–100
```

### 6.4 Category Auto-Assignment (Keyword Engine)

```kotlin
object CategoryKeywords {
    val map = mapOf(
        "Food & Dining" to listOf(
            "keells", "cargills", "arpico", "laugfs", "kfc",
            "mcdonalds", "pizza hut", "dominos", "uber eats",
            "pickme food", "foodpanda", "eliyada"
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
        ),
        "Transfer" to listOf(
            "fund transfer", "own account", "ceft", "slips"
        )
    )

    fun findCategory(merchantName: String?): String? {
        if (merchantName == null) return null
        val lower = merchantName.lowercase()
        return map.entries.firstOrNull { (_, keywords) ->
            keywords.any { lower.contains(it) }
        }?.key
    }
}
```

---

## 7. Material 3 Expressive Theme Setup

### Theme.kt

```kotlin
@Composable
fun SLMoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(
            primary = Color(0xFF66BB6A),        // Green accent
            secondary = Color(0xFF42A5F5),
            tertiary = Color(0xFFFFCA28),
            background = Color(0xFF0D1B0F),     // Deep dark green
            surface = Color(0xFF1A2C1E),
            onPrimary = Color.Black,
            onBackground = Color(0xFFE8F5E9)
        )
        else -> lightColorScheme(
            primary = Color(0xFF2E7D32),
            secondary = Color(0xFF1565C0),
            tertiary = Color(0xFFF9A825),
            background = Color(0xFFF1F8E9),
            surface = Color(0xFFFFFFFF)
        )
    }

    val typography = Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily(Font(R.font.outfit_bold)),
            fontSize = 57.sp, lineHeight = 64.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily(Font(R.font.outfit_semibold)),
            fontSize = 28.sp, lineHeight = 36.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 16.sp, lineHeight = 24.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 12.sp, lineHeight = 16.sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(28.dp),
            extraLarge = RoundedCornerShape(32.dp)
        ),
        content = content
    )
}
```

### Motion.kt — Spring Animation Specs

```kotlin
object SLMoneyMotion {
    // Standard enter/exit springs (M3 Expressive style)
    val SpringEnter = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,   // 0.5 — bouncy
        stiffness = Spring.StiffnessMedium              // 1500
    )
    val SpringExit = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
    )
    // FAB expansion spring
    val SpringFab = spring<Float>(
        dampingRatio = 0.6f,
        stiffness = 800f
    )
    // List item stagger delay
    const val StaggerDelayMs = 50
}
```

---

## 8. Dependencies

### gradle/libs.versions.toml

```toml
[versions]
kotlin = "2.1.0"
agp = "8.7.0"
compose-bom = "2025.01.01"
material3 = "1.4.0"
hilt = "2.53"
room = "2.7.0"
datastore = "1.1.2"
vico = "2.1.0"
navigation = "2.8.5"
lifecycle = "2.8.7"
workmanager = "2.10.0"

[libraries]
# Compose
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "material3" }
compose-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
compose-animation = { group = "androidx.compose.animation", name = "animation" }

# Navigation
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }

# Hilt DI
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# DataStore
datastore-prefs = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }

# Lifecycle
lifecycle-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-runtime = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }

# Charts
vico-compose = { group = "com.patrykandpatrick.vico", name = "compose-m3", version.ref = "vico" }

# WorkManager
workmanager = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "workmanager" }

# Security
biometric = { group = "androidx.biometric", name = "biometric", version = "1.2.0-alpha05" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "2.1.0-1.0.29" }
```

### AndroidManifest.xml — Key Permissions & Services

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- SMS (fallback approach) -->
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />

    <!-- Notification Listener (primary approach) -->
    <!-- No permission tag needed; user grants via Settings -->

    <application ...>
        <activity android:name=".MainActivity" ... />

        <!-- Notification Listener Service -->
        <service
            android:name=".service.TransactionNotificationListener"
            android:exported="true"
            android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
            <intent-filter>
                <action android:name="android.service.notification.NotificationListenerService" />
            </intent-filter>
        </service>

        <!-- SMS Receiver (fallback) -->
        <receiver
            android:name=".service.SmsReceiver"
            android:exported="true"
            android:permission="android.permission.BROADCAST_SMS">
            <intent-filter android:priority="999">
                <action android:name="android.provider.Telephony.SMS_RECEIVED" />
            </intent-filter>
        </receiver>
    </application>
</manifest>
```

---

## Appendix: Default Categories (Pre-seeded)

| ID | Name | Icon | Color |
|---|---|---|---|
| 1 | Food & Dining | `restaurant` | `#FF5722` |
| 2 | Transport | `directions_car` | `#2196F3` |
| 3 | Groceries | `shopping_cart` | `#4CAF50` |
| 4 | Utilities | `bolt` | `#FFC107` |
| 5 | Subscriptions | `subscriptions` | `#9C27B0` |
| 6 | Shopping | `shopping_bag` | `#E91E63` |
| 7 | Health | `local_hospital` | `#F44336` |
| 8 | Education | `school` | `#3F51B5` |
| 9 | Transfer | `swap_horiz` | `#607D8B` |
| 10 | Entertainment | `movie` | `#FF9800` |
| 11 | Salary | `payments` | `#00BCD4` |
| 12 | Freelance | `work` | `#8BC34A` |
| 13 | Other Income | `account_balance` | `#795548` |
| 14 | Uncategorized | `help_outline` | `#9E9E9E` |

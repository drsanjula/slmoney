package com.slmoney.app.ui.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val summary by viewModel.summary.collectAsState()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Insights") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            summary?.let { data ->
                item {
                    AnalyticsSummaryCard(
                        income = data.totalIncome,
                        expense = data.totalExpense,
                        savings = data.netSavings
                    )
                }

                item {
                    Text(
                        text = "Category Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(data.categoryBreakdown) { item ->
                    CategorySpentItem(
                        name = item.category.name,
                        amount = item.amount,
                        percentage = item.percentage,
                        color = Color.Green // Placeholder
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsSummaryCard(income: Double, expense: Double, savings: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Net Savings", style = MaterialTheme.typography.labelMedium)
            Text(
                "LKR ${"%,.2f".format(savings)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = if (savings >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Income", style = MaterialTheme.typography.labelSmall)
                    Text("LKR ${"%,.0f".format(income)}", fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Expenses", style = MaterialTheme.typography.labelSmall)
                    Text("LKR ${"%,.0f".format(expense)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CategorySpentItem(name: String, amount: Double, percentage: Float, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.bodyMedium)
            Text("LKR ${"%,.0f".format(amount)}", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxWidth().height(8.dp).padding(vertical = 2.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

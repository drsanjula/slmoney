package com.slmoney.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.slmoney.app.ui.components.TransactionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddClick: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val transactions by viewModel.recentTransactions.collectAsState()
    val summary by viewModel.spendingSummary.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("SL Money") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                com.slmoney.app.ui.components.AccountCarousel(
                    accounts = listOf(
                        com.slmoney.app.domain.model.Account(
                            bankName = "Commercial Bank",
                            bankCode = "COMBANK",
                            accountMask = "XXXX1234",
                            accountType = com.slmoney.app.domain.model.AccountType.SAVINGS,
                            currentBalance = 51250.40,
                            lastUpdated = null
                        ),
                        com.slmoney.app.domain.model.Account(
                            bankName = "BOC",
                            bankCode = "BOC",
                            accountMask = "XXXX5678",
                            accountType = com.slmoney.app.domain.model.AccountType.SAVINGS,
                            currentBalance = 15500.20,
                            lastUpdated = null
                        )
                    )
                )
            }

            item {
                SpendingSummaryCard(
                    income = summary.income,
                    expense = summary.expense,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "No transactions detected yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(transactions) { transaction ->
                    TransactionCard(transaction = transaction)
                }
            }
        }
    }
}

@Composable
fun SpendingSummaryCard(
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total Spent",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "Rs. ${"%,.2f".format(expense)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f))
            )

            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    text = "Total Income",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "Rs. ${"%,.2f".format(income)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                )
            }
        }
    }
}

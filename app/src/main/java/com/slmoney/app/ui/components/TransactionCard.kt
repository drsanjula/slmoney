package com.slmoney.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slmoney.app.domain.model.Transaction
import com.slmoney.app.domain.model.TransactionType
import java.time.format.DateTimeFormatter

@Composable
fun TransactionCard(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = when (transaction.type) {
                    TransactionType.CREDIT -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                    TransactionType.DEBIT -> Color(0xFFF44336).copy(alpha = 0.2f)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (transaction.type) {
                            TransactionType.CREDIT -> Icons.Default.ArrowUpward
                            TransactionType.DEBIT -> Icons.Default.ArrowDownward
                        },
                        contentDescription = null,
                        tint = when (transaction.type) {
                            TransactionType.CREDIT -> Color(0xFF4CAF50)
                            TransactionType.DEBIT -> Color(0xFFF44336)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.merchantName ?: "Unknown Merchant",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = transaction.date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount
            Text(
                text = "${if (transaction.type == TransactionType.CREDIT) "+" else "-"}Rs. ${"%,.2f".format(transaction.amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = when (transaction.type) {
                    TransactionType.CREDIT -> Color(0xFF4CAF50)
                    TransactionType.DEBIT -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

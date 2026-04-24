package com.norton.scamdetector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.data.local.ScanHistoryEntity
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun ScanHistoryList(
    items: List<ScanHistoryEntity>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No scans yet — analyze a message to see your history.",
                color = NortonColors.TextSecondary,
                fontSize = 13.sp
            )
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                ScanHistoryItem(item = item)
            }
        }
    }
}

@Composable
private fun ScanHistoryItem(item: ScanHistoryEntity) {
    val dotColor = when (item.riskLevel) {
        "DANGEROUS" -> NortonColors.DangerousRed
        "SUSPICIOUS" -> NortonColors.SuspiciousOrange
        else -> Color(0xFF4CAF50)
    }
    val riskLabel = item.riskLevel.lowercase().replaceFirstChar { it.uppercase() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.Surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.messagePreview,
                    color = NortonColors.TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = riskLabel,
                    color = dotColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = relativeTimeFrom(item.scannedAt),
                color = NortonColors.TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

private fun relativeTimeFrom(timestampMillis: Long): String {
    val diffMs = System.currentTimeMillis() - timestampMillis
    val diffMin = diffMs / 60_000L
    val diffHour = diffMin / 60L
    val diffDay = diffHour / 24L
    return when {
        diffMin < 1 -> "Just now"
        diffMin < 60 -> "$diffMin min ago"
        diffHour < 24 -> "$diffHour hour${if (diffHour > 1L) "s" else ""} ago"
        diffDay == 1L -> "Yesterday"
        else -> "$diffDay days ago"
    }
}

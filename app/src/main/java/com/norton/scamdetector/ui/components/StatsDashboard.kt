package com.norton.scamdetector.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun StatsDashboard(
    totalScans: Int,
    dangerousCount: Int,
    safeCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            count = totalScans,
            label = "Total Scans",
            icon = Icons.Filled.Search,
            iconTint = NortonColors.PrimaryYellow,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = dangerousCount,
            label = "Dangerous",
            icon = Icons.Filled.GppBad,
            iconTint = NortonColors.DangerousRed,
            countColor = NortonColors.DangerousRed,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = safeCount,
            label = "Safe",
            icon = Icons.Filled.CheckCircle,
            iconTint = Color(0xFF4CAF50),
            countColor = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    count: Int,
    label: String,
    icon: ImageVector,
    iconTint: Color,
    countColor: Color = NortonColors.TextPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = count.toString(),
                color = countColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = NortonColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

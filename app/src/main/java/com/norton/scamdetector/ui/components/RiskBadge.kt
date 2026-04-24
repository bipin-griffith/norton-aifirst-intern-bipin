package com.norton.scamdetector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.data.model.RiskLevel
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun RiskBadge(riskLevel: RiskLevel) {
    val color = riskLevel.toNortonColor()
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = riskLevel.emoji, fontSize = 16.sp)
        Text(
            text = riskLevel.displayLabel,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

fun RiskLevel.toNortonColor(): Color = when (this) {
    RiskLevel.SAFE -> NortonColors.SafeGreen
    RiskLevel.SUSPICIOUS -> NortonColors.SuspiciousOrange
    RiskLevel.DANGEROUS -> NortonColors.DangerousRed
}

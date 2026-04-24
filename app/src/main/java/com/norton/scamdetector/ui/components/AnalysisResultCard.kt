package com.norton.scamdetector.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun AnalysisResultCard(result: ScamAnalysisResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RiskBadge(riskLevel = result.riskLevel)

            ConfidenceMeter(
                confidenceScore = result.confidenceScore,
                riskLevel = result.riskLevel
            )

            HorizontalDivider(
                color = NortonColors.TextSecondary.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Why this was flagged:",
                    color = NortonColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = result.explanation,
                    color = NortonColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            if (result.redFlags.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Red flags detected:",
                        color = NortonColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    result.redFlags.forEach { flag ->
                        Text(
                            text = "⚠️  $flag",
                            color = NortonColors.TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

package com.norton.scamdetector.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.data.model.ExampleMessage
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun ExampleMessageChip(example: ExampleMessage, onClick: (String) -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .border(BorderStroke(1.dp, NortonColors.SuspiciousOrange), shape)
            .clickable { onClick(example.messageText) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Example scam message",
            tint = NortonColors.SuspiciousOrange,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = example.title,
            color = NortonColors.TextPrimary,
            fontSize = 13.sp
        )
    }
}

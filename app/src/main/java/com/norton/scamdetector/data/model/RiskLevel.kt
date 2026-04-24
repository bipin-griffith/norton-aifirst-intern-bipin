package com.norton.scamdetector.data.model

enum class RiskLevel(
    val displayLabel: String,
    val colorHex: String,
    val emoji: String
) {
    SAFE(
        displayLabel = "Safe",
        colorHex = "#2E7D32",
        emoji = "✅"
    ),
    SUSPICIOUS(
        displayLabel = "Suspicious",
        colorHex = "#E65100",
        emoji = "⚠️"
    ),
    DANGEROUS(
        displayLabel = "Dangerous",
        colorHex = "#B71C1C",
        emoji = "🚨"
    )
}

// AI-GENERATED: Written with Claude, reviewed and refined by [Your Name]
package com.norton.scamdetector

import com.google.common.truth.Truth.assertThat
import com.norton.scamdetector.data.model.RiskLevel
import com.norton.scamdetector.data.model.ScamAnalysisResult
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ScamAnalysisResultTest {

    @Test
    fun `valid confidence score is stored correctly`() {
        val result = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = 85,
            explanation = "No red flags detected.",
            redFlags = emptyList()
        )

        assertThat(result.confidenceScore).isEqualTo(85)
    }

    @Test
    fun `confidence score above 100 is clamped to 100 by coerceIn`() {
        val result = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SUSPICIOUS,
            confidenceScore = 150,
            explanation = "Suspicious indicators detected.",
            redFlags = listOf("Suspicious URL")
        )

        assertThat(result.confidenceScore).isEqualTo(100)
    }

    @Test
    fun `confidence score below 0 is clamped to 0 by coerceIn`() {
        val result = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = -10,
            explanation = "No flags.",
            redFlags = emptyList()
        )

        assertThat(result.confidenceScore).isEqualTo(0)
    }

    @Test
    fun `isHighRisk returns true for DANGEROUS and false for SAFE and SUSPICIOUS`() {
        val dangerous = ScamAnalysisResult.create(
            riskLevel = RiskLevel.DANGEROUS,
            confidenceScore = 92,
            explanation = "Multiple scam signals detected.",
            redFlags = listOf("Suspicious URL", "Urgency language")
        )
        val safe = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = 90,
            explanation = "No red flags detected.",
            redFlags = emptyList()
        )
        val suspicious = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SUSPICIOUS,
            confidenceScore = 60,
            explanation = "Some indicators detected.",
            redFlags = listOf("Urgency language")
        )

        assertThat(dangerous.isHighRisk).isTrue()
        assertThat(safe.isHighRisk).isFalse()
        assertThat(suspicious.isHighRisk).isFalse()
    }
}

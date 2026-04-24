package com.norton.scamdetector.data.model

data class ScamAnalysisResult(
    val riskLevel: RiskLevel,
    val confidenceScore: Int,
    val explanation: String,
    val redFlags: List<String>
) {
    val isHighRisk: Boolean
        get() = riskLevel == RiskLevel.DANGEROUS

    init {
        require(confidenceScore == confidenceScore.coerceIn(0, 100)) {
            "confidenceScore must be between 0 and 100"
        }
    }

    companion object {
        fun create(
            riskLevel: RiskLevel,
            confidenceScore: Int,
            explanation: String,
            redFlags: List<String>
        ): ScamAnalysisResult = ScamAnalysisResult(
            riskLevel = riskLevel,
            confidenceScore = confidenceScore.coerceIn(0, 100),
            explanation = explanation,
            redFlags = redFlags
        )
    }
}

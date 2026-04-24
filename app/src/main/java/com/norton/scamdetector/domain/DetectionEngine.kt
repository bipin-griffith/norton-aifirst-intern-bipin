package com.norton.scamdetector.domain

import com.norton.scamdetector.data.model.RiskLevel

data class DetectionScore(
    val keywordScore: Float,
    val urlScore: Float,
    val patternScore: Float,
    val combinedScore: Float,
    val riskLevel: RiskLevel,
    val detectedFlags: List<String>
)

class DetectionEngine {

    private val financialFraudKeywords = listOf(
        "wire transfer", "western union", "gift card", "bitcoin", "crypto payment",
        "bank transfer", "routing number", "wire funds"
    )

    private val impersonationKeywords = listOf(
        "irs", "fbi", "social security", "medicare", "apple support",
        "google security", "microsoft", "amazon security team"
    )

    private val urgencyManipulation = listOf(
        "final notice", "last warning", "account will be closed",
        "legal action", "warrant", "arrest"
    )

    private val ipUrlPattern = Regex("""\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b""")
    private val shortUrlPattern = Regex("""(bit\.ly|tinyurl|t\.co|goo\.gl)/""", RegexOption.IGNORE_CASE)
    private val suspiciousTldPattern = Regex("""\.(xyz|top|click|loan|work)(\b|/)""", RegexOption.IGNORE_CASE)
    private val prizePattern = Regex(
        """congratulations|you('ve| have) won|free\s+(gift|prize|reward)|selected.*winner""",
        RegexOption.IGNORE_CASE
    )
    private val personalInfoPattern = Regex(
        """social\s+security|credit\s+card|password|bank\s+account|ssn|cvv|pin\s+number""",
        RegexOption.IGNORE_CASE
    )
    private val dollarPattern = Regex("""\$[\d,]+""")
    private val phonePattern = Regex("""\b\d{3}[-.]?\d{3}[-.]?\d{4}\b""")

    fun analyze(message: String): DetectionScore {
        val lower = message.lowercase()
        val flags = mutableListOf<String>()

        val keywordScore = computeKeywordScore(lower, flags)
        val urlScore = computeUrlScore(message, flags)
        val patternScore = computePatternScore(message, lower, flags)

        val combined = (keywordScore * 0.40f + urlScore * 0.35f + patternScore * 0.25f).coerceIn(0f, 1f)

        val riskLevel = when {
            combined < 0.3f -> RiskLevel.SAFE
            combined <= 0.6f -> RiskLevel.SUSPICIOUS
            else -> RiskLevel.DANGEROUS
        }

        return DetectionScore(keywordScore, urlScore, patternScore, combined, riskLevel, flags)
    }

    private fun computeKeywordScore(lower: String, flags: MutableList<String>): Float {
        var hits = 0

        val financialHits = financialFraudKeywords.count { lower.contains(it) }
        if (financialHits > 0) {
            hits += financialHits
            flags.add("Financial fraud language detected ($financialHits indicator${if (financialHits > 1) "s" else ""})")
        }

        val impersonationHits = impersonationKeywords.count { lower.contains(it) }
        if (impersonationHits > 0) {
            hits += impersonationHits
            flags.add("Authority impersonation detected")
        }

        val urgencyHits = urgencyManipulation.count { lower.contains(it) }
        if (urgencyHits > 0) {
            hits += urgencyHits
            flags.add("Urgency manipulation tactics detected")
        }

        // 5+ total keyword hits saturates to 1.0
        return (hits.toFloat() / 5f).coerceIn(0f, 1f)
    }

    private fun computeUrlScore(message: String, flags: MutableList<String>): Float {
        var score = 0f

        if (ipUrlPattern.containsMatchIn(message)) {
            score += 0.6f
            flags.add("IP-based URL detected (masks real destination)")
        }
        if (shortUrlPattern.containsMatchIn(message)) {
            score += 0.4f
            flags.add("URL shortener used to hide destination")
        }
        if (suspiciousTldPattern.containsMatchIn(message)) {
            score += 0.4f
            flags.add("Suspicious domain extension (.xyz / .top / .click / .loan / .work)")
        }

        return score.coerceIn(0f, 1f)
    }

    private fun computePatternScore(message: String, lower: String, flags: MutableList<String>): Float {
        var score = 0f

        if (prizePattern.containsMatchIn(lower)) {
            score += 0.4f
            flags.add("Prize or reward language common in scams")
        }
        if (personalInfoPattern.containsMatchIn(lower)) {
            score += 0.4f
            flags.add("Requests sensitive personal or financial information")
        }
        if (dollarPattern.containsMatchIn(message)) {
            score += 0.1f
            flags.add("Suspicious dollar amount referenced")
        }
        if (phonePattern.containsMatchIn(message)) {
            score += 0.1f
            flags.add("Phone number present — verify before calling")
        }

        return score.coerceIn(0f, 1f)
    }
}

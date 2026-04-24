package com.norton.scamdetector.domain

class LocalMLClassifier {

    private val dollarPattern = Regex("""\$[\d,]+""")
    private val phonePattern = Regex("""\b\d{3}[-.]?\d{3}[-.]?\d{4}\b""")
    private val urlPattern = Regex("""https?://\S+|www\.\S+""", RegexOption.IGNORE_CASE)

    fun score(message: String): Float {
        val features = extractFeatures(message)
        return normalizeFeatures(features)
    }

    private data class MessageFeatures(
        val length: Int,
        val exclamationCount: Int,
        val allCapsWordCount: Int,
        val urlCount: Int,
        val dollarAmountCount: Int,
        val phoneNumberCount: Int
    )

    private fun extractFeatures(message: String): MessageFeatures {
        val words = message.split(Regex("\\s+")).filter { it.isNotBlank() }
        return MessageFeatures(
            length = message.length,
            exclamationCount = message.count { it == '!' },
            allCapsWordCount = words.count { w -> w.length > 2 && w == w.uppercase() && w.all { it.isLetter() } },
            urlCount = urlPattern.findAll(message).count(),
            dollarAmountCount = dollarPattern.findAll(message).count(),
            phoneNumberCount = phonePattern.findAll(message).count()
        )
    }

    private fun normalizeFeatures(f: MessageFeatures): Float {
        val lengthScore = when {
            f.length > 500 -> 0.8f
            f.length > 200 -> 0.5f
            f.length > 100 -> 0.3f
            else -> 0f
        }
        val exclamationScore = (f.exclamationCount / 5f).coerceIn(0f, 1f)
        val capsScore = (f.allCapsWordCount / 4f).coerceIn(0f, 1f)
        val urlScore = (f.urlCount / 3f).coerceIn(0f, 1f)
        val dollarScore = if (f.dollarAmountCount > 0) 0.6f else 0f
        val phoneScore = if (f.phoneNumberCount > 0) 0.4f else 0f

        return ((lengthScore + exclamationScore + capsScore + urlScore + dollarScore + phoneScore) / 6f)
            .coerceIn(0f, 1f)
    }
}

package com.norton.scamdetector.data.repository

import com.norton.scamdetector.data.local.ScanHistoryDao
import com.norton.scamdetector.data.local.ScanHistoryEntity
import com.norton.scamdetector.data.model.RiskLevel
import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.domain.DetectionEngine
import com.norton.scamdetector.domain.LocalMLClassifier
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScamAnalysisRepositoryImpl(
    val dao: ScanHistoryDao
) : ScamAnalysisRepository {

    private val detectionEngine = DetectionEngine()
    private val mlClassifier = LocalMLClassifier()

    override suspend fun analyzeMessage(message: String): Result<ScamAnalysisResult> {
        delay(1500)

        return runCatching {
            val engineResult = detectionEngine.analyze(message)
            val mlScore = mlClassifier.score(message)

            val finalScore = (engineResult.combinedScore * 0.60f + mlScore * 0.40f).coerceIn(0f, 1f)

            val riskLevel = when {
                finalScore < 0.3f -> RiskLevel.SAFE
                finalScore <= 0.6f -> RiskLevel.SUSPICIOUS
                else -> RiskLevel.DANGEROUS
            }

            val confidenceScore = when (riskLevel) {
                RiskLevel.SAFE -> ((1f - finalScore) * 100f).toInt().coerceIn(70, 95)
                RiskLevel.SUSPICIOUS -> (finalScore * 100f).toInt().coerceIn(55, 80)
                RiskLevel.DANGEROUS -> (finalScore * 100f).toInt().coerceIn(80, 98)
            }

            val flagCount = engineResult.detectedFlags.size
            val explanation = when (riskLevel) {
                RiskLevel.SAFE ->
                    "No significant scam indicators were detected. This message appears legitimate, though always exercise caution with unexpected messages."
                RiskLevel.SUSPICIOUS ->
                    "This message contains $flagCount suspicious indicator${if (flagCount != 1) "s" else ""}. Treat with caution — do not click links or share personal information."
                RiskLevel.DANGEROUS ->
                    "This message exhibits multiple strong hallmarks of a scam. Do not interact, click any links, or respond with personal information. Delete it immediately."
            }

            val result = ScamAnalysisResult.create(
                riskLevel = riskLevel,
                confidenceScore = confidenceScore,
                explanation = explanation,
                redFlags = engineResult.detectedFlags
            )

            dao.insertScan(
                ScanHistoryEntity(
                    messageText = message,
                    messagePreview = message.take(50),
                    riskLevel = riskLevel.name,
                    confidenceScore = confidenceScore,
                    explanation = explanation,
                    redFlags = Json.encodeToString(engineResult.detectedFlags),
                    scannedAt = System.currentTimeMillis()
                )
            )

            result
        }
    }
}

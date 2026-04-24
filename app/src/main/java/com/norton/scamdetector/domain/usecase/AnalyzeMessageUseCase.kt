package com.norton.scamdetector.domain.usecase

import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.data.repository.ScamAnalysisRepository

class AnalyzeMessageUseCase(
    private val repository: ScamAnalysisRepository
) {
    suspend operator fun invoke(message: String): Result<ScamAnalysisResult> {
        if (message.trim().length < 10) {
            return Result.failure(
                IllegalArgumentException("Message is too short to analyze")
            )
        }
        return repository.analyzeMessage(message)
    }
}

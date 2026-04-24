package com.norton.scamdetector.data.repository

import com.norton.scamdetector.data.model.ScamAnalysisResult

interface ScamAnalysisRepository {
    suspend fun analyzeMessage(message: String): Result<ScamAnalysisResult>
}

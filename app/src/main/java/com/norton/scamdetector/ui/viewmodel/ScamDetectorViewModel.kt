package com.norton.scamdetector.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.norton.scamdetector.data.local.ScanHistoryDao
import com.norton.scamdetector.data.local.ScanHistoryEntity
import com.norton.scamdetector.data.local.ScamDatabase
import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.data.repository.ScamAnalysisRepositoryImpl
import com.norton.scamdetector.domain.usecase.AnalyzeMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScamDetectorUiState {
    object Idle : ScamDetectorUiState()
    object Loading : ScamDetectorUiState()
    data class Success(val result: ScamAnalysisResult) : ScamDetectorUiState()
    data class Error(val message: String) : ScamDetectorUiState()
}

class ScamDetectorViewModel(
    private val analyzeMessageUseCase: AnalyzeMessageUseCase,
    private val dao: ScanHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScamDetectorUiState>(ScamDetectorUiState.Idle)
    val uiState: StateFlow<ScamDetectorUiState> = _uiState.asStateFlow()

    val currentInput = MutableStateFlow("")

    val totalScans: StateFlow<Int> = dao.getTotalScanCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dangerousCount: StateFlow<Int> = dao.getDangerousCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentHistory: StateFlow<List<ScanHistoryEntity>> = dao.getRecentScans(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateInput(text: String) {
        currentInput.value = text
    }

    fun analyzeMessage() {
        viewModelScope.launch {
            _uiState.value = ScamDetectorUiState.Loading
            analyzeMessageUseCase(currentInput.value)
                .onSuccess { result ->
                    _uiState.value = ScamDetectorUiState.Success(result)
                }
                .onFailure { error ->
                    _uiState.value = ScamDetectorUiState.Error(
                        error.message ?: "An unexpected error occurred"
                    )
                }
        }
    }

    fun clearResult() {
        _uiState.value = ScamDetectorUiState.Idle
    }

    fun clearHistory() {
        viewModelScope.launch { dao.deleteAll() }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = ScamDatabase.getInstance(context)
                    val dao = db.scanHistoryDao()
                    val repository = ScamAnalysisRepositoryImpl(dao)
                    val useCase = AnalyzeMessageUseCase(repository)
                    return ScamDetectorViewModel(useCase, dao) as T
                }
            }
    }
}

// AI-GENERATED: Written with Claude, reviewed and refined by [Your Name]
package com.norton.scamdetector

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.norton.scamdetector.data.model.RiskLevel
import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.domain.usecase.AnalyzeMessageUseCase
import com.norton.scamdetector.ui.viewmodel.ScamDetectorUiState
import com.norton.scamdetector.ui.viewmodel.ScamDetectorViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScamDetectorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var analyzeMessageUseCase: AnalyzeMessageUseCase
    private lateinit var viewModel: ScamDetectorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        analyzeMessageUseCase = mockk()
        viewModel = ScamDetectorViewModel(analyzeMessageUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        assertThat(viewModel.uiState.value).isEqualTo(ScamDetectorUiState.Idle)
    }

    @Test
    fun `analyzeMessage transitions through Loading then Success`() = runTest {
        val fakeResult = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = 90,
            explanation = "No red flags detected.",
            redFlags = emptyList()
        )
        coEvery { analyzeMessageUseCase(any()) } returns Result.success(fakeResult)
        viewModel.updateInput("Hello, this is a normal message to analyze for scams.")

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ScamDetectorUiState.Idle)

            viewModel.analyzeMessage()

            assertThat(awaitItem()).isEqualTo(ScamDetectorUiState.Loading)
            assertThat(awaitItem()).isEqualTo(ScamDetectorUiState.Success(fakeResult))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearResult resets state to Idle after Success`() = runTest {
        val fakeResult = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = 90,
            explanation = "No red flags detected.",
            redFlags = emptyList()
        )
        coEvery { analyzeMessageUseCase(any()) } returns Result.success(fakeResult)
        viewModel.updateInput("Hello, this is a normal message to analyze for scams.")
        viewModel.analyzeMessage()

        assertThat(viewModel.uiState.value).isEqualTo(ScamDetectorUiState.Success(fakeResult))

        viewModel.clearResult()

        assertThat(viewModel.uiState.value).isEqualTo(ScamDetectorUiState.Idle)
    }

    @Test
    fun `use case failure transitions state to Error with non-empty message`() = runTest {
        coEvery { analyzeMessageUseCase(any()) } returns Result.failure(RuntimeException("Network error"))
        viewModel.updateInput("Hello, this is a normal message to analyze for scams.")

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ScamDetectorUiState.Idle)

            viewModel.analyzeMessage()

            assertThat(awaitItem()).isEqualTo(ScamDetectorUiState.Loading)
            val errorState = awaitItem() as ScamDetectorUiState.Error
            assertThat(errorState.message).isNotEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
}

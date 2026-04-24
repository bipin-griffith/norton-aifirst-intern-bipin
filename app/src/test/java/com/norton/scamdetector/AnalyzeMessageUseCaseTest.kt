// AI-GENERATED: Written with Claude, reviewed and refined by [Your Name]
package com.norton.scamdetector

// Note: MockK is used instead of MockitoJUnitRunner because ScamAnalysisRepository
// declares a suspend function; MockK handles Kotlin coroutine stubs natively without
// the runBlocking/continuation workarounds that Mockito requires for suspend interfaces.
import com.google.common.truth.Truth.assertThat
import com.norton.scamdetector.data.model.RiskLevel
import com.norton.scamdetector.data.model.ScamAnalysisResult
import com.norton.scamdetector.data.repository.ScamAnalysisRepository
import com.norton.scamdetector.domain.usecase.AnalyzeMessageUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AnalyzeMessageUseCaseTest {

    @MockK
    private lateinit var repository: ScamAnalysisRepository

    private lateinit var useCase: AnalyzeMessageUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = AnalyzeMessageUseCase(repository)
    }

    @Test
    fun `valid message delegates to repository and returns its result`() = runTest {
        val fakeResult = ScamAnalysisResult.create(
            riskLevel = RiskLevel.SAFE,
            confidenceScore = 90,
            explanation = "No red flags detected.",
            redFlags = emptyList()
        )
        val message = "Hello, this is a perfectly normal message from your bank."
        coEvery { repository.analyzeMessage(message) } returns Result.success(fakeResult)

        val result = useCase(message)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(fakeResult)
        coVerify(exactly = 1) { repository.analyzeMessage(message) }
    }

    @Test
    fun `blank string returns failure without calling repository`() = runTest {
        val result = useCase("")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        verify { repository wasNot io.mockk.Called }
    }

    @Test
    fun `whitespace-only string returns failure without calling repository`() = runTest {
        val result = useCase("   ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        verify { repository wasNot io.mockk.Called }
    }

    @Test
    fun `repository network failure is propagated as Result failure`() = runTest {
        val networkException = RuntimeException("Connection timed out")
        val message = "Your account has been suspended. Click here to verify now."
        coEvery { repository.analyzeMessage(message) } returns Result.failure(networkException)

        val result = useCase(message)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Connection timed out")
    }

    @Test
    fun `use case passes exact message string to repository unchanged`() = runTest {
        val exactMessage = "Congratulations! You have won a gift card. Claim now."
        val fakeResult = ScamAnalysisResult.create(
            riskLevel = RiskLevel.DANGEROUS,
            confidenceScore = 92,
            explanation = "Multiple scam signals.",
            redFlags = listOf("Prize language", "Urgency")
        )
        coEvery { repository.analyzeMessage(exactMessage) } returns Result.success(fakeResult)

        useCase(exactMessage)

        coVerify(exactly = 1) { repository.analyzeMessage(exactMessage) }
    }
}

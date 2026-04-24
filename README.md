---

## ⚙️ Setup Instructions

### Prerequisites
- Android Studio Hedgehog 2023.1.1 or newer
- Android SDK 26 minimum, target SDK 36
- Kotlin 2.0.21
- Physical device or emulator with API 26+

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/bipin-griffith/norton-aifirst-intern-bipin.git
cd norton-aifirst-intern-bipin
```

**2. Open in Android Studio**
- File → Open → select the project folder
- Wait for Gradle sync to complete

**3. Run the app**
- Select an emulator or physical device running API 26+
- Press Run ▶

**4. SMS Permission**
- On first launch of the Scam Detector tab, tap "Grant Permission"
- Accept the READ_SMS permission dialog
- Your last 5 inbox messages will appear for scanning

**5. Run unit tests**
```bash
./gradlew test
```

---

## 📸 Screenshots

| Home Screen | Scam Detector | Result — Dangerous |
|---|---|---|
| ![Home](screenshots/home.png) | ![Detector](screenshots/detector.png) | ![Dangerous](screenshots/dangerous.png) |

| Result — Safe | Profile Screen | Scan History |
|---|---|---|
| ![Safe](screenshots/safe.png) | ![Profile](screenshots/profile.png) | ![History](screenshots/history.png) |

---

## 🤖 AI Interaction Log

### Prompt 1 — Architecture & Data Models

**Prompt given to Claude:**
> "I am building an Android app called Scam Message Detector using Kotlin and Jetpack Compose inspired by Norton Genie. Please build the complete data layer and ViewModel following MVVM + Clean Architecture. Create RiskLevel enum with color hex values, ScamAnalysisResult data class with coerceIn for confidence score, ExampleMessage with companion object, ScamAnalysisRepository interface, UseCase with blank input validation, and ViewModel with sealed UiState using StateFlow throughout. No LiveData anywhere."

**What Claude produced:** Complete architecture with all data models, repository, use case, and ViewModel with sealed UiState.

**How I refined it:** Claude initially used LiveData in the ViewModel. I caught this and followed up: *"Convert all LiveData to StateFlow and use collectAsStateWithLifecycle in the Compose UI."* I also added the color mapping function to RiskLevel myself since Claude omitted UI-specific concerns from the data layer.

**Verdict:** Accepted with modifications.

---

### Prompt 2 — Jetpack Compose UI

**Prompt given to Claude:**
> "Build the full Jetpack Compose UI for the ScamDetectorScreen inspired by the Norton 360 app. Use dark navy background #0A1628, Norton yellow #FFD700 for the primary button, card-based layout. Include: header with shield icon, multiline text input, two tappable example chips, yellow Analyze button with loading spinner, result card with AnimatedVisibility, animated confidence bar using animateFloatAsState with FastOutSlowInEasing, and error state with Try Again button. Add contentDescription to all icons."

**What Claude produced:** Full screen composable with all required elements and animations.

**How I refined it:** Claude's example chips were plain TextButtons. I redesigned them as SuggestionChip components with warning icons. I also extracted all colors to NortonTheme.kt instead of hardcoded values inline, and changed animation easing from linear to FastOutSlowInEasing for a polished feel.

**Verdict:** Accepted with styling refinements.

---

### Prompt 3 — Three-Layer Detection Pipeline

**Prompt given to Claude:**
> "Upgrade ScamAnalysisRepositoryImpl to use three detection layers. Layer 1: expanded keyword matching with financial fraud, impersonation, urgency categories. Layer 2: DetectionEngine scoring keywordScore, urlScore detecting IP URLs and shorteners, patternScore — combined 40/35/25 weighting. Layer 3: LocalMLClassifier extracting features like exclamation count, all-caps words, dollar amounts, phone numbers — returning normalized 0-1 score. Final result: 60% DetectionEngine, 40% LocalML."

**What Claude produced:** Complete three-layer system with DetectionEngine and LocalMLClassifier as separate classes.

**How I refined it:** The initial URL regex missed some edge cases for IP-based URLs. I tested it manually with sample phishing URLs and tightened the regex pattern. I also restructured the weighting logic so SAFE confidence reads as `(1 - combinedScore) * 100` rather than the raw score, which Claude initially got backwards.

**Verdict:** Accepted with logic corrections.

---

### Prompt 4 — Room Database + Stats

**Prompt given to Claude:**
> "Add Room database support. Create ScanHistoryEntity with id autoGenerate, messageText, messagePreview first 50 chars, riskLevel, confidenceScore, explanation, redFlags as JSON string, scannedAt timestamp. Create DAO with Flow queries for getAllScans, getRecentScans, getTotalScanCount, getDangerousCount, deleteAll. Add totalScans, dangerousCount, recentHistory StateFlows to ViewModel using stateIn WhileSubscribed 5000."

**What Claude produced:** Complete Room setup with database, DAO, entity, and updated ViewModel StateFlows.

**How I refined it:** Claude's `stateIn` used `SharingStarted.Eagerly` which keeps the Flow active even when the app is backgrounded — wasteful on battery. I changed all three to `SharingStarted.WhileSubscribed(5000)`. I also added the automatic save-after-analysis call in the repository which Claude forgot to wire up.

**Verdict:** Accepted with battery optimization fix.

---

### Prompt 5 — AI Code Review

**Prompt given to Claude:**
> "Review ScamDetectorViewModel.kt and ScamDetectorScreen.kt for: memory leaks, Compose recomposition issues from unstable lambdas, error handling gaps, accessibility issues, anything that would fail a production code review at a mobile-first company like Norton."

**What Claude flagged:**
1. ⚠️ **CRITICAL** — Inline lambdas in Compose causing unnecessary recompositions
2. ⚠️ **CRITICAL** — Missing contentDescription on shield icon and risk badge
3. ⚠️ **WARNING** — Analyze button not disabled during loading allowing spam taps
4. ⚠️ **WARNING** — API key hardcoded in repository class
5. ✅ **PASS** — StateFlow + collectAsStateWithLifecycle correctly implemented

**What I changed:** Fixed all four issues — extracted lambdas, added all contentDescriptions, disabled button during Loading state, moved API key to BuildConfig from local.properties.

---

## 🧪 Testing

| Test File | Tests | AI Generated |
|---|---|---|
| `ScamAnalysisResultTest.kt` | 4 tests — data model validity, confidence clamping | Partially |
| `AnalyzeMessageUseCaseTest.kt` | 5 tests — happy path, blank input, whitespace, network error | Yes — reviewed |
| `ScamDetectorViewModelTest.kt` | 4 tests — state transitions, Flow emissions, clear action | Yes — reviewed |

Run all tests:
```bash
./gradlew test
```

**AI-generated tests** are marked with `// AI-GENERATED: Reviewed and refined by Bipin Gupta` in the file header.

**Manually added edge cases:**
- Whitespace-only input (`"   "`) returning failure without calling repository
- TestCoroutineScope cancellation in `@After` to prevent test leaks

---

## 📦 Dependencies

```kotlin
// Compose + Material3
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.material3)
implementation(libs.androidx.material.icons.extended)

// Lifecycle + ViewModel
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.androidx.lifecycle.runtime.compose)

// Room Database
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
kapt(libs.androidx.room.compiler)

// Serialization
implementation(libs.kotlinx.serialization.json)

// Testing
testImplementation(libs.truth)
testImplementation(libs.kotlinx.coroutines.test)
testImplementation(libs.turbine)
testImplementation(libs.mockk)
```

---

## 🎥 Demo Video

**[Add your YouTube / Loom link here]**

The 5-minute video covers:
1. App demo — Home, Scam Detector with SMS scanning, Profile
2. Code walkthrough — Architecture, DetectionEngine, ViewModel, Room
3. AI workflow — Actual Claude conversations, prompt refinements, code review

---

## 💭 Reflection

**What I learned:**

Working AI-first changed how I think about prompts. A vague prompt produces generic code. A prompt that specifies the exact data model, error handling strategy, state management pattern, and UI behavior produces code that is production-ready with minimal changes. Prompt quality is architecture.

I also learned that the fastest AI workflow is: AI drafts the skeleton → I review critically → AI refines based on specific feedback → I handle the final 10% that requires judgment AI doesn't have. The whitespace edge case in the tests, the battery-draining `SharingStarted.Eagerly`, the backwards confidence score for SAFE — these are all things I caught that Claude missed.

Studying the Norton 360 app directly was invaluable. Understanding how they handle the protection status banner, the circular threat report, and the Safe SMS permission flow directly shaped my implementation decisions.

**What I would do differently:**

- Start with a `NortonTheme.kt` design system file before writing any screens
- Use Paparazzi screenshot tests alongside unit tests for Compose UI regression testing
- Explore Claude's structured output mode for the analysis API call instead of prompt-engineering JSON format — more robust and easier to parse

---

## 👨‍💻 Author

**Bipin Gupta**
- GitHub: [@bipin-griffith](https://github.com/bipin-griffith)
- Email: bgupta9861@gmail.com

---

*Submitted for Gen Digital Norton Mobile Engineering AI-First Intern Assignment*
*Built with ❤️ and Claude AI*

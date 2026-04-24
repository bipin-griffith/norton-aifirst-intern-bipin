# 🛡️ Norton Scam Detector
### Gen Digital · Norton Mobile Engineering · AI-First Intern Assignment (Option B)

> Built with Kotlin + Jetpack Compose · Inspired by Norton Genie · AI-First Development using Claude

---

## 📱 Project Overview

This is a **Scam Message Detector** prototype built for the Gen Digital Norton Mobile Engineering AI-First Internship assignment — **Option B**. The app is directly inspired by **Norton Genie**, Norton 360's AI-powered scam detection assistant.

Users can:
- Paste or type any suspicious SMS, email snippet, or URL
- Scan real SMS messages directly from their device inbox
- Get a risk assessment — Safe / Suspicious / Dangerous — with confidence score and explanation
- View persistent scan history and live statistics

**Platform:** Android — Kotlin + Jetpack Compose  
**AI Tool Used:** Claude (Anthropic) via Claude Code  
**Architecture:** MVVM + Clean Architecture + Room Database

---

## ✨ Key Features

- 🔍 Three-Layer Detection Engine — keyword matching, URL pattern scoring, local ML classifier
- 📱 Real SMS Inbox Scanning — reads device messages with permission, inspired by Norton Safe SMS
- 📊 Live Stats Dashboard — total scans, dangerous caught, safe messages
- 🕐 Persistent Scan History — Room database with relative timestamps
- 🏠 Norton-Clone Home Screen — Protection Report ring, Wi-Fi Security, Scam Protection cards
- 👤 Profile Screen — Subscription card, account management rows
- 🎨 Norton Design System — exact colors, dark bottom nav, card styles

---

## 🗂️ Project Structure


app/src/main/java/com/example/norton_aifirst_intern_bipin_gupta/
├── data/
│   ├── model/          → RiskLevel, ScamAnalysisResult, ExampleMessage
│   ├── local/          → ScanHistoryEntity, ScanHistoryDao, ScamDatabase
│   └── repository/     → ScamAnalysisRepository, ScamAnalysisRepositoryImpl
├── domain/
│   ├── DetectionEngine.kt
│   ├── LocalMLClassifier.kt
│   └── usecase/        → AnalyzeMessageUseCase
├── ui/
│   ├── theme/          → NortonTheme, NortonColors, NortonTypography
│   ├── screen/         → HomeScreen, ScamDetectorScreen, ProfileScreen
│   ├── components/     → StatsDashboard, ScanHistoryList, RiskBadge, ConfidenceMeter
│   └── viewmodel/      → ScamDetectorViewModel
└── MainActivity.kt
app/src/test/
├── ScamAnalysisResultTest.kt
├── AnalyzeMessageUseCaseTest.kt
└── ScamDetectorViewModelTest.kt



---

## ⚙️ Setup Instructions

### Prerequisites
- Android Studio Hedgehog 2023.1.1 or newer
- Android SDK minimum 26, target 36
- Kotlin 2.0.21
- Emulator or physical device API 26+

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
- Select emulator or device running API 26+
- Press Run ▶

**4. Grant SMS Permission**
- On the Scam Detector tab tap Grant Permission
- Accept the READ_SMS dialog
- Your last 5 inbox messages will appear for scanning

**5. Run unit tests**
```bash
./gradlew test
```

---

## 🤖 AI Interaction Log

### Prompt 1 — Architecture and Data Models

**Prompt:**
> "I am building an Android app called Scam Message Detector using Kotlin and Jetpack Compose inspired by Norton Genie. Build the complete data layer and ViewModel following MVVM + Clean Architecture. Create RiskLevel enum with color hex values, ScamAnalysisResult with coerceIn confidence clamping, ExampleMessage with companion object, ScamAnalysisRepository interface, UseCase with blank input validation, and ViewModel with sealed UiState using StateFlow throughout. No LiveData anywhere."

**What Claude produced:** Full architecture — all data models, repository interface, use case with validation, ViewModel with sealed UiState and StateFlow.

**What I refined:** Claude initially generated the ViewModel using LiveData. I caught it and follow-up prompted: "Convert all LiveData to StateFlow and use collectAsStateWithLifecycle in Compose." I also added the color mapping function to RiskLevel myself since Claude omitted UI-specific concerns from the data layer.

**Verdict:** Accepted with modifications.

---

### Prompt 2 — Jetpack Compose UI

**Prompt:**
> "Build the full Jetpack Compose UI for ScamDetectorScreen inspired by Norton 360. Dark navy background #0A1628, Norton yellow #FFD700 for the CTA button. Include: header with shield icon and contentDescription, multiline text input, two tappable example chips using SuggestionChip, yellow Analyze button disabled during Loading state showing CircularProgressIndicator, result card with AnimatedVisibility, confidence bar with animateFloatAsState and FastOutSlowInEasing, and error card with Try Again button."

**What Claude produced:** Full screen composable with all required elements and animations.

**What I refined:** Claude used plain TextButton for example chips. I redesigned them as SuggestionChip with warning icons. I extracted all colors to NortonTheme.kt instead of inline hardcoded values. I changed animation easing to FastOutSlowInEasing for a polished feel.

**Verdict:** Accepted with styling refinements.

---

### Prompt 3 — Three-Layer Detection Pipeline

**Prompt:**
> "Upgrade ScamAnalysisRepositoryImpl to a three-layer detection pipeline. Layer 1: expanded keyword matching — financial fraud, impersonation of authorities, urgency manipulation. Layer 2: DetectionEngine class scoring keywordScore, urlScore detecting IP URLs and shorteners, patternScore — combined 40/35/25 weighting. Layer 3: LocalMLClassifier extracting features — exclamation count, all-caps words, dollar amounts, phone numbers — returning normalized 0-1 score. Final result: 60% DetectionEngine plus 40% LocalML."

**What Claude produced:** Complete three-layer system with DetectionEngine and LocalMLClassifier as separate domain classes.

**What I refined:** The URL regex missed some edge cases for IP-based phishing URLs. I tested with sample URLs manually and tightened the pattern. I also fixed the SAFE confidence calculation — Claude had it reading raw combinedScore instead of (1 - combinedScore) * 100 which was backwards.

**Verdict:** Accepted with logic corrections.

---

### Prompt 4 — Room Database and Stats Dashboard

**Prompt:**
> "Add Room database. Create ScanHistoryEntity with id autoGenerate, messageText, messagePreview first 50 chars, riskLevel, confidenceScore, explanation, redFlags as JSON string, scannedAt timestamp millis. DAO with Flow queries: getAllScans ordered DESC, getRecentScans limit, getTotalScanCount, getDangerousCount, deleteAll. Add totalScans, dangerousCount, recentHistory StateFlows to ViewModel using stateIn with SharingStarted.WhileSubscribed(5000)."

**What Claude produced:** Complete Room setup — database singleton, DAO, entity, updated ViewModel with three new StateFlows.

**What I refined:** Claude used SharingStarted.Eagerly which keeps Flows active when the app is backgrounded — wastes battery. I changed all three to WhileSubscribed(5000). I also wired up the automatic save-after-analysis call in the repository which Claude forgot to add.

**Verdict:** Accepted with battery optimization fix.

---

### Prompt 5 — AI Code Review

**Prompt:**
> "Review ScamDetectorViewModel.kt and ScamDetectorScreen.kt for: memory leaks, Compose recomposition issues from unstable lambdas, error handling gaps, accessibility missing contentDescriptions, anything that would fail a production code review at a mobile-first company like Norton. Label each finding CRITICAL, WARNING, or SUGGESTION."

**What Claude flagged:**
- CRITICAL — Inline lambdas in Compose parameters causing unnecessary recompositions
- CRITICAL — Missing contentDescription on shield icon and risk badge
- WARNING — Analyze button not disabled during Loading state allowing spam taps
- WARNING — API key hardcoded in repository class — should use BuildConfig
- PASS — StateFlow with collectAsStateWithLifecycle correctly implemented

**What I changed:** Fixed all four issues — hoisted lambdas, added all contentDescriptions, disabled button during Loading, moved key to local.properties via BuildConfig.

---

## 🧪 Unit Tests

| File | Tests | Notes |
|---|---|---|
| ScamAnalysisResultTest.kt | 4 | Data model validity, confidence clamping 0-100 |
| AnalyzeMessageUseCaseTest.kt | 5 | Happy path, blank input, whitespace, network error |
| ScamDetectorViewModelTest.kt | 4 | State transitions Idle→Loading→Success, clear action |

AI-generated test files are marked with `// AI-GENERATED: Reviewed and refined by Bipin Gupta`

**Manually added edge cases not caught by AI:**
- Whitespace-only string returning failure without calling repository
- TestCoroutineScope cancellation in @After to prevent test leaks

Run tests:
```bash
./gradlew test
```

---

## 📦 Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin 2.0.21 | Primary language |
| Jetpack Compose | Declarative UI |
| MVVM + Clean Architecture | App architecture |
| StateFlow + collectAsStateWithLifecycle | Reactive state |
| Room 2.7.1 | Local database |
| kotlinx.serialization | JSON for red flags storage |
| ContentResolver + Telephony.Sms | SMS inbox reading |
| Turbine | Flow testing |
| MockK | Mocking in unit tests |
| Google Truth | Test assertions |

---

## 🎥 Demo Video

> Add your YouTube or Loom link here

5-minute walkthrough covering:
1. App demo — Home screen, SMS scanning, Dangerous and Safe results
2. Code walkthrough — Architecture, DetectionEngine, ViewModel, Room
3. AI workflow — Actual Claude conversations, prompt refinements, code review findings

---

## 💭 Reflection

**What I learned:**

Prompt quality is architecture. A vague prompt produces generic code. A precise prompt that specifies the exact data model fields, StateFlow requirements, error handling strategy, and UI behavior constraints produces code that is production-ready with minimal changes.

The fastest AI workflow is: AI drafts the skeleton → I review critically → AI refines on specific feedback → I handle the final 10% that requires judgment. The whitespace edge case in tests, the backwards SAFE confidence score, the battery-draining SharingStarted.Eagerly — these are all things I caught that Claude missed. AI drafts. Engineer decides.

Exploring the real Norton 360 app before writing a single line of code was the best decision I made. Understanding their protection status banner, the circular threat report ring, and the Safe SMS permission flow directly shaped my implementation.

**What I would do differently:**

- Build NortonTheme.kt design system first before writing any screen
- Add Paparazzi screenshot tests for Compose UI regression testing
- Use Claude structured output mode for API calls instead of prompt-engineering JSON format

---

## 👨‍💻 Author

**Bipin Gupta**  
GitHub: [@bipin-griffith](https://github.com/bipin-griffith)  
Email: bgupta9861@gmail.com

---

*Submitted for Gen Digital · Norton Mobile Engineering · AI-First Intern Assignment*  
*Built with Kotlin, Jetpack Compose, and Claude AI*

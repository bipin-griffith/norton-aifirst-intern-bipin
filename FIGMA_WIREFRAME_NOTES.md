# Figma Wireframe Notes — Norton Scam Detector (Norton 360 Clone)

## Global Dimensions & Grid
- Screen dimensions: 360 × 800 dp (standard Android phone)
- Horizontal content padding: 16–20 dp
- Vertical section spacing: 12–16 dp
- Card corner radius: 12 dp
- Card elevation: 2 dp
- Card padding: 16 dp

---

## Color Tokens
| Name                  | Hex       | Usage                                    |
|-----------------------|-----------|------------------------------------------|
| Background            | #F5F5F0   | Screen background (light cream)          |
| CardBackground        | #FFFFFF   | All card surfaces                        |
| BottomNavBackground   | #1A1A1A   | Bottom navigation bar                    |
| PrimaryYellow         | #FFD700   | CTAs, selected icons, accents            |
| PrimaryYellowPressed  | #E6C200   | Button pressed state                     |
| SafeGreen             | #2E7D32   | Safe status text/icons                   |
| SafeGreenLight        | #E8F5E9   | Safe status card background              |
| SuspiciousOrange      | #E65100   | Warning text/icons                       |
| SuspiciousOrangeLight | #FFF3E0   | Warning card background                  |
| DangerousRed          | #C62828   | Danger text/icons                        |
| DangerousRedLight     | #FFEBEE   | Danger card background                   |
| TextPrimary           | #1A1A1A   | Headings, primary body text              |
| TextSecondary         | #757575   | Subtitles, captions                      |
| TextTertiary          | #BDBDBD   | Placeholders, disabled text              |
| DividerColor          | #E0E0E0   | Card dividers, horizontal rules          |
| ShieldGreen           | #00A651   | Avatar background, active status dots    |
| ProtectionRing        | #6D2B3D   | Protection report ring fill (dark maroon)|

## Typography
| Style           | Font Family        | Weight    | Size  | Line Height |
|-----------------|--------------------|-----------|-------|-------------|
| Heading Large   | sans-serif-medium  | Bold      | 24 sp | 32 sp       |
| Heading Medium  | sans-serif-medium  | Bold      | 20 sp | 28 sp       |
| Heading Small   | sans-serif-medium  | SemiBold  | 16 sp | 24 sp       |
| Title Large     | sans-serif-medium  | Bold      | 18 sp | 26 sp       |
| Body Large      | sans-serif         | Normal    | 16 sp | 24 sp       |
| Body Medium     | sans-serif         | Normal    | 14 sp | 20 sp       |
| Body Small      | sans-serif         | Normal    | 12 sp | 16 sp       |
| Label           | sans-serif         | Medium    | 14 sp | —           |

---

## Screen 1 — Bottom Navigation Bar
- Height: 60 dp + system nav inset
- Background: #1A1A1A
- 3 tabs, equal weight (each ~120 dp wide on 360 dp screen)
- Each tab:
  - Icon: 24 × 24 dp, centered
  - Selected tint: #FFD700
  - Unselected tint: #FFFFFF at 60% alpha
  - Selection indicator: 4 × 4 dp yellow circle dot, 4 dp below icon
- Tab icons:
  - Home: Home (house) icon
  - Scan: Shield icon
  - Profile: Person icon

---

## Screen 2 — Home Screen (ui/screen/HomeScreen.kt)
Screen dimensions: 360 × 800 dp

### Component Hierarchy (indented)
```
Column [fillMaxSize, Background #F5F5F0, verticalScroll, padding H:16]
  Spacer [height: 16dp]
  Row "Norton Logo" [verticalAlignment: center, spacing: 10dp]
    Canvas [size: 40×40dp]         ← yellow circle + white checkmark
    Text "norton" [Bold, 24sp, #1A1A1A, letterSpacing: -0.5sp]
  Card "Alert Banner" [fillMaxWidth, radius:12dp, elev:2dp]
    ↳ IF safe: background #E8F5E9
      Row [padding:16dp, spacing:12dp]
        Icon CheckCircle [size:32dp, tint:#2E7D32]
        Column
          Text "You are protected" [Bold, 15sp, #2E7D32]
          Text "All systems running normally" [12sp, #757575]
    ↳ IF dangerous: background #FFEBEE
      Row [padding:16dp, spacing:12dp]
        Icon Warning [size:32dp, tint:#C62828]
        Column
          Text "Core protection needed" [Bold, 15sp, #C62828]
          Text "{n} threat(s) detected…" [12sp, #757575]
        Button "Fix now" [Yellow bg, Black text, radius:8dp]
  Card "Wi-Fi Security" [fillMaxWidth, radius:12dp, elev:2dp, bg:#FFF]
    Row [padding:16dp, spacing:12dp, verticalAlignment:center]
      Box [size:40dp, circle, bg:#E8F5E9]
        Icon Wifi [size:22dp, tint:#2E7D32]
      Column
        Text "Secure network" [SemiBold, 14sp, #1A1A1A]
        Row [spacing:4dp]
          Box [size:6dp, circle, bg:#00A651]  ← green dot
          Text "Home Network" [12sp, #757575]
      Icon CheckCircle [size:20dp, tint:#00A651]
  Card "Protection Report" [fillMaxWidth, radius:12dp, elev:2dp, bg:#FFF]
    Column [padding:16dp]
      Text "Protection Report" [Bold, 15sp, #1A1A1A]
      Spacer [height:12dp]
      Row [spacing:16dp, verticalAlignment:center]
        Box [size:80×80dp]                    ← ring indicator
          Canvas [fillMaxSize]
            Arc background [stroke:8dp, #E0E0E0, 360°]
            Arc fill [stroke:8dp, #6D2B3D, sweep=(safe/total)*360°]
          Column [center]
            Text "{threatsAvoided}" [Bold, 20sp, #1A1A1A]
            Text "avoided" [9sp, #757575]
        Column [spacing:6dp]
          Text "Scans this session" [12sp, #757575]
          Row [spacing:6dp]
            Box [size:10dp, circle, bg:#6D2B3D]
            Text "Device" [Medium, 13sp, #1A1A1A]
          Text "{totalScans} total scans" [12sp, #757575]
  Card "Scam Protection" [fillMaxWidth, radius:12dp, elev:2dp, bg:#FFF]
    Row [padding:16dp, spacing:12dp, verticalAlignment:center]
      Box [size:40dp, circle, bg:#E8F5E9]
        Icon Security [size:22dp, tint:#00A651]
      Column [weight:1]
        Text "Scam Protection" [Bold, 14sp, #1A1A1A]
        Text "Included in your plan" [12sp, #757575]
      TextButton "Set up ›" [color:#1565C0, 13sp]
  Row [spacing:12dp]
    Card "Dark Web Monitoring" [weight:1, radius:12dp, elev:2dp, bg:#FFF]
      Column [padding:16dp, spacing:8dp]
        Icon Lock [size:24dp, tint:#757575]
        Text "Dark Web Monitoring" [SemiBold, 12sp, #1A1A1A]
        Button "Set Up" [fillMaxWidth, Yellow, Black, radius:8dp]
    Card "VPN" [weight:1, radius:12dp, elev:2dp, bg:#FFF]
      Column [padding:16dp, spacing:8dp]
        Icon Language [size:24dp, tint:#757575]
        Text "VPN" [SemiBold, 12sp, #1A1A1A]
        Button "Enable" [fillMaxWidth, Yellow, Black, radius:8dp]
  Card "Device Count" [fillMaxWidth, radius:12dp, elev:2dp, bg:#FFF]
    Row [padding H:16 V:12, spacing:10dp, verticalAlignment:center]
      Icon PhoneAndroid [size:20dp, tint:#757575]
      Text "1/1 devices used" [14sp, #1A1A1A, weight:1]
      TextButton "Manage" [color:#1565C0, 13sp]
  Spacer [height:8dp]
```

### Component Sizes
| Component           | Width    | Height (approx) |
|---------------------|----------|-----------------|
| Norton Logo row     | fill     | 44 dp           |
| Alert Banner Card   | fill     | 80–100 dp       |
| Wi-Fi Card          | fill     | 72 dp           |
| Protection Report   | fill     | 130 dp          |
| Scam Protection     | fill     | 72 dp           |
| Dark Web + VPN row  | fill     | 120 dp          |
| Device Count row    | fill     | 52 dp           |

---

## Screen 3 — Scam Detector (ui/screen/ScamDetectorScreen.kt)
Screen dimensions: 360 × 800 dp

### Component Hierarchy
```
Box [fillMaxSize, bg:#F5F5F0]
  Column [verticalScroll, padding H:20 V:24, spacing:16dp]
    Row "Header" [spacing:10dp]
      Icon Security [size:28dp, tint:#FFD700]
      Text "Scam Detector" [Bold, 22sp, #1A1A1A]
    Text "Subtitle" [13sp, #757575, lineHeight:18sp]
    ↳ IF no SMS permission:
      Card "SMS Permission" [fillMaxWidth, border:1dp #FFD700, radius:12dp, elev:2dp, bg:#FFF]
        Row [padding:16dp, spacing:12dp]
          Icon Warning [size:28dp, tint:#FFD700]
          Column [spacing:6dp]
            Text "Scan your messages" [Bold, 15sp, #1A1A1A]
            Text subtitle [13sp, #757575]
            Button "Grant Permission" [Yellow, Black, radius:8dp]
    ↳ IF SMS permission granted:
      Card "Inbox Scanner" [fillMaxWidth, radius:12dp, elev:2dp, bg:#FFF]
        Column [padding:16dp]
          Row [spacing:8dp]
            Icon Inbox [size:20dp, tint:#00A651]
            Text "Inbox Scanner" [Bold, 14sp, #1A1A1A]
          ↳ For each SMS (up to 5):
            HorizontalDivider [0.5dp, #E0E0E0]
            Row [fillMaxWidth, verticalAlignment:center, spacing:8dp]
              Column [weight:1]
                Text sender [SemiBold, 13sp, #1A1A1A, maxLines:1]
                Text body[0..60] [12sp, #757575, maxLines:2]
              Button "Scan" [height:32dp, Yellow, Black, radius:6dp]
    StatsDashboard [3 stat cards]
    OutlinedTextField [fillMaxWidth, minLines:4, maxLines:8, radius:10dp]
      focused border: #FFD700
      unfocused border: #757575 @ 40%
    Row "Example chips" [horizontalScroll]
    Button "Analyze Message" [fillMaxWidth, height:52dp, Yellow, Black, radius:10dp]
    ↳ IF success: AnalysisResultCard
    ↳ IF error: Error card [bg:#C62828 @ 15%]
    ↳ IF history: Recent Scans section
```

### Component Sizes
| Component               | Width | Height  |
|-------------------------|-------|---------|
| Header row              | fill  | 44 dp   |
| SMS permission card     | fill  | ~130 dp |
| Inbox scanner card      | fill  | ~180 dp |
| Stats dashboard         | fill  | 80 dp   |
| Text input field        | fill  | ~120 dp |
| Chips row               | fill  | 44 dp   |
| Analyze button          | fill  | 52 dp   |
| Result card             | fill  | ~180 dp |

---

## Screen 4 — Profile (ui/screen/ProfileScreen.kt)
Screen dimensions: 360 × 800 dp

### Component Hierarchy
```
Column [fillMaxSize, bg:#F5F5F0, verticalScroll]
  Spacer [height:24dp]
  Row "Avatar + Welcome" [padding H:20, verticalAlignment:center]
    Box [size:72dp, circle, bg:#00A651]
      Icon Person [size:36dp, tint:#FFFFFF]
    Spacer [width:16dp]
    Column
      Text "Welcome" [Bold, 20sp, #1A1A1A]
      Text "user@example.com" [14sp, #757575]
  Spacer [height:28dp]
  Text "SUBSCRIPTION" [section header: 11sp, Medium, #757575, letterSpacing:1sp, padding H:20]
  Spacer [height:8dp]
  Card "Subscription" [padding H:16, radius:12dp, elev:2dp, bg:#FFFFFF]
    Row [fillMaxWidth]
      Box [width:4dp, height:140dp, bg:#2E7D32]   ← left accent border
      Box [weight:1, bg:#F1F8F1, padding:16dp]
        Column
          Row
            Box badge [bg:#2E7D32, radius:20dp, padding H:10 V:3]
              Text "Active" [White, 11sp, SemiBold]
            Icon Security [size:28dp, tint:#00A651]
          Spacer [height:10dp]
          Text "Norton Mobile Security" [Bold, 15sp, #1A1A1A]
          Spacer [height:4dp]
          Text subtitle [12sp, #757575]
          Spacer [height:12dp]
          OutlinedButton "Upgrade options" [radius:8dp, border:#1A1A1A]
  Spacer [height:24dp]
  Text "ACCOUNT" [section header]
  Spacer [height:8dp]
  Card [padding H:16, radius:12dp, elev:2dp, bg:#FFF]
    ProfileMenuItem "Manage account"   [ManageAccounts icon, ChevronRight]
    HorizontalDivider [padding H:16, 0.5dp, #E0E0E0]
    ProfileMenuItem "Manage devices"   [PhoneAndroid icon, Add, sublabel "1/1 devices used"]
  Spacer [height:24dp]
  Text "MORE" [section header]
  Spacer [height:8dp]
  Card [padding H:16, radius:12dp, elev:2dp, bg:#FFF]
    ProfileMenuItem "Privacy Settings"  [Settings icon, ChevronRight]
    HorizontalDivider
    ProfileMenuItem "Help and support"  [Help icon, ChevronRight]
    HorizontalDivider
    ProfileMenuItem "About"             [Info icon, ChevronRight]
    HorizontalDivider
    ProfileMenuItem "Sign out"          [ExitToApp icon, ChevronRight, labelColor:#C62828]
  Spacer [height:32dp]
```

### ProfileMenuItem anatomy
```
Row [padding H:16 V:14, fillMaxWidth]
  Box [size:40dp, circle, bg:#F5F5F5]   ← icon badge
    Icon [size:20dp, tint:#757575]
  Spacer [width:14dp]
  Column [weight:1]
    Text label [15sp, color varies]
    Text? sublabel [12sp, #757575]
  Icon ChevronRight [size:20dp, tint:#BDBDBD]
```

### Component Sizes
| Component            | Width | Height (approx) |
|----------------------|-------|-----------------|
| Avatar row           | fill  | 80 dp           |
| Subscription card    | fill  | 160 dp          |
| Account section card | fill  | 110 dp          |
| More section card    | fill  | 220 dp          |
| Each MenuItem row    | fill  | 56 dp           |

---

## Assets / Icons Used (Material Icons Extended)
- `Icons.Filled.Home` — Home tab
- `Icons.Filled.Shield` — Scan tab
- `Icons.Filled.Person` — Profile tab / avatar
- `Icons.Filled.Security` — Scam Detector header, Scam Protection card
- `Icons.Filled.CheckCircle` — Safe banner, Wi-Fi card
- `Icons.Filled.Warning` — Danger banner, SMS permission card
- `Icons.Filled.Wifi` — Wi-Fi card
- `Icons.Filled.Lock` — Dark Web card
- `Icons.Filled.Language` — VPN card
- `Icons.Filled.PhoneAndroid` — Device count row, Manage devices row
- `Icons.Filled.Inbox` — Inbox Scanner header
- `Icons.Filled.Settings` — Privacy Settings row
- `Icons.AutoMirrored.Filled.Help` — Help row
- `Icons.Filled.Info` — About row
- `Icons.AutoMirrored.Filled.ExitToApp` — Sign out row
- `Icons.Filled.ChevronRight` — Menu row trailing icon
- `Icons.Filled.Add` — Manage devices trailing icon
- `Icons.Filled.ManageAccounts` — Manage account row
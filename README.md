# SMS Relay (Play-safe, Notification Listener)

- Light "silvery white" theme, black text, adaptive icon
- Uses Notification Listener (no SMS/CallLog permissions) → Play Store friendly
- Forwards SMS notifications to Telegram or WhatsApp Cloud API
- Foreground service with a persistent notification + counter
- Simple file-based logs (can swap to Room later)

## Build
- Android Studio Ladybug+
- JDK 17

### GitHub Actions (no gradle wrapper needed)
See `.github/workflows/android.yml` — downloads Gradle 8.7 and runs `assembleDebug`.

## First run
- Open app → grant Notification access
- Enable "Activate forwarding" in Filters tab
- Configure Telegram/WA in Settings tab
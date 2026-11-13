# TS7 AutoLaunch (com.fieldops.mobile)

Generated with ApkForge.

## Project Configuration
- **Application ID**: `com.fieldops.mobile`
- **Version**: `1.0.0` (code `1`)
- **SDK Levels**: `minSdk 26` / `targetSdk 27`
- **Web Source**: `assets/www/index.html (Bundled)`

## Compile APK Locally
1. Open this folder in **Android Studio** (Ladybug or newer) or install JDK 17 + Android SDK.
2. Run Gradle to build a debug APK:
   ```bash
   gradle assembleDebug
   ```
   Output: `app/build/outputs/apk/debug/app-debug.apk`

3. Generate a release keystore and build a signed release APK:
   ```bash
   bash scripts/create-keystore.sh
   export ANDROID_KEYSTORE_PATH="$(pwd)/release-keystore.jks"
   export ANDROID_KEYSTORE_PASSWORD="your-store-password"
   export ANDROID_KEY_ALIAS="upload-key"
   export ANDROID_KEY_PASSWORD="your-key-password"
   gradle assembleRelease
   ```
   Output: `app/build/outputs/apk/release/app-release.apk`

## Compile APK via GitHub Actions
Push this folder to a GitHub repository. The included `.github/workflows/build-apk.yml` workflow automatically compiles and uploads the `.apk` build artifact.

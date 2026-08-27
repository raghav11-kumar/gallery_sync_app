# Fix NullPointerException in KSP during Gradle Sync

The project is experiencing a `NullPointerException` in KSP because it is trying to access deprecated/removed AGP `BaseVariant` APIs. This is likely due to using an older version of KSP with a newer AGP (9.3.1) and missing the Kotlin Android plugin declaration.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/gradle/libs.versions.toml)
- Update `agp` version to `9.3.2`.
- Add `kotlin` version `2.4.10`.
- Update `ksp` version to `2.4.10-1.0.28`.
- Add `kotlin-android` plugin declaration.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/build.gradle.kts)
- Add the Kotlin Android plugin to the `plugins` block with `apply false`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/build.gradle.kts)
- Apply the Kotlin Android plugin.
- Correct the Hilt plugin application (remove `apply false`).
- Add missing Hilt KSP dependency.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` or trigger a Gradle Sync in Android Studio.

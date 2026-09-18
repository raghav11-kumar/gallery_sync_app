# Fix SecurityException in Instrumentation Tests

The project is experiencing a `java.lang.SecurityException: Injecting input events requires the caller to have the INJECT_EVENTS permission` during UI Automator tests. This is likely caused by an invalid `targetSdk` configuration and incorrect library dependency scoping.

## Proposed Changes

### Build Configuration
#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/build.gradle.kts)
- Lower `compileSdk` and `targetSdk` from `37` to `35` (Android 15), as API 37 is not yet supported.
- Change `uiautomator` dependency from `implementation` to `androidTestImplementation` and update to version `2.4.0`.
- Correct Room dependencies: Replace `androidx.room3:room3-runtime` and `room3-compiler` with `androidx.room:room-runtime` and `room-compiler` (using the correct version from `libs.versions.toml`).
- Remove the ancient `com.android.support:support-v4:19.1.0` dependency.

### Room Database Migration
#### [MODIFY] Multiple Files
- Replace all imports of `androidx.room3.*` with `androidx.room.*` across the codebase to ensure compatibility with the official Room library.

### Instrumentation Test Improvement
#### [MODIFY] [ExampleInstrumentedTest.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/androidTest/java/com/example/gallery_sync_app/ExampleInstrumentedTest.kt)
- Add `device.wakeUp()` and `device.unlock()` calls to ensure the device is ready for input injection.
- Improve error logging to capture more detail if failure persists.

## Verification Plan

### Automated Tests
- Run the instrumented test using:
  ```bash
  ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.gallery_sync_app.ExampleInstrumentedTest
  ```
- Verify that the `SecurityException` no longer occurs and the test can interact with the UI.

### Manual Verification
- Deploy the app to an emulator/device and verify that it starts without crashes related to Room or missing dependencies.

# Fix SecurityException: Injecting input events in UI tests

The user is encountering a `java.lang.SecurityException: Injecting input events requires the caller to have the INJECT_EVENTS permission` during instrumentation tests. This is a common issue when using `UiAutomator` on modern Android versions (API 34+) or when the device state (e.g., locked screen) prevents input injection.

## Proposed Changes

### Build Configuration
#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/build.gradle.kts)
- Correctly move `uiautomator` from `implementation` to `androidTestImplementation`.
- Downgrade to a stable version (2.3.0) as 2.4.0-beta02 might be unstable or causing permission issues in certain environments.
- Clean up duplicate dependencies.

### Android Manifest
#### [MODIFY] [app/src/main/AndroidManifest.xml](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/AndroidManifest.xml)
- Add the `INJECT_EVENTS` permission with `tools:ignore="ProtectedPermissions"`. While this is a signature permission, declaring it can help in some testing environments where the shell needs to explicitly grant it to the instrumentation process.

### UI Test
#### [MODIFY] [app/src/androidTest/java/com/example/gallery_sync_app/ExampleInstrumentedTest.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/androidTest/java/com/example/gallery_sync_app/ExampleInstrumentedTest.kt)
- Add logic to wake up the device and dismiss the lock screen before performing interactions.
- Refactor the app launch logic to use `Context.startActivity` with a launch intent, which is more reliable than simulating swipes on the launcher to find the app icon. This avoids the need for `INJECT_EVENTS` during the app startup phase.

## Verification Plan

### Manual Verification
- Run the instrumented test `checkCurrentScreen` in `ExampleInstrumentedTest.kt` from Android Studio.
- Verify that the device wakes up, the app launches, and the test proceeds to find the `SignInButton` without a `SecurityException`.

# Improve Toolbar Dimensions and Layout

The goal is to enhance the toolbar in `MainScreen.kt` to better fit the screen (especially with edge-to-edge support) and improve the layout of its items.

## Proposed Changes

### [Component Name] Layout and Appearance

#### [MODIFY] [activity_main_screen.xml](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/res/layout/activity_main_screen.xml)
- Wrap `MaterialToolbar` in an `AppBarLayout` for better Material 3 behavior.
- Add `app:titleCentered="true"` to center the title, providing a more balanced look.
- Fix constraints to avoid redundancy and ensure proper alignment.
- Add elevation/styling to the `AppBarLayout`.

#### [MODIFY] [MainScreen.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/mainApp/MainScreen.kt)
- Add Window Insets handling to apply top padding to the `AppBarLayout` so it doesn't overlap with the status bar (since `enableEdgeToEdge()` is used).
- Clean up menu inflation logic to avoid duplicate items when switching destinations.

## Verification Plan

### Manual Verification
- Deploy the app and verify the toolbar is correctly positioned below the status bar.
- Verify the title is centered.
- Verify the menu items are visible and not duplicated when navigating back and forth.

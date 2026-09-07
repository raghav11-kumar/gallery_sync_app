# Fix SQLiteException and Project Configuration

The app is experiencing an `SQLiteException: no such column: creator` when querying the `pdu` table. This query is likely triggered by the system or a legacy library when interacting with media or messages. Additionally, the project has several configuration errors, including a non-existent `androidx.room3` package and an extremely old support library.

## Proposed Changes

### [Component Name] Project Configuration

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/build.gradle.kts)
- Set `compileSdk` and `targetSdk` to 35.
- Fix Room dependencies to use `androidx.room`.
- Remove legacy `support-v4:19.1.0`.
- Fix typo in Room runtime/compiler names.

### [Component Name] Room Database Migration

#### [MODIFY] All Room-related files
- Replace `androidx.room3` with `androidx.room`.

Files to be updated:
- [Images.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/data/Images.kt)
- [Tables.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/data/roomDataBase/Tables.kt)
- [UserDao.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/data/roomDataBase/UserDao.kt)
- [RoomDataBaseImp.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/data/roomDataBase/RoomDataBaseImp.kt)
- [DataBaseModule.kt](file:///C:/Users/Blaze.BAS-IT-OLP-047/AndroidStudioProjects/gallerysyncapp/app/src/main/java/com/example/gallery_sync_app/screens/di/DataBaseModule.kt)

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure the project builds successfully with the new configurations.

### Manual Verification
- Deploy to device/emulator and verify that the `SQLiteException` no longer occurs during gallery operations.

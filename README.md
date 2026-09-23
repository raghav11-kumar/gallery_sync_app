# Gallery Sync App

Gallery Sync App is an Android application that combines **Firebase Authentication, Room Database, Firestore, BLE communication, WebSockets, notifications, and UI automation testing** in a single application.

The project was developed using **MVVM architecture and SOLID principles**, with the goal of keeping the application modular, maintainable, and easy to extend.

---

## Features

# Authentication

* User registration and login using **Firebase Authentication**.
* Maintains the user's logged-in state.
* Displays the authenticated user's information.
* Allows the user to edit their profile name.

##Gallery

* Pick images from the device gallery.
* Display selected images using a `RecyclerView`.
* Store image information locally using **Room Database**.
* Store image information remotely using **Firebase Firestore**.
* Supports working with locally stored data when network connectivity is unavailable.
* Delete images from the application.

# BLE Device

The application provides a BLE device screen where users can:

* Scan for nearby Bluetooth Low Energy devices.
* Display discovered devices in a `RecyclerView`.
* Select a BLE device.
* Pair/bond with the device.
* Connect to the device using GATT.
* Discover BLE services and characteristics.
* Write data to BLE characteristics.
* Enable notifications.
* Receive and process data from the BLE device.
* Display connection/device information.

## WebSocket

The application includes WebSocket communication for live data.

* Establishes a WebSocket connection.
* Sends data through the connection.
* Receives live messages from the server(node js).
* Processes incoming WebSocket data.
* Displays live communication in the application.

# Notifications

The application supports Android notifications and includes notification handling for application events.

#User Profile

The profile section allows the authenticated user to:

* View their account information.
* View their profile information.
* Edit their name.

## Architecture

The application follows the **MVVM (Model-View-ViewModel)** architecture.

### MVVM Responsibilities

**View**

* Displays the UI.
* Handles user interactions.
* Observes UI state.
* Does not directly manage application data.

**ViewModel**

* Holds UI-related state.
* Coordinates actions from the UI.
* Communicates with repositories.
* Uses observable state such as `StateFlow` and `SharedFlow`.

**Repository**

* Handles data-related operations.
* Coordinates between local and remote data sources.
* Keeps data access logic separate from the UI.

---

## Technologies Used

Technology               : Purpose                               

1 Kotlin                   =Main programming language             
2 Android SDK              = Android application development       
3 XML                      = UI layouts                            
4 ViewBinding              = View access without `findViewById`    
5 MVVM                     = Application architecture              
6 SOLID                    = Code design principles                
7 Hilt                     = Dependency injection                  
8 Navigation Component     = Fragment navigation                   
9 Room                     = Local database                        
10 Firebase Authentication  = User authentication                   
11 FirebaseFirestore       =Cloud data storage                    
12 Firebase Cloud Messaging = Push notifications                    |
13 Retrofit                 = REST API communication                |
14 WebSocket                = Real-time communication               |
15 BLE / GATT               = Bluetooth Low Energy communication    |
16 RecyclerView             = Displaying lists                      |
17 StateFlow                = Observable application/UI state       |
18 SharedFlow               = One-time/event-based UI communication |
19 UI Automator             = UI automation testing                 |
20 JUnit / AndroidX Test    = Application testing                   |

---

## Local and Cloud Data

The application uses both **Room** and **Firestore**.

### Room

Room is used for local storage and allows the application to retain data on the device.

```text
Application
     │
     ▼
 Repository
     │
     ▼
   Room DB
```

### Firestore

Firestore is used as the remote/cloud data source.

```text
Application
     │
     ▼
 Repository
     │
     ▼
 Firestore
```

The repository is responsible for coordinating the appropriate data source instead of making the UI directly communicate with Room or Firestore.

---

## BLE Communication Flow

The BLE functionality follows a typical Android BLE communication flow:

```text
Scan
  │
  ▼
Discover Device
  │
  ▼
Select Device
  │
  ▼
Bond / Pair
  │
  ▼
Connect using GATT
  │
  ▼
Discover Services
  │
  ▼
Discover Characteristics
  │
  ▼
Read / Write
  │
  ▼
Enable Notifications
  │
  ▼
Receive BLE Data
```

The application also handles Bluetooth permissions and BLE connection states.

---

## WebSocket Communication

The WebSocket feature provides a persistent connection between the Android application and the server.

```text
Android App
     │
     │ WebSocket Connection
     ▼
 WebSocket Server
     │
     │ Live Messages
     ▼
Android App
```

Unlike a normal REST API request, the WebSocket connection can remain open and receive messages continuously.

---

## Testing

The application includes Android testing and UI automation.

### UI Automator

UI Automator is used to automate interactions with the application, including:

* Launching the application.
* Finding UI elements.
* Clicking buttons.
* Entering login information.
* Navigating between screens.
* Verifying expected UI elements.
* Checking the Gallery screen.

Example test flow:

```text
Launch Application
       │
       
   Login Screen
       │
       
Enter Email
       │
       
Enter Password
       │
       
     Login
       │
       
  Main Screen
       │
bleDeviceScreen->Verify BleDeviceScreen
       |
    GalleryScreen->Verify Gallery UI
       │
websocketsScreen ->verify websocketsScreen
      |
Notification
    |
UserProfile ->edit name Logout
       
```

Testing was also performed on different Android devices. Device-specific behavior was investigated when UI automation behaved differently on devices such as Xiaomi/MIUI.

---

## Main Screens

### Login / Sign In

Provides authentication using Firebase Authentication.

### Main Screen

Acts as the main entry point after authentication and provides access to:

* Gallery
* BLE Device
* WebSocket
* Notifications
* User Profile

### Gallery Screen

Allows users to select, display, store, and delete images.

### BLE Device Screen

Allows users to scan, discover, pair, connect, and communicate with BLE devices.

### WebSocket Screen

Provides live communication through a WebSocket connection.

### User Profile

Displays the user's information and allows profile information such as the name to be edited.

---

## Key Android Concepts Practiced

This project was also used as a practical learning project for:

* Activities and Fragments
* Fragment lifecycle
* Navigation Component
* Navigation Graph
* MVVM architecture
* SOLID principles
* Repository pattern
* Dependency Injection
* Hilt
* RecyclerView
* ViewBinding
* Room Database
* SharedPreferences
* Firebase Authentication
* Firebase Firestore
* Firebase Cloud Messaging
* Retrofit
* WebSockets
* MQTT concepts
* Bluetooth Low Energy
* GATT
* Bluetooth permissions
* BroadcastReceiver
* StateFlow
* SharedFlow
* Android notifications
* UI Automator
* Instrumented testing
* Debugging and Logcat

---

## Project Goals

The main goals of this project were to:

1. Build a complete Android application instead of isolated sample applications.
2. Apply MVVM architecture to a real application.
3. Practice SOLID principles and separation of responsibilities.
4. Work with both local and cloud data storage.
5. Implement real-time communication using WebSockets.
6. Implement Bluetooth Low Energy communication.
7. Understand Android permissions and lifecycle behavior.
8. Create automated UI tests.
9. Debug and handle device-specific Android behavior.
10. Build a maintainable foundation that can be extended with additional features.

---

l

## Conclusion

Gallery Sync App is a complete Android project that combines authentication, local and cloud storage, image management, Bluetooth Low Energy communication, WebSocket communication, notifications, and automated UI testing.

The project demonstrates practical usage of modern Android development concepts while following **MVVM architecture, Repository pattern, dependency injection, and SOLID principles**.

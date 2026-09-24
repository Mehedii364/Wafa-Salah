# 🕌 Wafa Salah

### Premium Islamic Salah Tracker for Android

**Wafa Salah** is a modern Islamic Android application designed to help Muslims track daily Salah, view real-time prayer times, follow a live Salah countdown, read the Quran and Duas, find the Qibla direction, use a digital Tasbih, and access Islamic calendar features.

Built with a modern Android architecture using **Kotlin, Jetpack Compose, Material 3, Room, DataStore, AndroidX, Coroutines, and Media3**.

---

## ✨ Features

### 🕌 Salah & Prayer Times

* 📍 Real device location support
* 🕐 Real-time daily prayer times
* ⏳ Live countdown to the next Salah
* 🔄 Automatic prayer-time recalculation
* 🌅 Fajr
* ☀️ Sunrise
* 🕛 Dhuhr
* 🌤️ Asr
* 🌇 Maghrib
* 🌙 Isha
* 📅 Daily and monthly prayer timetable
* ⚙️ Configurable calculation method
* 🧭 Configurable Asr calculation method
* 🌍 Automatic timezone support

### ⏱️ Live Salah Countdown

The home screen provides a real-time countdown showing:

* Current prayer
* Next prayer
* Prayer time
* Remaining time
* Prayer status

The countdown updates continuously using the device's actual time.

After Isha, the application automatically moves to the next day's Fajr.

---

## 📊 Salah Tracker

Track your daily Salah with simple status management:

* ✅ Completed
* ❌ Missed
* ⏳ Not marked
* 📈 Daily statistics
* 📅 Weekly statistics
* 📆 Monthly statistics
* 🔥 Salah streak tracking

---

## 📖 Quran

A dedicated Quran reading experience with:

* 📚 114 Surahs
* 🗂️ Juz navigation
* 🔎 Quran search
* 🔖 Bookmarks
* 📌 Last-read position
* 🎧 Quran audio
* ▶️ Background audio playback
* 📱 Clean reading interface

### Quran Content

The reader supports multiple content layers:

* 🇸🇦 Arabic
* 🔤 Bangla pronunciation
* 🇬🇧 English meaning
* 🇧🇩 Bangla meaning

Each content layer can be controlled independently.

> Quranic text should come from verified data sources and should never be generated or modified by AI.

---

## 🤲 Dua & Zikir

Access Islamic Duas and Zikir through organized categories.

Features include:

* 🤲 Dua categories
* 🔖 Bookmarks
* 🔊 Audio support where available
* 📤 Share functionality
* 📚 Source/reference information
* 🔎 Search

The application is designed to avoid fabricated Islamic references or generated religious source material.

---

## 🧭 Qibla

Real device-based Qibla functionality using:

* 📍 Current location
* 🧭 Device sensors
* 🌍 Geographic coordinates
* 🔄 Real-time direction updates

The Qibla direction is calculated from the user's actual location rather than using a simulated or hardcoded position.

---

## 📿 Tasbih

Digital Tasbih counter with:

* ➕ Tap-to-count
* 🔄 Reset
* 🎯 Target counting
* 📊 Progress tracking
* 📱 Clean mobile interface

---

## 🌙 Islamic Calendar

Access Islamic calendar information including:

* 🌙 Hijri date
* 📅 Gregorian date
* 🕌 Islamic months
* 🌙 Ramadan mode
* 📆 Calendar navigation

---

## 🔔 Notifications

Prayer-related notifications can include:

* 🕌 Prayer reminders
* ⏰ Upcoming prayer notifications
* 🔊 Azan support
* 📳 Vibration
* ⚙️ Individual notification controls

Notification behavior is designed to respect Android background and permission requirements.

---

## 🎧 Audio

Audio functionality is powered by Android-compatible background playback technology.

Supported capabilities include:

* ▶️ Play
* ⏸️ Pause
* ⏭️ Next
* ⏮️ Previous
* 🔊 Background playback
* 🔒 Lock-screen controls
* 📱 Media notification controls
* 💾 Audio caching where supported

---

## 📴 Offline-First Experience

Wafa Salah is designed with offline usability in mind.

Local technologies include:

* Room Database
* DataStore
* Local caching
* Offline content access

Internet access should not be required for every basic application operation.

---

## 🎨 Premium UI

The application uses a modern Islamic visual system built around:

* Material 3
* Jetpack Compose
* Responsive layouts
* Light mode
* Dark mode
* Premium cards
* Smooth animations
* Modern typography
* Consistent spacing
* Accessible touch targets

### Visual Identity

Primary design direction:

**Emerald • Deep Emerald • Gold • Soft Gold**

The interface is intentionally designed to remain clean and readable rather than overloaded with colors or effects.

---

## 🛠️ Technology Stack

| Technology            | Usage                        |
| --------------------- | ---------------------------- |
| Kotlin                | Primary programming language |
| Jetpack Compose       | Modern UI                    |
| Material 3            | Design system                |
| AndroidX              | Android architecture         |
| Room                  | Local database               |
| DataStore             | Preferences and settings     |
| Coroutines            | Asynchronous operations      |
| ViewModel             | UI state management          |
| Media3 / ExoPlayer    | Audio playback               |
| Android Location APIs | Real device location         |
| Android Sensors       | Qibla direction              |
| Gradle                | Android build system         |

---

## 🏗️ Architecture

Wafa Salah follows a modular Android architecture designed for maintainability.

```text
Wafa Salah
│
├── UI
│   ├── Home
│   ├── Quran
│   ├── Dua
│   ├── Qibla
│   ├── Salah Tracker
│   ├── Tasbih
│   ├── Calendar
│   └── Settings
│
├── Data
│   ├── Room
│   ├── DataStore
│   ├── Prayer
│   ├── Quran
│   ├── Dua
│   └── Islamic Content
│
├── Services
│   ├── Audio
│   ├── Notifications
│   └── Prayer Reminders
│
└── Android Platform
    ├── Location
    ├── Sensors
    └── Background Services
```

---

## 📱 Android Requirements

The application is designed for modern Android devices.

Required capabilities may include:

* Location permission for automatic prayer times
* Sensor access for Qibla
* Notification permission on supported Android versions
* Audio/background playback permissions where required

Permissions are requested only when the related feature needs them.

---

## 🚀 Build From Source

### Requirements

* Android Studio
* Android SDK
* JDK
* Gradle-compatible environment

Clone the repository:

```bash
git clone https://github.com/Mehedii364/Wafa-Salah.git
cd Wafa-Salah
```

Build the debug APK:

```bash
./gradlew assembleDebug
```

The standard Gradle debug APK output is:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📦 APK

Installable APK builds can be distributed through the project's GitHub Releases or repository build artifacts when available.

> Always verify that an APK is produced by the real Android/Gradle build process before distributing it.

---

## 🔐 Privacy

Wafa Salah is designed with privacy in mind.

Location information is used for features such as:

* Prayer-time calculation
* Qibla direction
* Current city/location display

The application should request only the permissions required for enabled features.

---

## 🎯 Project Goals

Wafa Salah aims to provide a single, clean Islamic companion for everyday use:

```text
Prayer Times
     ↓
Live Salah Countdown
     ↓
Salah Tracking
     ↓
Quran + Dua
     ↓
Qibla + Tasbih
     ↓
Islamic Calendar
     ↓
Notifications
```

The focus is on **accuracy, usability, offline support, privacy, and a modern Android experience**.

---

## 👨‍💻 Developer

### Md. Mehedi Hasan

**Mehedi364**

Web Developer • Software Engineer • Programmer

Building modern, secure, fast, and user-friendly applications.

---

## ❤️ Credits

**Developed by Mehedi364**

**Created & Developed by Md. Mehedi Hasan**

---

## 🌐 Links

* GitHub: [Mehedi364](https://github.com/Mehedii364)
* Project: [Wafa Salah](https://github.com/Mehedii364/Wafa-Salah)

---

## ⭐ Support the Project

If you find Wafa Salah useful, consider giving the repository a ⭐ on GitHub.

Your support helps the project continue to improve.

---

### 🕌 Wafa Salah

**Pray on time. Track your Salah. Stay connected with the Quran.**

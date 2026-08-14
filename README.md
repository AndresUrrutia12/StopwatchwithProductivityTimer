# ⏱️ Android Stopwatch App

A modern, functional Stopwatch application built for Android using **Kotlin**. This project was developed as part of the Android Developer track on Jetbrains Academy / Hyperskill.

It goes beyond a basic timer by featuring dynamic UI color changes, configurable upper-limit time alerts, and Android system notifications (including Android 13+ runtime permission handling).

---

## ✨ Features

- **Precision Stopwatch:** Start, pause, and reset options with clean time formatting (`MM:SS`).
- **Custom Time Limits:** Set an upper time limit in seconds via an interactive popup dialog.
- **Visual Feedback:** 
  - Dynamic `ProgressBar` with randomized tint color shifts.
  - Text color indicator changes to red when the time limit is reached.
- **System Notifications:** 
  - Triggers a high-priority system notification with custom flags when the upper limit is exceeded.
  - Fully compliant with **Android 13+ (API 33)** runtime permission requirements (`POST_NOTIFICATIONS`).
  - Utilizes Notification Channels and persistent alert flags (`FLAG_INSISTENT`, `OnlyAlertOnce`).

---

## 🛠️ Tech Stack & Concepts Applied

- **Language:** Kotlin
- **UI Components:** View Binding / `findViewById`, `AlertDialog`, `ProgressBar`, `ColorStateList`
- **Concurrency & Timing:** `Handler` and `Looper.getMainLooper()` with recurring `Runnable` tasks
- **Android Architecture & APIs:**
  - `NotificationChannel` & `NotificationCompat.Builder`
  - `registerForActivityResult` & `ActivityResultContracts` for modern runtime permission handling
  - Bitwise operations on Notification Flags (`Notification.FLAG_INSISTENT`)
  - Material Design components

---

## 📸 Screenshots

<img width="400" height="840" alt="Screen Recording 2026-08-14 at 3 41 19 AM" src="https://github.com/user-attachments/assets/7a64490c-21bc-4c06-a840-7fcfa5007239" />

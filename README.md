TempCheck – Android Overlay Monitor
A floating overlay app for Android that displays real-time CPU temperature, GPU temperature, and FPS while you use your device.

📌 Features
📊 Real-time monitoring of CPU and GPU temperatures

🎮 FPS counter for games and apps

🪟 Overlay stays visible above all apps

🔒 Toggle control directly from the app

⚙ Uses minimal system resources

🛠 How It Works
Temperatures are read from /sys/class/thermal/ device files

FPS is measured using frame callbacks

Runs as a foreground service with overlay permissions

📲 Installation
Download the latest APK from the Releases page

Install it on your device (you may need to allow "Install from unknown sources")

Open the app and grant overlay permission when prompted

Toggle the switch to enable the overlay

🔑 Permissions
SYSTEM_ALERT_WINDOW — required to draw overlay above other apps

FOREGROUND_SERVICE — keeps overlay active in the background

⚠ Known Issues
CPU/GPU temperature file paths vary by device — some may show “--” if unavailable

FPS counter may not match 120/144Hz screens exactly

Overlay permission must be manually enabled if denied the first time

📦 Build
Clone this repo and open it in Android Studio:

bash
Copy
Edit
git clone https://github.com/Michaell-android/TempMonitorApp.git
Then build the APK via:
Build → Build Bundle(s) / APK(s) → Build APK(s)

📜 License
This project is licensed under the MIT License — see the LICENSE file for details.

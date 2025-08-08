# TempCheck – Real-time Android overlay for CPU & GPU temps and FPS monitoring

📊 **Keep an eye on your Android’s performance — anywhere, anytime**  

---

## ✨ Features
- **Real-time CPU & GPU temperature monitoring**
- **FPS counter** for games and apps
- **Floating overlay** that stays on top of all apps
- **Toggle control** from the main app screen
- **Lightweight and efficient**, minimal battery usage

---

## 🛠 How It Works
- **Temperatures** are read from `/sys/class/thermal/` device files  
- **FPS** is calculated using frame callbacks  
- Runs as a **foreground service** to keep the overlay active
- Uses `WindowManager` with `TYPE_APPLICATION_OVERLAY` for Android 8.0+

---

## 📲 Installation
1. Download the latest APK from the [Releases](../../releases) page  
2. Install it on your device (enable *Install from unknown sources* if prompted)  
3. Open the app and **grant overlay permission**  
4. Toggle the switch to enable the overlay

---

## 🔑 Permissions
- `SYSTEM_ALERT_WINDOW` — required to draw overlay above other apps  
- `FOREGROUND_SERVICE` — keeps overlay running in the background

---

## ⚠ Known Issues
- CPU/GPU temperature file paths vary by device — may show “--” if unavailable  
- FPS counter may not match exactly on 120Hz/144Hz displays  
- Overlay permission must be manually enabled if denied initially

---

## 📦 Build
Clone the repo and open it in Android Studio:
```bash
git clone https://github.com/Michaell-android/TempMonitorApp.git

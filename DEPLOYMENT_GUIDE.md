# Dynamic Clock App - Deployment Guide

## 🚀 Complete Setup & Deployment Instructions

### Prerequisites

Before starting, ensure you have:
- **Android Studio** (latest version - Arctic Fox or newer)
- **Java JDK 8+** installed
- **Android SDK** with API level 24+ (Android 7.0)
- **Physical Android device** or **Android Emulator**

---

## 📥 Step 1: Install Android Studio

1. **Download Android Studio**
   - Go to [developer.android.com/studio](https://developer.android.com/studio)
   - Download for your operating system (Windows/Mac/Linux)

2. **Install Android Studio**
   - Run the installer
   - Follow setup wizard
   - Install Android SDK, SDK Tools, and Emulator

3. **Configure SDK**
   - Open Android Studio
   - Go to `File → Settings → Appearance & Behavior → System Settings → Android SDK`
   - Install Android API 34 (or latest)
   - Install Android API 24 (minimum required)

---

## 📂 Step 2: Open the Project

1. **Open Android Studio**
2. **Import Project**
   - Click `File → Open`
   - Navigate to your Dynamic Clock project folder
   - Select the root folder (where `build.gradle` is located)
   - Click `OK`

3. **Wait for Project Sync**
   - Android Studio will automatically sync the project
   - Wait for "Gradle sync finished" message
   - This may take a few minutes on first run

---

## 🔑 Step 3: Configure Weather API Key

**IMPORTANT**: You must get a free API key for weather functionality.

1. **Get OpenWeatherMap API Key**
   - Go to [openweathermap.org/api](https://openweathermap.org/api)
   - Click "Sign Up" and create free account
   - Go to "My API Keys" section
   - Copy your API key

2. **Update the Code**
   - In Android Studio, navigate to:
     `app/src/main/java/com/dynamicclock/app/network/WeatherService.kt`
   - Find this line:
     ```kotlin
     const val API_KEY = "YOUR_OPENWEATHER_API_KEY"
     ```
   - Replace with your actual key:
     ```kotlin
     const val API_KEY = "abc123your-actual-api-key-here"
     ```

---

## 🏃‍♂️ Step 4: Run the App

### Option A: Run on Physical Device

1. **Enable Developer Mode on Android Device**
   - Go to `Settings → About Phone`
   - Tap "Build Number" 7 times
   - Go back to Settings → find "Developer Options"
   - Enable "USB Debugging"

2. **Connect Device**
   - Connect Android device via USB
   - Allow USB debugging when prompted
   - Device should appear in Android Studio

3. **Run the App**
   - Click the green "Run" button (▶️) in Android Studio
   - Or press `Shift + F10`
   - Select your device from the dropdown
   - App will install and launch automatically

### Option B: Run on Android Emulator

1. **Create Virtual Device**
   - In Android Studio: `Tools → AVD Manager`
   - Click "Create Virtual Device"
   - Choose a phone (e.g., Pixel 7)
   - Select system image (API 34 recommended)
   - Click "Finish"

2. **Start Emulator**
   - Click the play button next to your virtual device
   - Wait for emulator to boot up

3. **Run the App**
   - Click green "Run" button in Android Studio
   - Select your emulator
   - App will install and launch

---

## 📱 Step 5: Export APK File

### Debug APK (for testing)

1. **Build Debug APK**
   ```bash
   # In Android Studio terminal:
   ./gradlew assembleDebug
   ```
   Or use menu: `Build → Build Bundle(s) / APK(s) → Build APK(s)`

2. **Find APK Location**
   - APK will be generated at:
     `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for distribution)

1. **Generate Signing Key** (first time only)
   - `Build → Generate Signed Bundle / APK`
   - Choose "APK"
   - Click "Create new..."
   - Fill in keystore details:
     - Key store path: Choose location (e.g., `keystore.jks`)
     - Password: Create strong password
     - Alias: `dynamic-clock-key`
     - Key password: Same or different password
     - Certificate info: Fill your details

2. **Build Release APK**
   - `Build → Generate Signed Bundle / APK`
   - Choose "APK"
   - Select your keystore file
   - Enter passwords
   - Choose "release" build variant
   - Click "Finish"

3. **Find Release APK**
   - APK will be at: `app/build/outputs/apk/release/app-release.apk`

---

## 🔧 Step 6: Build Commands (Alternative)

You can also use command line to build:

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing setup)
./gradlew assembleRelease

# Clean and rebuild
./gradlew clean
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

---

## 📋 Step 7: Testing Checklist

Once the app is running, verify:

- ✅ **Time Display**: Shows current time and updates every second
- ✅ **Date Display**: Shows current date
- ✅ **Background Changes**: Changes based on time of day
- ✅ **Location Permission**: App requests location access
- ✅ **Weather Data**: Shows temperature and condition (if API key configured)
- ✅ **Fullscreen Mode**: App runs in immersive fullscreen
- ✅ **Screen On**: Screen stays on while app is active

---

## 🐛 Troubleshooting

### Common Issues:

**1. Gradle Sync Failed**
```bash
# Solution: Clean and rebuild
./gradlew clean
# Then sync again in Android Studio
```

**2. Weather Not Showing**
- Check API key is correct in `WeatherService.kt`
- Ensure location permissions are granted
- Check internet connection
- API key might need activation (wait 10-60 minutes after signup)

**3. Build Errors**
- Ensure Android SDK 34 is installed
- Check Java version (should be 8+)
- Try: `File → Invalidate Caches and Restart`

**4. App Won't Install on Device**
- Enable "Install from Unknown Sources" in Android settings
- Check device has Android 7.0+ (API 24+)
- Try uninstalling previous version first

**5. Location Permission Issues**
- Go to device Settings → Apps → Dynamic Clock → Permissions
- Enable Location permissions manually

---

## 📲 Distribution Options

### Direct Installation
- Send APK file via email/cloud storage
- Install directly on Android devices
- Enable "Install from Unknown Sources"

### Google Play Store (Advanced)
- Create developer account ($25 one-time fee)
- Upload AAB bundle: `Build → Generate Signed Bundle`
- Follow Play Console guidelines

### F-Droid / Other Stores
- Upload APK to alternative app stores
- Open source friendly options

---

## 🎯 Final Tips

1. **First Run**: Always test with API key configured
2. **Permissions**: Grant location access for best experience
3. **Performance**: App is optimized for minimal battery usage
4. **Customization**: Colors can be modified in `colors.xml`
5. **Updates**: Weather updates every 10 minutes automatically

The app is now ready for distribution and will provide a beautiful, living clock wallpaper experience on any Android device! 🎉
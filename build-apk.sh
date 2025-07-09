#!/bin/bash

# Dynamic Clock App - Build Script
# This script helps build and validate the Android project

echo "🚀 Dynamic Clock App - Build Script"
echo "=================================="

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    echo "   (where build.gradle is located)"
    exit 1
fi

echo "✅ Project structure verified"

# Check if gradlew exists and is executable
if [ ! -x "./gradlew" ]; then
    echo "❌ Error: gradlew not found or not executable"
    echo "   Making gradlew executable..."
    chmod +x gradlew
    echo "✅ gradlew is now executable"
fi

# Check for API key configuration
echo ""
echo "🔑 Checking API key configuration..."
if grep -q "YOUR_OPENWEATHER_API_KEY" app/src/main/java/com/dynamicclock/app/network/WeatherService.kt; then
    echo "⚠️  WARNING: Weather API key not configured!"
    echo "   Please update WeatherService.kt with your OpenWeatherMap API key"
    echo "   Get one free at: https://openweathermap.org/api"
else
    echo "✅ API key appears to be configured"
fi

echo ""
echo "🔨 Building debug APK..."

# Clean and build debug APK
echo "   Running: ./gradlew clean"
./gradlew clean
if [ $? -ne 0 ]; then
    echo "❌ Clean failed"
    echo "   This might be normal if you don't have Android SDK installed"
    echo "   Use Android Studio to build instead"
    exit 1
fi

./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

# Check if APK was created
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
    echo "✅ Success! APK built successfully"
    echo "📱 APK location: $APK_PATH"
    echo "📏 APK size: $(du -h "$APK_PATH" | cut -f1)"
else
    echo "❌ APK file not found at expected location"
    exit 1
fi

echo ""
echo "🎉 Build Complete!"
echo ""
echo "Next steps:"
echo "1. Install Android Studio if not already installed"
echo "2. Open this project in Android Studio"
echo "3. Connect an Android device or start emulator"
echo "4. Click the green 'Run' button to install and test"
echo ""
echo "Or install the APK directly:"
echo "  adb install $APK_PATH"
echo ""
echo "For detailed instructions, see DEPLOYMENT_GUIDE.md"
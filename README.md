# Dynamic Clock App for Android

A beautiful, minimalist clock app that displays time, date, and weather with dynamic backgrounds that change based on the time of day and weather conditions.

## Features

### 🕐 Digital Clock Display
- Large, elegant time display
- Real-time updates every second
- 12-hour format with AM/PM indicator
- Beautiful typography with shadows

### 🌅 Dynamic Backgrounds
- **Morning (5AM-12PM)**: Light/warm colors (peach to sky blue)
- **Afternoon (12PM-5PM)**: Bright/sky colors (sky blue to steel blue)
- **Evening (5PM-8PM)**: Sunset/orange colors (coral to tomato)
- **Night (8PM-5AM)**: Dark/cool colors (dark slate gray to midnight blue)

### 🌤️ Weather Integration
- Real-time weather data from OpenWeatherMap API
- Temperature display in Fahrenheit
- Weather condition descriptions
- Weather-based background overlays:
  - **Sunny**: Warm yellow overlay
  - **Rainy**: Dark gray overlay
  - **Cloudy**: Medium gray overlay

### 🎨 Design Features
- Immersive fullscreen experience
- Smooth animated transitions between backgrounds
- Clean, minimal interface
- No clutter - just time, date, and weather
- Screen stays on while app is active

## Setup Instructions

### 1. Get Weather API Key
1. Go to [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Generate an API key
4. Open `app/src/main/java/com/dynamicclock/app/network/WeatherService.kt`
5. Replace `YOUR_OPENWEATHER_API_KEY` with your actual API key:

```kotlin
const val API_KEY = "your_actual_api_key_here"
```

### 2. Build and Run
1. Open the project in Android Studio
2. Make sure you have Android SDK 34 installed
3. Connect an Android device or start an emulator
4. Click "Run" or use `Ctrl+R`

### 3. Permissions
The app will request location permissions on first launch to provide weather data. Grant these permissions for the full experience.

## Project Structure

```
app/
├── src/main/
│   ├── java/com/dynamicclock/app/
│   │   ├── MainActivity.kt              # Main activity with clock logic
│   │   ├── data/
│   │   │   └── WeatherData.kt          # Weather data models
│   │   ├── network/
│   │   │   └── WeatherService.kt       # Weather API service
│   │   └── utils/
│   │       ├── BackgroundManager.kt    # Dynamic background management
│   │       └── LocationManager.kt      # Location services
│   └── res/
│       ├── layout/
│       │   └── activity_main.xml       # Main UI layout
│       ├── values/
│       │   ├── colors.xml              # App colors and gradients
│       │   ├── strings.xml             # String resources
│       │   └── themes.xml              # App themes
│       └── xml/                        # Android config files
```

## Technical Details

### Dependencies
- **Android SDK**: Minimum API 24 (Android 7.0)
- **Kotlin**: 1.9.10
- **Material Design 3**: Modern UI components
- **Retrofit**: HTTP client for weather API
- **Google Play Services**: Location services
- **AndroidX**: Modern Android libraries

### Background System
The app uses a sophisticated background manager that:
- Creates gradient backgrounds based on time of day
- Applies weather-specific color overlays
- Smoothly animates transitions between states
- Updates automatically as time progresses

### Performance
- Efficient time updates using Handler with 1-second intervals
- Weather data cached for 10 minutes to reduce API calls
- Smooth animations using ValueAnimator
- Minimal battery impact with optimized update cycles

## Customization

### Adding New Time Periods
Edit `TimeOfDay.fromHour()` in `WeatherData.kt` to modify time ranges.

### Changing Colors
Update gradient colors in `app/src/main/res/values/colors.xml`:
- `morning_start` / `morning_end`
- `afternoon_start` / `afternoon_end`
- `evening_start` / `evening_end`
- `night_start` / `night_end`

### Weather Overlays
Modify weather overlay colors in `colors.xml`:
- `sunny_overlay`
- `rainy_overlay`
- `cloudy_overlay`

## Screenshots

The app provides a beautiful, living wallpaper experience that changes throughout the day:

- **Morning**: Warm peachy-orange gradients with sky blue accents
- **Afternoon**: Bright sky blue gradients
- **Evening**: Beautiful sunset orange and coral colors
- **Night**: Deep, calming dark blue and slate gray gradients

Weather conditions add subtle overlays that enhance the atmospheric feeling of the current conditions.

## Future Enhancements

- [ ] iOS version
- [ ] Custom font options
- [ ] More weather conditions (snow, fog, etc.)
- [ ] Location-based time zone support
- [ ] Widget support
- [ ] Customizable color schemes
- [ ] Sound effects for transitions

## License

This project is open source. Feel free to use, modify, and distribute as needed.

## Support

For issues or questions about setup, please check that:
1. Your OpenWeatherMap API key is valid
2. Location permissions are granted
3. Device has internet connectivity
4. Android version is 7.0 or higher

The app works great as a bedside clock, desk display, or any time you want a beautiful, functional timepiece on your Android device.
package com.dynamicclock.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.dynamicclock.app.data.TimeOfDay
import com.dynamicclock.app.data.WeatherCondition
import com.dynamicclock.app.databinding.ActivityMainBinding
import com.dynamicclock.app.network.WeatherApi
import com.dynamicclock.app.utils.BackgroundManager
import com.dynamicclock.app.utils.LocationManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var backgroundManager: BackgroundManager
    private lateinit var locationManager: LocationManager
    
    private val handler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateTime()
            handler.postDelayed(this, 1000) // Update every second
        }
    }
    
    private var currentWeatherCondition = WeatherCondition.UNKNOWN
    private var lastWeatherUpdate = 0L
    private val weatherUpdateInterval = 10 * 60 * 1000L // 10 minutes
    
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        
        if (locationGranted) {
            fetchWeatherData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Setup immersive mode
        setupImmersiveMode()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        backgroundManager = BackgroundManager(this)
        locationManager = LocationManager(this)
        
        // Initialize with current time
        updateTime()
        
        // Start the time update loop
        handler.post(timeUpdateRunnable)
        
        // Request location permission and fetch weather
        requestLocationPermissionAndFetchWeather()
        
        // Initial background setup
        updateBackground()
    }
    
    private fun setupImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        // Make the app truly fullscreen
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }
    
    private fun updateTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM)
        
        // Format time
        val timeFormat = if (hour == 0) "12" else hour.toString()
        val minuteFormat = String.format("%02d", minute)
        val timeString = "$timeFormat:$minuteFormat"
        
        binding.timeText.text = timeString
        binding.amPmText.text = if (amPm == Calendar.AM) "AM" else "PM"
        
        // Format date
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        binding.dateText.text = dateFormat.format(calendar.time)
        
        // Update background if time of day changed
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val newTimeOfDay = TimeOfDay.fromHour(currentHour)
        updateBackground(newTimeOfDay)
        
        // Check if we need to update weather
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastWeatherUpdate > weatherUpdateInterval) {
            fetchWeatherData()
        }
    }
    
    private fun updateBackground(timeOfDay: TimeOfDay? = null) {
        val currentTimeOfDay = timeOfDay ?: run {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            TimeOfDay.fromHour(hour)
        }
        
        backgroundManager.updateBackground(
            binding.backgroundView,
            binding.weatherOverlay,
            currentTimeOfDay,
            currentWeatherCondition
        )
    }
    
    private fun requestLocationPermissionAndFetchWeather() {
        if (locationManager.hasLocationPermission()) {
            fetchWeatherData()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    
    private fun fetchWeatherData() {
        lifecycleScope.launch {
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    val response = WeatherApi.service.getCurrentWeather(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        apiKey = WeatherApi.API_KEY
                    )
                    
                    if (response.isSuccessful && response.body() != null) {
                        val weatherResponse = response.body()!!
                        updateWeatherUI(weatherResponse)
                        lastWeatherUpdate = System.currentTimeMillis()
                    }
                }
            } catch (e: Exception) {
                // Handle error silently - weather is optional
                updateWeatherUI(null)
            }
        }
    }
    
    private fun updateWeatherUI(weatherResponse: com.dynamicclock.app.data.WeatherResponse?) {
        if (weatherResponse != null && weatherResponse.weather.isNotEmpty()) {
            val weather = weatherResponse.weather[0]
            val temperature = weatherResponse.main.temp.toInt()
            
            binding.temperatureText.text = "${temperature}°"
            binding.weatherDescription.text = weather.description.replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
            }
            
            currentWeatherCondition = WeatherCondition.fromWeatherMain(weather.main)
            binding.weatherLayout.visibility = View.VISIBLE
        } else {
            // Hide weather info if unavailable
            binding.weatherLayout.visibility = View.GONE
            currentWeatherCondition = WeatherCondition.UNKNOWN
        }
        
        // Update background with new weather condition
        updateBackground()
    }
    
    override fun onResume() {
        super.onResume()
        // Restart the time updates when app resumes
        handler.post(timeUpdateRunnable)
    }
    
    override fun onPause() {
        super.onPause()
        // Stop time updates when app is paused
        handler.removeCallbacks(timeUpdateRunnable)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeUpdateRunnable)
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            setupImmersiveMode()
        }
    }
}
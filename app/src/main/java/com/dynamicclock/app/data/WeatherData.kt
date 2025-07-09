package com.dynamicclock.app.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)

enum class WeatherCondition {
    SUNNY, CLOUDY, RAINY, UNKNOWN;
    
    companion object {
        fun fromWeatherMain(weatherMain: String): WeatherCondition {
            return when (weatherMain.lowercase()) {
                "clear" -> SUNNY
                "clouds" -> CLOUDY
                "rain", "drizzle", "thunderstorm" -> RAINY
                else -> UNKNOWN
            }
        }
    }
}

enum class TimeOfDay {
    MORNING, AFTERNOON, EVENING, NIGHT;
    
    companion object {
        fun fromHour(hour: Int): TimeOfDay {
            return when (hour) {
                in 5..11 -> MORNING
                in 12..16 -> AFTERNOON
                in 17..19 -> EVENING
                else -> NIGHT
            }
        }
    }
}
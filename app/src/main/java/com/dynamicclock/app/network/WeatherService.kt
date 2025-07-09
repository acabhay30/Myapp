package com.dynamicclock.app.network

import com.dynamicclock.app.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): Response<WeatherResponse>
}

object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    
    // You'll need to get a free API key from OpenWeatherMap
    // For demo purposes, using a placeholder
    const val API_KEY = "YOUR_OPENWEATHER_API_KEY"
    
    val service: WeatherService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}
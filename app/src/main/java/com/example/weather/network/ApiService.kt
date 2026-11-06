package com.example.weather.network

import com.example.weather.data.LocationSearchResponse
import com.example.weather.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络服务接口定义
 */
interface GeocodingApi {
    // 地点搜索 API (使用免费 Open-Meteo Geocoding API)
    @GET("search")
    suspend fun searchLocations(
        @Query("name") query: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "zh",
        @Query("format") format: String = "json"
    ): LocationSearchResponse
}

interface WeatherApi {
    // 天气 API (使用免费 Open-Meteo API)
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,precipitation_probability,weathercode",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_probability_max,weathercode",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 7
    ): WeatherResponse
}
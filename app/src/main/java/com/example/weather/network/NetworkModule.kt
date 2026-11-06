package com.example.weather.network

import com.example.weather.data.LocationResult
import com.example.weather.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络客户端配置
 */
object NetworkModule {
    private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/v1/"
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val geocodingRetrofit = Retrofit.Builder()
        .baseUrl(GEOCODING_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val geocodingApi: GeocodingApi = geocodingRetrofit.create(GeocodingApi::class.java)
    val weatherApi: WeatherApi = retrofit.create(WeatherApi::class.java)
}
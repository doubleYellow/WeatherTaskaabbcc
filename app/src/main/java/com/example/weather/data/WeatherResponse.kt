package com.example.weather.data

import com.google.gson.annotations.SerializedName

/**
 * 天气数据模型
 */
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    @SerializedName("hourly")
    val hourly: Hourly? = null,
    @SerializedName("daily")
    val daily: Daily? = null
)

data class CurrentWeather(
    val temperature: Double,
    @SerializedName("windspeed")
    val windSpeed: Double,
    @SerializedName("winddirection")
    val windDirection: Double? = null,
    @SerializedName("weathercode")
    val weatherCode: Int,
    val time: String,
    @SerializedName("is_day")
    val isDay: Int? = null
)

data class Hourly(
    val time: List<String> = listOf(),
    @SerializedName("temperature_2m")
    val temperature2m: List<Double> = listOf(),
    @SerializedName("precipitation_probability")
    val precipitationProbability: List<Int> = listOf(),
    @SerializedName("weathercode")
    val weatherCode: List<Int> = listOf()
)

data class Daily(
    val time: List<String> = listOf(),
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double> = listOf(),
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double> = listOf(),
    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int> = listOf(),
    @SerializedName("weathercode")
    val weatherCode: List<Int> = listOf()
)
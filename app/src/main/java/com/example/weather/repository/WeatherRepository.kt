package com.example.weather.repository

import com.example.weather.data.LocationResult
import com.example.weather.data.WeatherResponse
import com.example.weather.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

/**
 * UI 状态定义
 * 采用密封类模式统一管理加载、成功、错误状态
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

/**
 * 天气与地点数据仓库
 * 负责调用网络接口并提供统一的状态处理
 */
class WeatherRepository {

    private val geocodingApi = NetworkModule.geocodingApi
    private val weatherApi = NetworkModule.weatherApi

    /**
     * 搜索地点
     */
    fun searchLocations(query: String): Flow<UiState<List<LocationResult>>> = flow<UiState<List<LocationResult>>> {
        if (query.isBlank()) {
            emit(UiState.Success(emptyList()))
            return@flow
        }
        emit(UiState.Loading)
        val response = geocodingApi.searchLocations(query)
        emit(UiState.Success(response.results ?: emptyList()))
    }.catch { e ->
        emit(UiState.Error(mapError(e)))
    }.flowOn(Dispatchers.IO)

    /**
     * 获取指定地点的天气
     */
    fun getWeather(lat: Double, lon: Double): Flow<UiState<WeatherResponse>> = flow {
        emit(UiState.Loading)
        val response = weatherApi.getWeather(latitude = lat, longitude = lon)
        emit(UiState.Success(response))
    }.catch { e ->
        emit(UiState.Error(mapError(e)))
    }.flowOn(Dispatchers.IO)

    /**
     * 将异常映射为用户可读的错误信息
     */
    private fun mapError(e: Throwable): String = when (e) {
        is IOException -> "网络连接失败，请检查网络设置"
        is HttpException -> when (e.code()) {
            404 -> "未找到相关数据"
            429 -> "请求过于频繁，请稍后再试"
            500, 502, 503 -> "服务器暂时不可用，请稍后再试"
            else -> "请求失败（HTTP ${e.code()}）"
        }
        else -> "发生未知错误：${e.message}"
    }
}
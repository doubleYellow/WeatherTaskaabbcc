package com.example.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.LocationResult
import com.example.weather.data.WeatherResponse
import com.example.weather.repository.UiState
import com.example.weather.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 主界面的 ViewModel，管理搜索与天气状态
 */
class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _searchResults = MutableStateFlow<UiState<List<LocationResult>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<LocationResult>>> = _searchResults.asStateFlow()

    private val _weather = MutableStateFlow<UiState<WeatherResponse>>(UiState.Loading)
    val weather: StateFlow<UiState<WeatherResponse>> = _weather.asStateFlow()

    private val _currentLocationName = MutableStateFlow<String>("")
    val currentLocationName: StateFlow<String> = _currentLocationName.asStateFlow()

    private var searchJob: Job? = null

    /**
     * 搜索地点（带防抖，避免频繁请求）
     */
    fun searchLocations(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            repository.searchLocations(query).collect { state ->
                _searchResults.value = state
            }
        }
    }

    /**
     * 获取某地点的天气
     */
    fun getWeather(lat: Double, lon: Double, locationName: String = "当前城市") {
        _currentLocationName.value = locationName
        viewModelScope.launch {
            repository.getWeather(lat, lon).collect { state ->
                _weather.value = state
            }
        }
    }

    /**
     * 清空搜索结果
     */
    fun clearSearchResults() {
        searchJob?.cancel()
        _searchResults.value = UiState.Success(emptyList())
    }
}
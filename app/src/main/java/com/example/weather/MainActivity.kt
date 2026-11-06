package com.example.weather

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.adapter.DailyAdapter
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.adapter.LocationAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.repository.UiState
import com.example.weather.util.WeatherCode
import com.example.weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var locationAdapter: LocationAdapter
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter

    private var searchDebounceJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupSearchInput()
        observeViewModel()

        // 默认加载北京天气
        viewModel.getWeather(39.9042, 116.4041, "北京")
    }

    private fun setupRecyclerViews() {
        // 地点搜索结果列表
        locationAdapter = LocationAdapter { location ->
            viewModel.getWeather(location.latitude, location.longitude, location.displayName)
            hideKeyboard()
            viewModel.clearSearchResults()
        }
        binding.recyclerLocations.layoutManager = LinearLayoutManager(this)
        binding.recyclerLocations.adapter = locationAdapter

        // 今日逐小时预报（横向滚动）
        hourlyAdapter = HourlyAdapter()
        binding.recyclerHourly.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerHourly.adapter = hourlyAdapter

        // 未来 7 天预报
        dailyAdapter = DailyAdapter {}
        binding.recyclerDaily.layoutManager = LinearLayoutManager(this)
        binding.recyclerDaily.adapter = dailyAdapter
    }

    private fun setupSearchInput() {
        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim().orEmpty()
            // 输入防抖，避免频繁请求
            searchDebounceJob?.cancel()
            searchDebounceJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchLocations(query)
            }
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text?.toString()?.trim().orEmpty()
                if (query.isNotEmpty()) {
                    viewModel.searchLocations(query)
                    hideKeyboard()
                }
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        // 观察搜索结果状态
        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressSearch.visibility = View.VISIBLE
                        binding.recyclerLocations.visibility = View.GONE
                        binding.tvSearchError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressSearch.visibility = View.GONE
                        binding.tvSearchError.visibility = View.GONE
                        if (state.data.isNotEmpty()) {
                            binding.recyclerLocations.visibility = View.VISIBLE
                            locationAdapter.submitList(state.data)
                        } else {
                            binding.recyclerLocations.visibility = View.GONE
                        }
                    }
                    is UiState.Error -> {
                        binding.progressSearch.visibility = View.GONE
                        binding.recyclerLocations.visibility = View.GONE
                        binding.tvSearchError.visibility = View.VISIBLE
                        binding.tvSearchError.text = state.message
                    }
                }
            }
        }

        // 观察天气状态
        lifecycleScope.launch {
            viewModel.weather.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.weatherLoadingContainer.visibility = View.VISIBLE
                        binding.scrollWeather.visibility = View.GONE
                        binding.tvWeatherError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.weatherLoadingContainer.visibility = View.GONE
                        binding.tvWeatherError.visibility = View.GONE
                        binding.scrollWeather.visibility = View.VISIBLE
                        bindWeatherData(state.data)
                    }
                    is UiState.Error -> {
                        binding.weatherLoadingContainer.visibility = View.GONE
                        binding.scrollWeather.visibility = View.GONE
                        binding.tvWeatherError.visibility = View.VISIBLE
                        binding.tvWeatherError.text = state.message
                    }
                }
            }
        }
    }

    private fun bindWeatherData(data: com.example.weather.data.WeatherResponse) {
        val current = data.currentWeather

        // 当前位置名称
        binding.tvLocationName.text = viewModel.currentLocationName.value

        // 当前温度与天气图标
        binding.tvTemperature.text = WeatherCode.formatTemperature(current.temperature)
        binding.tvWeatherEmoji.text = WeatherCode.emoji(current.weatherCode)
        binding.tvWeatherDesc.text = WeatherCode.description(current.weatherCode)

        // 风力信息
        val direction = current.windDirection
        val dirText = when {
            direction == null -> ""
            direction <= 22.5 || direction >= 337.5 -> "北风"
            direction < 67.5 -> "东北风"
            direction < 112.5 -> "东风"
            direction < 157.5 -> "东南风"
            direction < 202.5 -> "南风"
            direction < 247.5 -> "西南风"
            direction < 292.5 -> "西风"
            else -> "西北风"
        }
        binding.tvWindInfo.text = "风速 ${current.windSpeed.toInt()} km/h" +
                (if (dirText.isNotEmpty()) "，$dirText" else "")

        // 今日逐小时预报
        data.hourly?.let { hourlyAdapter.submitList(it) }

        // 未来 7 天
        data.daily?.let { dailyAdapter.submitList(it) }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
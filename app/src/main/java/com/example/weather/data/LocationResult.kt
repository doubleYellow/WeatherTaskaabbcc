package com.example.weather.data

import com.google.gson.annotations.SerializedName

/**
 * 地点搜索结果数据模型
 */
data class LocationResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @SerializedName("admin1")
    val state: String? = null
) {
    val displayName: String
        get() = buildString {
            append(name)
            state?.let { append(", ").append(it) }
            country?.let { append(", ").append(it) }
        }
}

data class LocationSearchResponse(
    val results: List<LocationResult>? = null
)
package com.example.weather.util

/**
 * WMO 天气代码转换工具
 * 参考 WMO WeatherInterpretation 规范
 */
object WeatherCode {
    private val descriptions = mapOf(
        0 to "晴",
        1 to "大部晴朗",
        2 to "局部多云",
        3 to "阴天",
        45 to "雾",
        48 to "冻雾",
        51 to "小毛毛雨",
        53 to "毛毛雨",
        55 to "大毛毛雨",
        56 to "冻毛毛雨",
        57 to "强冻毛毛雨",
        61 to "小阵雨",
        63 to "阵雨",
        65 to "强阵雨",
        66 to "冻雨",
        67 to "强冻雨",
        71 to "小雪",
        73 to "中雪",
        75 to "大雪",
        77 to "雪粒",
        80 to "小阵雨",
        81 to "阵雨",
        82 to "强阵雨",
        85 to "小阵雪",
        86 to "强阵雪",
        95 to "雷暴",
        96 to "雷暴伴小冰雹",
        99 to "雷暴伴大冰雹"
    )

    fun description(code: Int): String = descriptions[code] ?: "未知天气"

    fun emoji(code: Int): String = when (code) {
        0 -> "☀"
        1 -> "🌤"
        2 -> "⛅"
        3 -> "☁"
        45, 48 -> "🌫"
        51, 53, 55, 56, 57 -> "🌦"
        61, 63, 65, 66, 67 -> "🌧"
        71, 73, 75, 77 -> "🌨"
        80, 81, 82 -> "🌧"
        85, 86 -> "🌨"
        95, 96, 99 -> "⛈"
        else -> "❓"
    }

    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°"
    }

    fun formatDate(dateStr: String): String {
        // Open-Meteo 日期格式为 "2024-06-01T00:00"，提取日期部分
        val date = dateStr.substringBefore("T")
        val parts = date.split("-")
        return if (parts.size == 3) {
            "${parts[1]}月${parts[2]}日"
        } else {
            date
        }
    }

    fun formatDayOfWeek(dateStr: String): String {
        val parts = dateStr.substringBefore("T").split("-")
        return try {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
                java.util.Calendar.MONDAY -> "周一"
                java.util.Calendar.TUESDAY -> "周二"
                java.util.Calendar.WEDNESDAY -> "周三"
                java.util.Calendar.THURSDAY -> "周四"
                java.util.Calendar.FRIDAY -> "周五"
                java.util.Calendar.SATURDAY -> "周六"
                java.util.Calendar.SUNDAY -> "周日"
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun formatTime(timeStr: String): String {
        // Open-Meteo 时间格式为 "2024-06-01T13:00"
        return timeStr.substringAfter("T").take(5)
    }
}
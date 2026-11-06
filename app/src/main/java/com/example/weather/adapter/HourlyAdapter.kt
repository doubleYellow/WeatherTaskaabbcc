package com.example.weather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.Hourly
import com.example.weather.databinding.ItemHourlyBinding
import com.example.weather.util.WeatherCode

/**
 * 逐小时天气预报适配器
 */
class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private var hourly: Hourly? = null

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: Hourly) {
        hourly = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = hourly?.time?.size ?: 0

    inner class HourlyViewHolder(private val binding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val h = hourly ?: return
            if (position >= h.time.size) return

            binding.tvHourlyTime.text = WeatherCode.formatTime(h.time[position])

            val code = h.weatherCode.getOrElse(position) { 0 }
            binding.tvHourlyEmoji.text = WeatherCode.emoji(code)

            val temp = h.temperature2m.getOrElse(position) { 0.0 }
            binding.tvHourlyTemp.text = WeatherCode.formatTemperature(temp)

            val precip = h.precipitationProbability.getOrElse(position) { 0 }
            binding.tvHourlyPrecip.text = if (precip > 0) "💧$precip%" else ""
            binding.tvHourlyPrecip.visibility = if (precip > 0) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
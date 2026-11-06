package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.Daily
import com.example.weather.databinding.ItemDailyBinding
import com.example.weather.util.WeatherCode

/**
 * 每日天气预报列表适配器
 */
class DailyAdapter(
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    private var daily: Daily? = null

    fun submitList(data: Daily) {
        daily = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = daily?.time?.size ?: 0

    inner class DailyViewHolder(private val binding: ItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val d = daily ?: return
            if (position >= d.time.size) return

            val dateStr = d.time[position]
            binding.tvDayOfWeek.text = WeatherCode.formatDayOfWeek(dateStr)
            binding.tvDayDate.text = WeatherCode.formatDate(dateStr)

            val emoji = d.weatherCode.getOrElse(position) { 0 }
            binding.tvDayEmoji.text = WeatherCode.emoji(emoji)

            val maxTemp = d.temperature2mMax.getOrElse(position) { 0.0 }
            val minTemp = d.temperature2mMin.getOrElse(position) { 0.0 }
            binding.tvDayTempRange.text = "${WeatherCode.formatTemperature(minTemp)} / ${WeatherCode.formatTemperature(maxTemp)}"

            val precip = d.precipitationProbabilityMax.getOrElse(position) { 0 }
            if (precip > 0) {
                binding.tvDayPrecip.visibility = android.view.View.VISIBLE
                binding.tvDayPrecip.text = "💧$precip%"
            } else {
                binding.tvDayPrecip.visibility = android.view.View.GONE
            }

            binding.root.setOnClickListener { onDayClick(position) }
        }
    }
}
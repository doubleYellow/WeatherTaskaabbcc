package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.LocationResult
import com.example.weather.databinding.ItemLocationBinding

/**
 * 地点搜索结果列表适配器
 */
class LocationAdapter(
    private val onLocationClick: (LocationResult) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val items = mutableListOf<LocationResult>()

    fun submitList(newItems: List<LocationResult>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationResult) {
            binding.tvPlaceName.text = item.displayName
            binding.root.setOnClickListener { onLocationClick(item) }
        }
    }
}
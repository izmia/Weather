/*
 * Copyright (c) 12/20/19 2:25 PM Evren Coşkun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.weather.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.weather.R
import com.evrencoskun.weather.repository.database.entity.Weather
import com.evrencoskun.weather.util.formatTemperature
import com.evrencoskun.weather.util.getResourceIdForWeatherCondition
import com.evrencoskun.weather.util.getStringForWeatherCondition

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    var weatherList: MutableList<Weather> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.detail_recyclerview_item, parent, false)
        )

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weatherList.isNotEmpty()) {
            // Get city
            holder.data = weatherList[position]

            // Get image icon considering to the weather code
            val imgId = getResourceIdForWeatherCondition(holder.data.weatherCode)
            holder.imageIcon.setImageResource(imgId)

            // create a friendly date text
            //val dateText = DateUtils.getFriendlyDateString(holder.itemView.context, holder.data.date.toLong(), false)
            holder.dateTextView.text = holder.data.date

            // create a description considering to the weather code
            holder.descriptionTextView.text = getStringForWeatherCondition(holder.itemView.context,holder.data.weatherCode)

            // Append °C temperature string
            holder.highTempTextView.text = formatTemperature(holder.itemView.context,holder.data.maxTemp.toDouble())

            // Append °C temperature string
            holder.lowTempTextView.text = formatTemperature(holder.itemView.context,holder.data.minTemp.toDouble())
        }
    }

    fun updateWeatherList(weatherList: List<Weather>) {
        this.weatherList = weatherList.toMutableList()
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var data: Weather
        val imageIcon: ImageView = itemView.findViewById(R.id.weather_icon)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val descriptionTextView: TextView = itemView.findViewById(R.id.weather_description)
        val highTempTextView: TextView = itemView.findViewById(R.id.high_temperature)
        val lowTempTextView: TextView = itemView.findViewById(R.id.low_temperature)
    }
}
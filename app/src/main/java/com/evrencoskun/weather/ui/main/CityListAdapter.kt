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

package com.evrencoskun.weather.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.weather.R
import com.evrencoskun.weather.repository.database.entity.City

class CityListAdapter : RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    var cityList: MutableList<City> = mutableListOf()
    var onClick: ((City) -> Unit)? = null
    var onDeleteClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_recyclerview_item, parent, false)
        )

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (cityList.isNotEmpty()) {
            // Get city
            holder.data = cityList[position]
            // Set name
            holder.cityNameTextView.text = holder.data.name

            // set listeners
            holder.clickListener { onClick?.invoke(holder.data) }
            holder.deleteButtonClickListener { onDeleteClick?.invoke(holder.data.id) }
        }
    }

    fun updateCityList(cityList: List<City>) {
        this.cityList = cityList.toMutableList()
        notifyDataSetChanged()
    }

    fun addCity(city: City) {
        // display the city as a first item of the list.
        this.cityList.add(cityList.size, city)
        notifyDataSetChanged()
    }

    fun removeCity(id: String) {
        // Remove the city
        this.cityList.removeAll { it.id == id }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var data: City
        val cityNameTextView: TextView = itemView.findViewById(R.id.city_textView)
        private val deleteImageButton: ImageButton = itemView.findViewById(R.id.delete_imageButton)


        fun clickListener(callback: () -> Unit) {
            itemView.setOnClickListener { callback() }
            deleteImageButton.setOnClickListener { callback() }
        }

        fun deleteButtonClickListener(callback: () -> Unit) {
            deleteImageButton.setOnClickListener { callback() }
        }
    }
}
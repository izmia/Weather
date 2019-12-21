/*
 * Copyright (c) 12/17/19 10:23 PM Evren Coşkun
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

package com.evrencoskun.weather.repository.database

import androidx.room.*
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(city: City)

    @Query("DELETE FROM city WHERE id = :id")
    suspend fun deleteCity(id: String)

    @Query("SELECT * FROM city WHERE id = :id")
    suspend fun getCity(id: String): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCityList(cityList: List<City>)

    @Query("SELECT * FROM city ORDER BY id DESC")
    suspend fun getCityList(): List<City>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeatherList(weatherList: List<Weather>)

    @Query("DELETE FROM weather WHERE cityId = :cityId")
    suspend fun deleteWeatherList(cityId: String)

    @Transaction
    suspend fun updateWeathers(weatherList: List<Weather>) {
        if (weatherList.isNotEmpty()) {
            deleteWeatherList(weatherList[0].cityId)
        }
        saveWeatherList(weatherList)
    }

    @Transaction
    @Query("SELECT * FROM weather WHERE cityId = :cityId")
    suspend fun getWeatherList(cityId: String): List<Weather>


}
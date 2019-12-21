/*
 * Copyright (c) 12/21/19 3:36 PM Evren Coşkun
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

package com.evrencoskun.weather.mocks.database

import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.Database
import com.evrencoskun.weather.repository.database.DatabaseRepository
import com.evrencoskun.weather.repository.database.WeatherDao
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

class MockDatabaseRepository(private val database: Database = mockDatabase()) :
    DatabaseRepository(database) {

    var getCityListSuccess = true
    var getWeatherListSuccess = true


    companion object {
        val mockDatabase: Database = mock()
        val mockDao: WeatherDao = mock()

        fun mockDatabase(): Database {
            whenever(mockDatabase.weatherDao()).thenReturn(
                mockDao
            )
            return mockDatabase
        }
    }


    override suspend fun getCityList(
        success: suspend (List<City>) -> Repository.CityListRequestResult.Success,
        error: suspend (String) -> Repository.CityListRequestResult.Error
    ): Repository.CityListRequestResult {
        return if (getCityListSuccess)
            success(MockEntity.mockCityList)
        else
            error("Error")
    }


    override suspend fun getWeatherList(
        cityId: String,
        success: suspend (List<Weather>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getWeatherListSuccess)
            success(MockEntity.mockWeatherList)
        else
            error("Error")
    }

    override suspend fun updateWeatherList(weatherList: List<Weather>) {
        database.weatherDao().updateWeathers(weatherList)
    }

    override suspend fun saveCityList(cityList: List<City>) {
        database.weatherDao().saveCityList(cityList)
    }

    override suspend fun saveCity(city: City) {
        database.weatherDao().saveCity(city)
    }

    override suspend fun deleteCity(id: String) {
        database.weatherDao().deleteCity(id)
    }
}
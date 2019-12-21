/*
 * Copyright (c) 12/18/19 11:09 AM Evren Coşkun
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

import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather
import org.koin.standalone.KoinComponent

/**
 * Repository for communicating with the Room database
 */
open class DatabaseRepository constructor(private val database: Database) : KoinComponent {

    open suspend fun getCityList(
        success: suspend (List<City>) -> Repository.CityListRequestResult.Success,
        error: suspend (String) -> Repository.CityListRequestResult.Error
    ): Repository.CityListRequestResult = processData(database.weatherDao().getCityList(), success, error)

    open suspend fun getCity(
        id: String,
        success: suspend (City) -> Repository.RequestResult.Success,
        error: suspend (String) -> Repository.RequestResult.Error
    ): Repository.RequestResult = processData(database.weatherDao().getCity(id), success, error)

    open suspend fun getWeatherList(
        cityId: String,
        success: suspend (List<Weather>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult = processData(database.weatherDao().getWeatherList(cityId), success, error)

    open suspend fun updateWeatherList(weatherList: List<Weather>) {
        database.weatherDao().updateWeathers(weatherList)
    }

    open suspend fun saveCityList(cityList: List<City>) {
        database.weatherDao().saveCityList(cityList)
    }

    open suspend fun saveCity(city: City) {
        database.weatherDao().saveCity(city)
    }

    open suspend fun deleteCity(id: String) {
        database.weatherDao().deleteCity(id)
    }


    /**
     * Higher-Order Function that takes two arguments of types [R] and [T] and returns a value of type [R].
     * It allows to return proper function considering the params.
     */
    suspend fun <T, R> processData(
        // First param is data whose type is T.
        data: T,
        // Second param is a function whose parameter is T data and its return type is R.
        success: suspend (T) -> R,
        // Third param is also function that has a String parameter and its return type is also R.
        error: suspend (String) -> R
    ): R {
        return if (data != null) {
            // if data is valid, call and return success function with data.
            success(data)
        } else {
            // if not, call and return error function with error message.
            // ${javaClass.simpleName} gives the class name.
            error("${javaClass.simpleName}: Load from Database Error")
        }
    }
}
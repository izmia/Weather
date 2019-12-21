/*
 * Copyright (c) 12/17/19 5:28 PM Evren Coşkun
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

package com.evrencoskun.weather.repository

import com.evrencoskun.weather.repository.database.DatabaseRepository
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather
import com.evrencoskun.weather.repository.network.NetworkRepository
import com.evrencoskun.weather.util.CoroutineContextProvider
import com.evrencoskun.weather.util.combineErrorMessages
import com.evrencoskun.weather.util.generateWeatherList
import com.evrencoskun.weather.util.getDefaultCityList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

/**
 * Repository used to fetch data from either the database or the network.
 *
 * The general rule is that if data cannot be found in the database, it should resort to a network call and
 * finally an error.
 *
 * All requests for data come from the database to ensure a single source of truth.
 */
open class Repository constructor(
    private val databaseRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository,
    private val contextPool: CoroutineContextProvider
) : CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = contextPool.io


    open suspend fun getCityList(): CityListRequestResult {
        //TODO: looks ugly
        // First get city list from local database
        return databaseRepository.getCityList({
            var list = it
            // if this is the first time to open the app then get the default city list.
            if (it.isEmpty()) {
                databaseRepository.saveCityList(getDefaultCityList())

                // Call it again to get saved data set
                databaseRepository.getCityList({
                    list = it
                    CityListRequestResult.Success(it)
                }) {
                    CityListRequestResult.Error(it)
                }
            }

            CityListRequestResult.Success(list)
        }) {
            CityListRequestResult.Error(it)
        }
    }

    open suspend fun addCity(city: City): AddCityRequestResult {

        // Save it
        databaseRepository.saveCity(city)
        // Get city list from local db
        var cityListFromDb: List<City>? = null
        val getCityResult = databaseRepository.getCityList({
            cityListFromDb = it

            CityListRequestResult.Success(it)
        }) {
            CityListRequestResult.Error(it)
        }

        // Checking whether there is something wrong.
        if (getCityResult is CityListRequestResult.Error) {
            return AddCityRequestResult.Error(getCityResult.message)
        }

        // The city shouldn't be null.
        if (cityListFromDb == null) {
            return AddCityRequestResult.Error("Adding a city process has been failed.")
        }

        return AddCityRequestResult.Success(cityListFromDb!!)
    }

    open suspend fun removeCity(cityId: String): RemoveCityRequestResult {

        // Delete the city from local db
        databaseRepository.deleteCity(cityId)

        // Get city list from local db
        var cityListFromDb: List<City>? = null
        val getCityResult = databaseRepository.getCityList({
            cityListFromDb = it

            CityListRequestResult.Success(it)
        }) {
            CityListRequestResult.Error(it)
        }

        // Checking whether there is something wrong.
        if (getCityResult is CityListRequestResult.Error) {
            return RemoveCityRequestResult.Error(getCityResult.message)
        }

        // The city shouldn't be null.
        if (cityListFromDb == null) {
            return RemoveCityRequestResult.Error("Removing a city process has been failed.")
        }

        // Otherwise everything is fine.
        return RemoveCityRequestResult.Success(cityListFromDb!!)
    }

    /**
     * This function get weather list from server, then save these on Local Database.
     * And gets weather list from local database.
     */
    suspend fun getWeatherList(city: City): WeatherListRequestResult {
        val updateResult = updateWeatherListAsync(city).await()

        // The result has an error
        if (updateResult is RequestResult.Error) {
            return WeatherListRequestResult.Error(updateResult.message)
        }

        return getWeatherListFromLocal(city)
    }

    private suspend fun updateWeatherListAsync(city: City) = async {
        // Get Weather list from server
        networkRepository.getWeather(city.name, {
            // if there is something wrong return error message.
            if (!it.data.error.isNullOrEmpty()) {
                RequestResult.Error(combineErrorMessages(it.data.error))
            }

            // prepare proper weather date before saving up.
            val list = generateWeatherList(it, city)
            // Save new data set in local database before sending result
            databaseRepository.updateWeatherList(list)
            RequestResult.Success
        }) {
            RequestResult.Error(it)
        }
    }

    /**
     * This function helps to get weather list from local database.
     */
    private suspend fun getWeatherListFromLocal(city: City): WeatherListRequestResult {
        var list: List<Weather>? = null
        val localResult = databaseRepository.getWeatherList(city.id, {
            list = it
            RequestResult.Success
        }) { RequestResult.Error(it) }

        if (localResult is RequestResult.Error) {
            return WeatherListRequestResult.Error(localResult.message)
        }

        if(list!!.isEmpty()){
            return WeatherListRequestResult.Error("${city.name} is not proper value to get weather list.")
        }

        // Instead of checking null check, throw NullPointerException if the list is null.
        return WeatherListRequestResult.Success(list!!)
    }


    /**
     * Result class before getting Weather which is combined data structures with ApiResponse inner classes etc.
     */
    sealed class RequestResult {
        object Success : RequestResult()
        data class Error(val message: String) : RequestResult()
    }

    sealed class AddCityRequestResult {
        data class Success(val cityList: List<City>) : AddCityRequestResult()
        data class Error(val message: String) : AddCityRequestResult()
    }

    sealed class RemoveCityRequestResult {
        data class Success(val cityList: List<City>) : RemoveCityRequestResult()
        data class Error(val message: String) : RemoveCityRequestResult()
    }

    sealed class CityListRequestResult {
        data class Success(val cityList: List<City>) : CityListRequestResult()
        data class Error(val message: String) : CityListRequestResult()
    }

    sealed class WeatherListRequestResult {
        data class Success(val weatherList: List<Weather>) : WeatherListRequestResult()
        data class Error(val message: String) : WeatherListRequestResult()
    }
}

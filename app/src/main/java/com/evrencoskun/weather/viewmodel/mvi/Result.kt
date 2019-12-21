/*
 * Copyright (c) 12/17/19 7:15 PM Evren Coşkun
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

package com.evrencoskun.weather.viewmodel.mvi

import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather
import com.evrencoskun.weather.viewmodel.mvi.base.Interpreter

sealed class Result {
    data class GetCityListResult(val list: List<City>) : Result()
    data class GetCityListError(val message: String) : Result()
    object GetCityListLoading : Result()

    data class AddCityActionResult(val list: List<City>) : Result()
    data class AddCityActionError(val message: String) : Result()
    object AddCityListLoading : Result()

    data class RemoveCityActionResult(val list: List<City>) : Result()
    data class RemoveCityActionError(val message: String) : Result()
    object RemoveCityListLoading : Result()

    data class NavigateToDetails (val city: City) : Result()
    data class GetWeatherListResult(val weatherList: List<Weather>) : Result()
    data class GetWeatherListError(val message: String) : Result()



    /**
     * Maps [Result]s to [State]s
     *
     * Note that same results end up with the same state, namely [State.Error]
     */
    class ResultInterpreter : Interpreter<Result, State>() {
        override suspend fun interpret(input: Result, callback: suspend (State) -> Unit) {
            callback(
                when(input) {
                    is GetCityListResult -> State.CityListLoaded(input.list)
                    is GetCityListError -> State.Error(input.message)
                    is GetCityListLoading -> State.CityListLoading

                    is AddCityActionResult -> State.CityAdded(input.list)
                    is AddCityActionError -> State.Error(input.message)
                    is AddCityListLoading -> State.CityListLoading

                    is RemoveCityActionResult -> State.CityRemoved(input.list)
                    is RemoveCityActionError -> State.Error(input.message)
                    is RemoveCityListLoading -> State.CityListLoading

                    is NavigateToDetails -> State.WeatherListLoading(input.city)
                    is GetWeatherListResult -> State.WeatherListLoaded(input.weatherList)
                    is GetWeatherListError -> State.Error(input.message)
                }
            )
        }
    }
}
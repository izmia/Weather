/*
 * Copyright (c) 12/17/19 6:48 PM Evren Coşkun
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

import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.viewmodel.mvi.base.Interpreter

/**
 * Action which signifies the action the Activity should take upon receiving an Intent
 */
sealed class Action {
    object GetCityListAction : Action()
    data class AddCityAction(val city: City) : Action()
    data class RemoveCityAction(val cityId: String) : Action()
    data class GetWeatherAction(val city: City) : Action()
    object GetCityListActionWithoutLoading : Action()


    class ActionInterpreter(private val repository: Repository) : Interpreter<Action, Result>() {
        override suspend fun interpret(input: Action, callback: suspend (Result) -> Unit) {
            callback(
                when (input) {

                    is GetCityListAction -> {
                        callback(Result.GetCityListLoading)
                        getCityListActionToResult(repository.getCityList())
                    }
                    is AddCityAction -> {
                        callback(Result.AddCityListLoading)
                        addActionToResult(repository.addCity(input.city))
                    }
                    is RemoveCityAction -> {
                        callback(Result.RemoveCityListLoading)
                        removeActionToResult(repository.removeCity(input.cityId))
                    }
                    is GetWeatherAction -> {
                        // Show Loading
                        callback(Result.GetCityListLoading)
                        val result = getWeatherActionToResult(repository.getWeatherList(input.city))

                        // if city value is not proper then don't need to navigate to detail fragment
                        if(result is Result.GetWeatherListResult){
                            callback(Result.NavigateToDetails(input.city))
                        }

                        result
                    }
                    is GetCityListActionWithoutLoading -> getCityListActionToResult(repository.getCityList())
                }
            )
        }

        private fun getCityListActionToResult(result: Repository.CityListRequestResult): Result {
            return when (result) {
                is Repository.CityListRequestResult.Success -> Result.GetCityListResult(result.cityList)
                is Repository.CityListRequestResult.Error -> Result.GetCityListError(result.message)
            }
        }

        private fun addActionToResult(result: Repository.AddCityRequestResult): Result {
            return when (result) {
                is Repository.AddCityRequestResult.Success -> Result.AddCityActionResult(result.cityList)
                is Repository.AddCityRequestResult.Error -> Result.AddCityActionError(result.message)
            }
        }

        private fun removeActionToResult(result: Repository.RemoveCityRequestResult): Result {
            return when (result) {
                is Repository.RemoveCityRequestResult.Success -> Result.RemoveCityActionResult(result.cityList)
                is Repository.RemoveCityRequestResult.Error -> Result.RemoveCityActionError(result.message)
            }
        }

        private fun getWeatherActionToResult(result: Repository.WeatherListRequestResult): Result {
            return when (result) {
                is Repository.WeatherListRequestResult.Success -> Result.GetWeatherListResult(result.weatherList)
                is Repository.WeatherListRequestResult.Error -> Result.GetWeatherListError(result.message)
            }
        }
    }
}
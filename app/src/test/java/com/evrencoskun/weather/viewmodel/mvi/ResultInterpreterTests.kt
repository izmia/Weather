/*
 * Copyright (c) 12/21/19 5:54 PM Evren Coşkun
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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.mocks.database.MockEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class ResultInterpreterTests {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val resultInterpreter = Result.ResultInterpreter()

    /*
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
     */


    @Test
    fun `when get city list results is passed, city list loaded state should be returned`() {
        runBlocking {
            val callback: suspend (State) -> Unit = { state ->
                Assert.assertEquals(State.CityListLoaded(MockEntity.mockCityList), state)
            }
            resultInterpreter.interpret(Result.GetCityListResult(MockEntity.mockCityList), callback)
        }
    }

    @Test
    fun `when add city action results is passed, city added state should be returned`() {
        runBlocking {
            val callback: suspend (State) -> Unit = { state ->
                Assert.assertEquals(State.CityAdded(MockEntity.mockCityList), state)
            }
            resultInterpreter.interpret(Result.AddCityActionResult(MockEntity.mockCityList), callback)
        }
    }

    @Test
    fun `when remove city list results is passed, city removed state should be returned`() {
        runBlocking {
            val callback: suspend (State) -> Unit = { state ->
                Assert.assertEquals(State.CityRemoved(MockEntity.mockCityList), state)
            }
            resultInterpreter.interpret(Result.RemoveCityActionResult(MockEntity.mockCityList), callback)
        }
    }

    @Test
    fun `when get weather list results is passed, weather list loaded state should be returned`() {
        runBlocking {
            val callback: suspend (State) -> Unit = { state ->
                Assert.assertEquals(State.WeatherListLoaded(MockEntity.mockWeatherList), state)
            }
            resultInterpreter.interpret(Result.GetWeatherListResult(MockEntity.mockWeatherList), callback)
        }
    }

    // TODO: Test the remaining Result -> State functions.

}
/*
 * Copyright (c) 12/21/19 3:28 PM Evren Coşkun
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

package com.evrencoskun.weather.mocks

import com.evrencoskun.weather.mocks.database.MockEntity
import com.evrencoskun.weather.repository.Repository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking

/**
 * Holds a mocked repository and sets up callbacks used to mock repository calls
 */
class MockRepositoryWrapper {

    val repository = mock<Repository>()

    init {
        mockWeather()
    }

    private fun mockWeather() {
        runBlocking {
            whenever(repository.getCityList()).thenReturn(Repository.CityListRequestResult.Success(MockEntity.mockCityList))
            whenever(repository.addCity(any())).thenReturn(Repository.AddCityRequestResult.Success(MockEntity.mockCityList))
            whenever(repository.removeCity(any())).thenReturn(Repository.RemoveCityRequestResult.Success(MockEntity.mockCityList))
            whenever(repository.getWeatherList(any())).thenReturn(Repository.WeatherListRequestResult.Success(MockEntity.mockWeatherList))
        }
    }
}
/*
 * Copyright (c) 12/23/19 4:59 PM Evren Coşkun
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

import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather

class MockEntityEnst {
    companion object{
        val mockCity = City(name = "Istanbul")
        val mockCityList = createMockCityList()
        val mockWeatherList = createMockWeatherList()

        private fun createMockCityList(): List<City> {
            return listOf(
                City(name = "London"),
                City(name = "Istanbul"),
                City(name = "Barcelona")
            )
        }

        private fun createMockWeatherList(): List<Weather> {
            return listOf(
                Weather(
                    cityId = mockCity.id,
                    weatherCode = 300,
                    minTemp = 30,
                    maxTemp = 20,
                    date = "20-12-2019"
                ),
                Weather(
                    cityId = mockCity.id,
                    weatherCode = 300,
                    minTemp = 30,
                    maxTemp = 20,
                    date = "20-13-2019"
                )
            )
        }
    }
}
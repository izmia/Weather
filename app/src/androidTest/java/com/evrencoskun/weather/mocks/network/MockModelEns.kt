/*
 * Copyright (c) 12/23/19 5:02 PM Evren Coşkun
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

package com.evrencoskun.weather.mocks.network

import com.evrencoskun.weather.repository.network.model.*

class MockModelEns {
    companion object {
        val mockApiResponse =
            createMockApiResponse()
        val mockWeatherList =
            createMockWeatherList()
        val mockHourlyList =
            createMockHourlyList()
        val mockErrorList =
            createMockErrorList()

        private fun createMockErrorList(): List<Error> {
            return listOf(
                Error("Error 1"),
                Error("Error 2")
            )
        }

        private fun createMockApiResponse(): ApiResponse {
            return ApiResponse(Data(
                mockWeatherList,
                mockErrorList
            ))
        }

        private fun createMockWeatherList(): List<Weather> {
            return listOf(
                Weather("21,12,2019", 30, 20,
                    mockHourlyList
                ),
                Weather("22,12,2019", 30, 20,
                    mockHourlyList
                ),
                Weather("23,12,2019", 30, 20,
                    mockHourlyList
                )
            )
        }

        private fun createMockHourlyList(): List<Hourly> {
            return listOf(
                Hourly(450),
                Hourly(500),
                Hourly(600)
            )
        }
    }
}
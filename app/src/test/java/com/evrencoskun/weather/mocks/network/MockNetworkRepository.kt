/*
 * Copyright (c) 12/21/19 3:43 PM Evren Coşkun
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

import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.network.NetworkRepository
import com.evrencoskun.weather.repository.network.model.ApiResponse
import com.nhaarman.mockitokotlin2.mock

class MockNetworkRepository : NetworkRepository(mock()) {

    var getWeatherListSucces = true

    override suspend fun getWeather(
        cityName: String,
        success: suspend (ApiResponse) -> Repository.RequestResult,
        error: (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getWeatherListSucces)
            success(MockModel.mockApiResponse)
        else
            error("Error")
    }


}

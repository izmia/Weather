/*
 * Copyright (c) 12/23/19 4:55 PM Evren Coşkun
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

import com.evrencoskun.weather.mocks.network.MockModelEns
import com.evrencoskun.weather.repository.network.WeatherService
import com.evrencoskun.weather.repository.network.model.ApiResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response

class MockJsonPlaceOrderService : WeatherService {

    var getWeatherSuccess = true

    override fun getWeather(
        q: String,
        key: String,
        format: String,
        num_of_day: String
    ): Deferred<Response<ApiResponse>> {
        return if (getWeatherSuccess)
            CompletableDeferred(Response.success(MockModelEns.mockApiResponse))
        else
            CompletableDeferred(Response.error(404, ResponseBody.create(null, "error")))
    }

}
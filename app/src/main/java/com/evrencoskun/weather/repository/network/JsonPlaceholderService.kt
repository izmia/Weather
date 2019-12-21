/*
 * Copyright (c) 12/17/19 9:05 PM Evren Coşkun
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

package com.evrencoskun.weather.repository.network

import com.evrencoskun.weather.repository.network.model.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Sample link
 * http://api.worldweatheronline.com/premium/v1/weather.ashx?key=0cbb7a26bd914b119c434021192711&q=London&format=json&num_of_days=5
 */
interface JsonPlaceholderService {
    companion object {
        const val BASE_URL = "http://api.worldweatheronline.com"
        // Keys
        const val URL_PREFIX = "/premium/v1/weather.ashx/"
        const val KEY = "key"
        const val Q = "q"
        const val FORMAT = "format"
        const val NUM_OF_DAYS = "num_of_days"
        // Defaults values
        const val JSON_FORMAT = "json"
        const val TOKEN = "0cbb7a26bd914b119c434021192711"
        const val DEFAULT_NUM_OF_DAYS = "5"
    }


    @GET(URL_PREFIX)
    fun getWeather(
        @Query(Q) q: String,
        @Query(KEY) key: String = TOKEN,
        @Query(FORMAT) format: String = JSON_FORMAT,
        @Query(NUM_OF_DAYS) num_of_day: String = DEFAULT_NUM_OF_DAYS
    ): Deferred<Response<ApiResponse>>

}
/*
 * Copyright (c) 12/17/19 9:26 PM Evren Coşkun
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

import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.network.model.ApiResponse
import org.koin.standalone.KoinComponent
import retrofit2.Response

/**
 * Repository for communicating with the Network via the JsonPlaceHolder API
 */
open class NetworkRepository constructor(private val service: WeatherService) : KoinComponent {

    open suspend fun getWeather(
        cityName: String,
        success: suspend (ApiResponse) -> Repository.RequestResult,
        error: (String) -> Repository.RequestResult
    ): Repository.RequestResult = processData(service.getWeather(cityName), success, error)


    /**
     * Higher-Order Function that takes two arguments of types [R] and [T] and returns a value of type [R].
     * It allows to return proper function considering the params.
     */
    suspend fun <T, R> processData(
        // First param is Retrofit response object of Corountine.
        response: Response<T>,
        // Second param is a function whose parameter is T data and its return type is R.
        success: suspend (T) -> R,
        // Third param is also function that has a String parameter and its return type is also R.
        error: (String) -> R
    ): R {

        try {
            // Check whether getting data process is successful or not.
            if (response.data.isSuccessful) {
                val body = response.body()

                // 204 is the Http response code for "no content"
                if (body == null || response.code() == 204) {
                    error("${javaClass.simpleName}: Data is empty.")
                } else if (response.code() == 401) {
                    // 401 is the Http response code for the client side error
                    error("${javaClass.simpleName}: 401 Unauthorized. Token may be invalid.")
                } else {
                    success(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMessage = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return error(errorMsg ?: "${javaClass.simpleName}: Fetch Data Unsuccessful")
            }
        } catch (e: Exception) {
            // If something goes wrong, call & return error function
            return error("${javaClass.simpleName}: ${e.message}")
        }
    }
}
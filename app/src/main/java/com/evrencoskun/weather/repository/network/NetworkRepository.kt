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
import kotlinx.coroutines.Deferred
import org.koin.standalone.KoinComponent
import retrofit2.Response

/**
 * Repository for communicating with the Network via the JsonPlaceHolder API
 */
open class NetworkRepository constructor(private val service: JsonPlaceholderService) : KoinComponent {

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
        // First param is Retrofit response within 'Deffered' object of Corountine.
        deferredData: Deferred<Response<T>>,
        // Second param is a function whose parameter is T data and its return type is R.
        success: suspend (T) -> R,
        // Third param is also function that has a String parameter and its return type is also R.
        error: (String) -> R
    ): R {
        val data: Response<T>

        try {
            // Wait for the whole data.
            data = deferredData.await()
        } catch (e: Exception) {
            // If something goes wrong, call & return error function
            return error("${javaClass.simpleName}: ${e.message}")
        }

        val body = data.body()

        // Check whether getting data process is successful or not.
        return if (data.isSuccessful && body != null) {
            // if it is call & return success function with 'body' parameter.
            success(body)
        } else {
            // if not, call and return error function with error message.
            // ${javaClass.simpleName} gives the class name.
            error("${javaClass.simpleName}: Fetch Data Unsuccessful")
        }
    }
}
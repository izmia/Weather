/*
 * Copyright (c) 12/21/19 4:13 PM Evren Coşkun
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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.mocks.database.MockEntity
import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.network.model.ApiResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class NetworkRepositoryTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private val service = mock<JsonPlaceholderService>()
    private val networkRepository = NetworkRepository(service)
    private val apiResponse = mock<ApiResponse>()


    @Test
    fun `when processing network data which is getting weather list in successfully, call success `() {
        runBlocking {
            whenever(service.getWeather(MockEntity.mockCity.id)).thenReturn(
                CompletableDeferred((Response.success(apiResponse)))
            )

            val result = networkRepository.processData(
                service.getWeather(MockEntity.mockCity.id),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            Assert.assertEquals(Repository.RequestResult.Success, result)
        }
    }


    @Test
    fun `when processing database data which is fetched unsuccessfully, call error`() {

        whenever(service.getWeather(MockEntity.mockCity.id)).thenReturn(
            CompletableDeferred((Response.error(404, ResponseBody.create(null, ""))))
        )

        runBlocking {
            val result = networkRepository.processData(
                service.getWeather(MockEntity.mockCity.id),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            Assert.assertEquals(Repository.RequestResult.Error("NetworkRepository: Fetch Data Unsuccessful"), result)
        }
    }
}
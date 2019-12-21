/*
 * Copyright (c) 12/21/19 4:20 PM Evren Coşkun
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

package com.evrencoskun.weather.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.mocks.database.MockDatabaseRepository
import com.evrencoskun.weather.mocks.database.MockEntity
import com.evrencoskun.weather.mocks.network.MockNetworkRepository
import com.evrencoskun.weather.util.CoroutineContextProviderTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class RepositoryTest{

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: Repository

    private var databaseRepository = MockDatabaseRepository()
    private var networkRepository = MockNetworkRepository()

    @Before
    fun setup() {
        databaseRepository = MockDatabaseRepository()
        networkRepository = MockNetworkRepository()

        repository = Repository(databaseRepository, networkRepository, CoroutineContextProviderTest())
    }

    @Test
    fun `when getting city list and no errors, return a city list `() {
        runBlocking {
            val result = repository.getCityList()
            Assert.assertEquals(Repository.CityListRequestResult.Success(MockEntity.mockCityList), result)
        }
    }

    @Test
    fun `when getting city list returns error, return that error`() {
        databaseRepository.getCityListSuccess = false
        runBlocking {
            val result = repository.getCityList()
            Assert.assertEquals(Repository.CityListRequestResult.Error("Error"), result)
        }
    }

    // TODO: Test the remaining cases
}
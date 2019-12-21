/*
 * Copyright (c) 12/21/19 4:07 PM Evren Coşkun
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

package com.evrencoskun.weather.repository.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.entity.City
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class DatabaseRepositoryTest{
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database = mock<Database>()
    private val weatherDao = mock<WeatherDao>()
    private val databaseRepository = DatabaseRepository(database)
    private val city = mock<City>()

    @Before
    fun setup() {
        whenever(database.weatherDao()).thenReturn(weatherDao)
    }

    @Test
    fun `when processing database data which is fetched successfully, call success`() {

        runBlocking {
            whenever(weatherDao.getCityList()).thenReturn(listOf(city))

            val result = databaseRepository.processData(
                database.weatherDao().getCityList(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            Assert.assertEquals(Repository.RequestResult.Success, result)
        }
    }

    @Test
    fun `when processing database data which is fetched unsuccessfully, call error`() {

        runBlocking {
            whenever(weatherDao.getCityList()).thenReturn(null)

            val result = databaseRepository.processData(
                database.weatherDao().getCityList(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            Assert.assertEquals(Repository.RequestResult.Error("DatabaseRepository: Load from Database Error"), result)
        }
    }
}
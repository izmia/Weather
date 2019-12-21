/*
 * Copyright (c) 12/21/19 4:35 PM Evren Coşkun
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

package com.evrencoskun.weather.viewmodel.mvi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.mocks.MockRepositoryWrapper
import com.evrencoskun.weather.mocks.database.MockEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class ActionInterpreterTests {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var actionInterpreter: Action.ActionInterpreter
    private val repositoryWrapper = MockRepositoryWrapper()

    @Before
    fun setup() {
        actionInterpreter = Action.ActionInterpreter(repositoryWrapper.repository)
    }

    @Test
    fun `When get city list action is passed, get city list action results should show loading and pass results`() {
        runBlocking {

            var timesCalled = 0

            val callback: suspend (Result) -> Unit = { result ->
                timesCalled += 1
                if (timesCalled == 1) {
                    Assert.assertEquals(Result.GetCityListLoading, result)
                } else {
                    Assert.assertEquals(Result.GetCityListResult(MockEntity.mockCityList), result)
                }
            }

            actionInterpreter.interpret(Action.GetCityListAction, callback)
        }
    }

    @Test
    fun `When add city action is passed, add city action results should show loading and pass results`() {
        runBlocking {

            var timesCalled = 0

            val callback: suspend (Result) -> Unit = { result ->
                timesCalled += 1
                if (timesCalled == 1) {
                    Assert.assertEquals(Result.AddCityListLoading, result)
                } else {
                    Assert.assertEquals(Result.AddCityActionResult(MockEntity.mockCityList), result)
                }
            }

            actionInterpreter.interpret(Action.AddCityAction(MockEntity.mockCity), callback)
        }
    }

    @Test
    fun `When remove city action is passed, remove city action results should show loading and pass results`() {
        runBlocking {

            var timesCalled = 0

            val callback: suspend (Result) -> Unit = { result ->
                timesCalled += 1
                if (timesCalled == 1) {
                    Assert.assertEquals(Result.RemoveCityListLoading, result)
                } else {
                    Assert.assertEquals(Result.RemoveCityActionResult(MockEntity.mockCityList), result)
                }
            }

            actionInterpreter.interpret(Action.RemoveCityAction(MockEntity.mockCity.id), callback)
        }
    }


    @Test
    fun `When get weather list without loading action is passed, get city list action result should be passed as result`() {
        runBlocking {
            val callback: suspend (Result) -> Unit = {
                Assert.assertEquals(Result.GetCityListResult(MockEntity.mockCityList), it)
            }
            actionInterpreter.interpret(Action.GetCityListActionWithoutLoading, callback)
        }
    }
}
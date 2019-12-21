/*
 * Copyright (c) 12/21/19 4:27 PM Evren Coşkun
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
import com.evrencoskun.weather.mocks.database.MockEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class IntentInterpreterTests {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val intentInterpreter = Intent.IntentInterpreter()

    @Test
    fun `When sending an initial intent, a get city list action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.GetCityListAction, action)
            }
            intentInterpreter.interpret(Intent.InitialIntent, callback)
        }
    }

    @Test
    fun `When sending an add city intent, a add city action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.AddCityAction(MockEntity.mockCity), action)
            }
            intentInterpreter.interpret(Intent.AddCityIntent(MockEntity.mockCity), callback)
        }
    }


    @Test
    fun `When sending a remove city intent, a remove city action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.RemoveCityAction(MockEntity.mockCity.id), action)
            }
            intentInterpreter.interpret(Intent.RemoveCityIntent(MockEntity.mockCity.id), callback)
        }
    }

    @Test
    fun `When sending select city intent, a get weather action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.GetWeatherAction(MockEntity.mockCity), action)
            }
            intentInterpreter.interpret(Intent.SelectCityIntent(MockEntity.mockCity), callback)
        }
    }

    @Test
    fun `When sending refresh detail intent, a get weather action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.GetWeatherAction(MockEntity.mockCity), action)
            }
            intentInterpreter.interpret(Intent.RefreshDetailIntent(MockEntity.mockCity), callback)
        }
    }

    @Test
    fun `When sending leave detail intent, a get city list without loading action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = { action ->
                Assert.assertEquals(Action.GetCityListActionWithoutLoading, action)
            }
            intentInterpreter.interpret(Intent.LeaveDetailIntent, callback)
        }
    }
}
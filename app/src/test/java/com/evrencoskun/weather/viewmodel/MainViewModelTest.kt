/*
 * Copyright (c) 12/21/19 4:26 PM Evren Coşkun
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

package com.evrencoskun.weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evrencoskun.weather.mocks.MockRepositoryWrapper
import com.evrencoskun.weather.mocks.database.MockEntity
import com.evrencoskun.weather.util.CoroutineContextProviderTest
import com.evrencoskun.weather.viewmodel.mvi.Action
import com.evrencoskun.weather.viewmodel.mvi.Intent
import com.evrencoskun.weather.viewmodel.mvi.Result
import com.evrencoskun.weather.viewmodel.mvi.State
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class MainViewModelTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val repositoryWrapper = MockRepositoryWrapper()
    private val intentInterpreter = Intent.IntentInterpreter()
    private val actionInterpreter = Action.ActionInterpreter(repositoryWrapper.repository)
    private val resultInterpreter = Result.ResultInterpreter()
    private val testContextProvider = CoroutineContextProviderTest()

    @Before
    fun setup() {
        viewModel = MainViewModel(intentInterpreter, actionInterpreter, resultInterpreter, testContextProvider)
    }

    @Test
    fun `when sending initial intent, get city list loaded state`() {
        viewModel.sendIntent(Intent.InitialIntent)
        viewModel.state.observeForever {
            Assert.assertEquals(State.CityListLoaded(MockEntity.mockCityList), it)
        }
    }

    @Test
    fun `when sending add city intent, get city added state`() {
        viewModel.sendIntent(Intent.AddCityIntent(MockEntity.mockCity))
        viewModel.state.observeForever {
            Assert.assertEquals(State.CityAdded(MockEntity.mockCityList), it)
        }
    }

    @Test
    fun `when sending remove city intent, get city removed state`() {
        viewModel.sendIntent(Intent.RemoveCityIntent(MockEntity.mockCity.id))
        viewModel.state.observeForever {
            Assert.assertEquals(State.CityRemoved(MockEntity.mockCityList), it)
        }
    }

    @Test
    fun `when sending select city intent, get weather list loaded state`() {
        viewModel.sendIntent(Intent.SelectCityIntent(MockEntity.mockCity))
        viewModel.state.observeForever {
            Assert.assertEquals(State.WeatherListLoaded(MockEntity.mockWeatherList), it)
        }
    }

    @Test
    fun `when sending leave detail intent, get city list loaded state`() {
        viewModel.sendIntent(Intent.LeaveDetailIntent)
        viewModel.state.observeForever {
            Assert.assertEquals(State.CityListLoaded(MockEntity.mockCityList), it)
        }
    }

    @Test
    fun `when sending refresh detail intent, get weather list loaded state`() {
        viewModel.sendIntent(Intent.RefreshDetailIntent(MockEntity.mockCity))
        viewModel.state.observeForever {
            Assert.assertEquals(State.WeatherListLoaded(MockEntity.mockWeatherList), it)
        }
    }
}
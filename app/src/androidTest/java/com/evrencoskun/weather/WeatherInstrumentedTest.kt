/*
 * Copyright (c) 12/23/19 4:35 PM Evren Coşkun
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

package com.evrencoskun.weather

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.evrencoskun.weather.mocks.MockJsonPlaceOrderService
import com.evrencoskun.weather.mocks.database.MockDatabaseRepositoryEns
import com.evrencoskun.weather.ui.MainActivity
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest

/**
 * Espresso UI test
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherInstrumentedTest : KoinTest, KoinComponent{


    @get:Rule
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java, false, false)

    private lateinit var mockDataRepository: MockDatabaseRepositoryEns
    private lateinit var mockJsonPlaceOrderService: MockJsonPlaceOrderService

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }


}
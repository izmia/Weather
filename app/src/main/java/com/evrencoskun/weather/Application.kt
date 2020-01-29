/*
 * Copyright (c) 12/18/19 1:48 PM Evren Coşkun
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

import android.app.Application
import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.DatabaseFactory
import com.evrencoskun.weather.repository.database.DatabaseRepository
import com.evrencoskun.weather.repository.network.NetworkRepository
import com.evrencoskun.weather.repository.network.RetrofitWrapper
import com.evrencoskun.weather.util.CoroutineContextProvider
import com.evrencoskun.weather.viewmodel.MainViewModel
import com.evrencoskun.weather.viewmodel.mvi.Action
import com.evrencoskun.weather.viewmodel.mvi.Intent
import com.evrencoskun.weather.viewmodel.mvi.Result
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
/**
 * Class used primarily for creating and holding the Koin Module for Dependency Injection
 */
@Suppress("unused")
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(listOf(module {
            single { Intent.IntentInterpreter() }
            single { Action.ActionInterpreter(get()) }
            single { Result.ResultInterpreter() }
            single { RetrofitWrapper.getWeatherService() }
            single { NetworkRepository(get()) }
            single { DatabaseRepository(DatabaseFactory(applicationContext).createDatabase()) }
            single { Repository(get(), get(), get()) }
            single { CoroutineContextProvider() }
            viewModel { MainViewModel(get(), get(), get(), get()) }
        }))
    }
}
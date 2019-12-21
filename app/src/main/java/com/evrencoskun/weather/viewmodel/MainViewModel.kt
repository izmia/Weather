/*
 * Copyright (c) 12/17/19 5:30 PM Evren Coşkun
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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evrencoskun.weather.util.CoroutineContextProvider
import com.evrencoskun.weather.viewmodel.mvi.Action
import com.evrencoskun.weather.viewmodel.mvi.Intent
import com.evrencoskun.weather.viewmodel.mvi.Result
import com.evrencoskun.weather.viewmodel.mvi.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for the Application
 */
class MainViewModel constructor(
    private val intentInterpreter: Intent.IntentInterpreter,
    private val actionInterpreter: Action.ActionInterpreter,
    private val resultInterpreter: Result.ResultInterpreter,
    private val contextPool: CoroutineContextProvider
) : ViewModel(), KoinComponent,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = contextPool.io

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state


    init {
        sendIntent(Intent.InitialIntent)
    }

    /**
     * This function pieces together each part of the View Models' architecture. Each [Intent] becomes a [State], and
     * in the process takes an [Action].
     *
     * The basic idea: [ Intents -> Actions -> (Action taken) -> Result -> State ]
     */
    fun sendIntent(intent: Intent) {
        launch(contextPool.io) {
            intentInterpreter.interpret(intent) { action ->
                actionInterpreter.interpret(action) { result ->
                    resultInterpreter.interpret(result, ::changeState)
                }
            }
        }
    }

    private suspend fun changeState(nextState: State) {
        launch(contextPool.main) {
            _state.value = nextState
        }
    }

    public override fun onCleared() {
        contextPool.cancelJob()
        super.onCleared()
    }
}
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

package com.evrencoskun.weather.viewmodel.mvi

import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.viewmodel.mvi.base.Interpreter

/**
 * Immutable object which represent an view's intent
 */
sealed class Intent {
    // For Main Screen
    object InitialIntent : Intent()
    data class AddCityIntent(val city: City) : Intent()
    data class RemoveCityIntent(val cityId: String) : Intent()
    data class SelectCityIntent(val city: City) : Intent()

    // For Detail Screen
    data class RefreshDetailIntent(val city: City) : Intent()
    object LeaveDetailIntent : Intent()



    /**
     * Maps [Intent]s to [Action]s
     */
    class IntentInterpreter : Interpreter<Intent, Action>() {
        override suspend fun interpret(input: Intent, callback: suspend (Action) -> Unit) {
            callback(
                when (input) {
                    is InitialIntent -> Action.GetCityListAction
                    is AddCityIntent -> Action.AddCityAction(input.city)
                    is RemoveCityIntent -> Action.RemoveCityAction(input.cityId)
                    is SelectCityIntent -> Action.GetWeatherAction(input.city)
                    is RefreshDetailIntent -> Action.GetWeatherAction(input.city)
                    is LeaveDetailIntent -> Action.GetCityListActionWithoutLoading
                }
            )
        }
    }
}
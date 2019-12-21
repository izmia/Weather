/*
 * Copyright (c) 12/21/19 3:27 PM Evren Coşkun
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

package com.evrencoskun.weather.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

class CoroutineContextProviderTest: CoroutineContextProvider() {

    var jobCanceled = false

    // Dispatchers.Unconfined makes a coroutine run on the current thread
    // without restricting the execution to a specific thread pool.
    @ExperimentalCoroutinesApi
    override val main: CoroutineContext = Dispatchers.Unconfined
    @ExperimentalCoroutinesApi
    override val io: CoroutineContext = Dispatchers.Unconfined

    override fun cancelJob() {
        jobCanceled = true
    }
}
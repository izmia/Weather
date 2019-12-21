/*
 * Copyright (c) 12/17/19 4:31 PM Evren Coşkun
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

package com.evrencoskun.weather.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.evrencoskun.weather.R
import com.evrencoskun.weather.ui.detail.DetailFragment
import com.evrencoskun.weather.viewmodel.MainViewModel
import com.evrencoskun.weather.viewmodel.mvi.State
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    // ViewModel must be injected here to allow the same instance to be shared with the fragments
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            // Fragment is already added in Activity before configuration change.
            return
        }

        logErrors()
    }

    // TODO: Include errors in any future analytics
    private fun logErrors() {
        viewModel.state.observe(this,
            Observer<State> { state ->
                when (state) {
                    is State.Error -> Log.e(javaClass.simpleName, state.message)
                }
            }
        )
    }

    override fun onBackPressed() {
        // Call onBackPressed method of DetailFragment
        // Before calling onBackPressed method of the activity
        // To send an LeaveDetailIntent.
        supportFragmentManager.fragments.forEach {
            if (it is NavHostFragment) {
                it.childFragmentManager.fragments.forEach { fragment ->
                    when (fragment) {
                        is DetailFragment -> fragment.onBackPressed()
                    }
                }
            }
        }
        super.onBackPressed()
    }
}

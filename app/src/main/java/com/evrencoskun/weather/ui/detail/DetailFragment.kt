/*
 * Copyright (c) 12/17/19 4:32 PM Evren Coşkun
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

package com.evrencoskun.weather.ui.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.evrencoskun.weather.R
import com.evrencoskun.weather.databinding.FragmentDetailBinding
import com.evrencoskun.weather.repository.database.entity.Weather
import com.evrencoskun.weather.viewmodel.MainViewModel
import com.evrencoskun.weather.viewmodel.mvi.Intent
import com.evrencoskun.weather.viewmodel.mvi.State
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private var snackbar: Snackbar? = null
    private var weatherListAdapter: WeatherListAdapter? = null
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupManualRefresh()
        setupRecyclerView()
        observeViewModelState()
    }


    private fun setupManualRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            // Get selected city object from bundle of that fragment
            val city = DetailFragmentArgs.fromBundle(arguments!!).cityArg
            mainViewModel.sendIntent(Intent.RefreshDetailIntent(city))
        }
    }

    private fun setupRecyclerView() {
        if (weatherListAdapter == null) {
            weatherListAdapter = WeatherListAdapter()
        }

        binding.detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weatherListAdapter
        }
    }

    private fun observeViewModelState() {
        mainViewModel.state.removeObservers(this)
        mainViewModel.state.observe(viewLifecycleOwner,
            Observer { state ->
                when (state) {
                    is State.Error -> {
                        showLoading(false)
                        showError(state.message)
                    }
                    is State.WeatherListLoaded -> {
                        hideError()
                        showLoading(false)
                        showLoadingSpinner(false)
                        updateWeatherList(state.weatherList)
                    }
                    is State.WeatherListLoading -> showLoading(true)
                }
            })
    }


    private fun updateWeatherList(weatherList: List<Weather>){
        weatherListAdapter?.updateWeatherList(weatherList)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showLoadingSpinner(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
    }

    private fun showError(message: String) {
        snackbar = Snackbar.make(binding.progressBar, message, Snackbar.LENGTH_INDEFINITE)
        snackbar?.show()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }


    fun onBackPressed() {
        mainViewModel.sendIntent(Intent.LeaveDetailIntent)
    }
}

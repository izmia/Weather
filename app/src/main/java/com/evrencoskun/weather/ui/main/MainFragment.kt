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

package com.evrencoskun.weather.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evrencoskun.weather.R
import com.evrencoskun.weather.databinding.FragmentMainBinding
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.viewmodel.MainViewModel
import com.evrencoskun.weather.viewmodel.mvi.Intent
import com.evrencoskun.weather.viewmodel.mvi.State
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private var snackbar: Snackbar? = null
    private var cityListAdapter: CityListAdapter? = null
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeClickListeners()
        observeViewModelState()
    }

    private fun setupRecyclerView() {
        if (cityListAdapter == null) {
            cityListAdapter = CityListAdapter()
        }

        binding.cityRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cityListAdapter
        }
    }

    private fun subscribeClickListeners() {
        // item click listener
        cityListAdapter?.onClick = {
            mainViewModel.sendIntent(Intent.SelectCityIntent(it))
        }

        // delete button click listener
        cityListAdapter?.onDeleteClick = {
            mainViewModel.sendIntent(Intent.RemoveCityIntent(it))
        }

        // add button click listener
        binding.addCityButton.setOnClickListener {
            val city = City(name = binding.cityEditText.text.toString())
            mainViewModel.sendIntent(Intent.AddCityIntent(city))
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
                    is State.CityListLoaded -> {
                        hideError()
                        showLoading(false)
                        setCityList(state.cityList)
                    }
                    is State.CityAdded -> {
                        hideError()
                        showLoading(false)
                        cleanEditText()
                        setCityList(state.list)
                    }
                    is State.CityRemoved -> {
                        hideError()
                        showLoading(false)
                        setCityList(state.list)
                    }
                    is State.WeatherListLoading -> {
                        hideError()
                        navigateToDetail(state.city)
                    }
                    is State.CityListLoading -> showLoading(true)
                }
            })
    }

    private fun setCityList(cityList: List<City>) {
        cityListAdapter?.updateCityList(cityList)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(message: String) {
        snackbar = Snackbar.make(binding.cityEditText, message, Snackbar.LENGTH_INDEFINITE)
        snackbar?.show()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun cleanEditText() {
        binding.cityEditText.setText("")
    }

    private fun navigateToDetail(city: City) {
        view?.findNavController()
            ?.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(city))
    }
}

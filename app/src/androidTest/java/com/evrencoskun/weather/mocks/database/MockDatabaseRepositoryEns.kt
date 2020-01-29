/*
 * Copyright (c) 12/23/19 5:00 PM Evren Coşkun
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

package com.evrencoskun.weather.mocks.database

import android.content.Context
import androidx.room.Room
import com.evrencoskun.weather.repository.Repository
import com.evrencoskun.weather.repository.database.Database
import com.evrencoskun.weather.repository.database.DatabaseRepository
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather

class MockDatabaseRepositoryEns(context: Context): DatabaseRepository(
    getMockDatabase(
        context
    )
){

    var getCityListSuccess = true
    var getCitySuccess = true
    var getWeatherListSuccess = true

    companion object{
        fun getMockDatabase(context: Context) =
            Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

    override suspend fun getCityList(
        success: suspend (List<City>) -> Repository.CityListRequestResult.Success,
        error: suspend (String) -> Repository.CityListRequestResult.Error
    ): Repository.CityListRequestResult {
        return if (getCityListSuccess)
            success(MockEntityEnst.mockCityList)
        else
            error("Error")
    }

    override suspend fun getCity(
        id: String,
        success: suspend (City) -> Repository.RequestResult.Success,
        error: suspend (String) -> Repository.RequestResult.Error
    ): Repository.RequestResult {
        return if (getCitySuccess)
            success(MockEntityEnst.mockCity)
        else
            error("Error")

    }
    override suspend fun getWeatherList(
        cityId: String,
        success: suspend (List<Weather>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getWeatherListSuccess)
            success(MockEntityEnst.mockWeatherList)
        else
            error("Error")

    }
}
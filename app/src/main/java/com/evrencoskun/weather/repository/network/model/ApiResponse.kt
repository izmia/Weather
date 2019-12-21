/*
 * Copyright (c) 12/17/19 8:59 PM Evren Coşkun
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

package com.evrencoskun.weather.repository.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data") val data: Data
)

data class Data(
    //@SerializedName("current_condition") val currentCondition: List<CurrentCondition>?,
    //@SerializedName("request") val request: List<RequestInfo>?,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("error") val error: List<Error>?
)

data class RequestInfo(
    @SerializedName("type") val type: String,
    @SerializedName("query") val query: String
)

data class CurrentCondition(
    @SerializedName("observation_time") val observationTime: String,
    @SerializedName("temp_C") val temp: Int,
    @SerializedName("weatherCode") val code: Int,
    //@SerializedName("weatherDesc") val descriptions: List<Description>,
    @SerializedName("windspeedKmph") val windSpeed: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("weatherIconUrl") val weatherIconUrl: List<IconUrl>
)

data class IconUrl(
    @SerializedName("value") val value: String
) {
    fun getConvertedValue(): String = value.replace("http", "https")
}

data class Weather(
    @SerializedName("date") val date: String,
    @SerializedName("maxtempC") val maxTemp: Int,
    @SerializedName("mintempC") val minTemp: Int,
    //@SerializedName("avgtempC") val avgTemp: Int,
    @SerializedName("hourly") val hourly: List<Hourly>
)


data class Hourly(
    //@SerializedName("time") val time: String,
    //@SerializedName("tempC") val temp: Int,
    //@SerializedName("windspeedKmph") val windSpeed: Int,
    @SerializedName("weatherCode") val code: Int
    //@SerializedName("weatherDesc") val descriptions: List<Description>,
    //@SerializedName("humidity") val humidity: Int,
    //@SerializedName("pressure") val pressure: Int,
    //@SerializedName("weatherIconUrl") val weatherIconUrl: List<IconUrl>
)

/*data class Description(
    @SerializedName("value") val value: String
)*/

data class Error(
    @SerializedName("msg") val message: String
)
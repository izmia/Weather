/*
 * Copyright (c) 12/19/19 8:00 AM Evren Coşkun
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

import android.content.Context
import com.evrencoskun.weather.R
import com.evrencoskun.weather.repository.database.entity.City
import com.evrencoskun.weather.repository.database.entity.Weather
import com.evrencoskun.weather.repository.network.model.ApiResponse
import com.evrencoskun.weather.repository.network.model.Error


/**
 * This function creates a list of city for the time when a user open the app for the first time.
 */
fun getDefaultCityList(): List<City> {
    return listOf(
        City(name = "London"),
        City(name = "Barcelona"),
        City(name = "New York"),
        City(name = "Istanbul")
    )
}

/**
 * Helper function to generate proper a weather list before updating in local database.
 */
fun generateWeatherList(response: ApiResponse, city: City): List<Weather> {
    val list = mutableListOf<Weather>()
    response.data.weather?.forEach {
        list.add(
            Weather(
                cityId = city.id,
                date = it.date,
                maxTemp = it.maxTemp,
                minTemp = it.minTemp,
                // Considering just first item of hourly object
                weatherCode = it.hourly[0].code
            )
        )
    }
    return list
}


fun combineErrorMessages(errorList: List<Error>): String {
    return errorList.joinToString(
        separator = ", ",
        prefix = "[Getting weather list process is not successful because of the errors: ",
        postfix = "]"
    )
}


/**
 * Helper method to provide the string according to the weather
 * condition id returned by the OpenWeatherMap call.
 *
 * @param context   Android context
 * @param weatherId from OpenWeatherMap API response
 * See http://openweathermap.org/weather-conditions for a list of all IDs
 *
 * @return String for the weather condition, null if no relation is found.
 */
fun getStringForWeatherCondition(context: Context, weatherId: Int): String? {
    val stringId: Int = when (weatherId) {
        in 200..232 -> R.string.condition_2xx
        in 299..321 -> R.string.condition_3xx
        500 -> R.string.condition_500
        501 -> R.string.condition_501
        502 -> R.string.condition_502
        503 -> R.string.condition_503
        504 -> R.string.condition_504
        511 -> R.string.condition_511
        520 -> R.string.condition_520
        531 -> R.string.condition_531
        600 -> R.string.condition_600
        601 -> R.string.condition_601
        602 -> R.string.condition_602
        611 -> R.string.condition_611
        612 -> R.string.condition_612
        615, 616 -> R.string.condition_615
        620, 621, 622 -> R.string.condition_620
        701 -> R.string.condition_701
        711 -> R.string.condition_711
        721 -> R.string.condition_721
        731 -> R.string.condition_731
        741 -> R.string.condition_741
        751 -> R.string.condition_751
        761 -> R.string.condition_761
        762 -> R.string.condition_762
        771 -> R.string.condition_771
        781 -> R.string.condition_781
        800 -> R.string.condition_800
        801 -> R.string.condition_801
        802 -> R.string.condition_802
        803 -> R.string.condition_803
        804 -> R.string.condition_804
        900 -> R.string.condition_900
        901 -> R.string.condition_901
        902 -> R.string.condition_902
        903 -> R.string.condition_903
        904 -> R.string.condition_904
        905 -> R.string.condition_905
        906 -> R.string.condition_906
        951 -> R.string.condition_951
        952 -> R.string.condition_952
        953 -> R.string.condition_953
        954 -> R.string.condition_954
        955 -> R.string.condition_955
        956 -> R.string.condition_956
        957 -> R.string.condition_957
        958 -> R.string.condition_958
        959 -> R.string.condition_959
        960 -> R.string.condition_960
        961 -> R.string.condition_961
        962 -> R.string.condition_962
        else -> R.string.condition_2xx
    }
    return context.getString(stringId)
}

/**
 * Temperature data is stored in Celsius by our app. Depending on the user's preference,
 * the app may need to display the temperature in Fahrenheit. This method will perform that
 * temperature conversion if necessary. It will also format the temperature so that no
 * decimal points show. Temperatures will be formatted to the following form: "21°"
 *
 * @param context     Android Context to access preferences and resources
 * @param temperature Temperature in degrees Celsius (°C)
 *
 * @return Formatted temperature String in the following form:
 * "21°"
 */
fun formatTemperature(context: Context, temperature: Double): String {
    return String.format(context.getString(R.string.format_temperature), temperature)
}


/**
 * Based on weather code data for Open Weather Map.
 */
fun getResourceIdForWeatherCondition(weatherId: Int): Int {
    return when (weatherId) {
        in 200..232 -> R.drawable.art_storm
        in 300..321 -> R.drawable.art_light_rain
        in 500..504 -> R.drawable.art_rain
        511 -> R.drawable.art_snow
        in 520..531 -> R.drawable.art_rain
        in 600..622 -> R.drawable.art_snow
        in 701..761 -> R.drawable.art_fog
        771, 781 -> R.drawable.art_storm
        800 -> R.drawable.art_clear
        801 -> R.drawable.art_light_clouds
        in 802..804 -> R.drawable.art_clouds
        in 900..906 -> R.drawable.art_storm
        in 958..962 -> R.drawable.art_storm
        in 951..957 -> R.drawable.art_clear
        else -> R.drawable.art_storm
    }
}


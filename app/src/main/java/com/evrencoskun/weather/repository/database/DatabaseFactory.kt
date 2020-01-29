/*
 * Copyright (c) 12/17/19 10:20 PM Evren Coşkun
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

package com.evrencoskun.weather.repository.database

import android.content.Context
import androidx.room.Room
import org.koin.standalone.KoinComponent

/**
 * Factory for building a database.
 *
 * This class is meant to abstract database creation from the Application class, where it is injected into the rest
 * of the app
 */
class DatabaseFactory constructor(private val context: Context) : KoinComponent {
    // For Singleton instantiation
    @Volatile
    private var instance: Database? = null


    private fun buildDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()
    }

    fun createDatabase(): Database {
        return instance ?: synchronized(this) {
            instance
                ?: buildDatabase(
                    context
                ).also { instance = it }
        }
    }
}

private const val DATABASE_NAME = "weather-db-test1"
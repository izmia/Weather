# Weather App

A simple project used to illustrate clean architecture and testing in Android with Kotlin.

The app shows a list of city from user local db. Upon tapping on one of the cities, a detail view is shown with further a weather list from [worldweatheronline](www.worldweatheronline.com/).


### Architecture
<div style="text-align:center">
<img src="/images/architecture.png" alt="Architecture Diagram" width="400" />
</div>

The drawn diagram above illustrates how the Architecture for the App works. Essentially this is a slimmed down version of MVI. 

There are three layers to the app, namely the UI, View Model, and Repository layers. Users send an Intent through the View Model layer which are later emitted by a State. The State is emitted via a LiveData, and is all the UI layer needs to do is observe it in order to figure out what to display.
The View Model layer is (importantly) unidirectional, and takes intents, maps them to actions, performs the actions, and returns results. Those results are finally mapped to the State.
These mappings occur in an Interpreter which exists in each step along the way. 

The Repository layer contains a Repository which is the logic of how the app gets data. Data is initially loaded from the network, but subsequently saved to a Database until that data is no longer viable. At the present there is no invalidation logic. 
The UI layer is an Activity with multiple fragments.

- The reason why I use this architecture was to get more experiences to find out different aspects of this approach.


## Installing and Testing

The app can be run and installed easily from Android Studio. The instructions below are for running it via the command line.

### Running the unit tests via the command line

Unit tests work the same way:

    ./gradlew test

## Built With

* [Kotlin](https://kotlinlang.org/)
* [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform actions in response to a change in the lifecycle status of another component, such as activities and fragments.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - designed to store and manage UI-related data in a lifecycle conscious way. The ViewModel class allows data to survive configuration changes such as screen rotations.
* [Mockito](https://site.mockito.org/) - most popular Mocking framework for unit tests written in Java.
* [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView) - to show data set in a list
* [Retrofit2](https://square.github.io/retrofit/) - type-safe HTTP client.
* [Gson](https://github.com/google/gson) - makes it easy to parse JSON into Kotlin objects.
* [Room](https://developer.android.com/topic/libraries/architecture/room) - to store the data set in local database which is SQLite.
* [ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout) - to create a responsive layout without using nested layouts
* [Koin](https://github.com/InsertKoinIO/koin) - easy to use dependency injection library
* [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin/wiki) - a wrapper library around Mockito
* [Espresso](https://developer.android.com/training/testing/espresso/) - to write concise, beautiful, and reliable Android UI tests
* [Navigation Architecture Component](https://developer.android.com/topic/libraries/architecture/navigation/) - to navigate between fragments easily without dealing with things like Fragment transactions, Fragment Manager, back stack. 
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)  - managing background threads with simplified code and reducing needs for callbacks.
* [DataBinding](https://developer.android.com/topic/libraries/data-binding/) - allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.


## License

    Copyright (C) 2019 Evren Coşkun
    
    This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.


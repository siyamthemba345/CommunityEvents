# CommunityEvents
Community Events Android application with South African public holidays REST API integration.

@'
# CommunityEvents

## Project Overview

CommunityEvents is an Android application developed in Kotlin. It provides a foundation for viewing community events and includes a South African public holidays feature that retrieves data from a REST API.

## Features

- Displays community event information.
- Uses Android layouts and Kotlin classes to present event data.
- Retrieves South African public holidays from the OpenHolidays API.
- Displays holiday dates and names in a dialog.
- Handles unsuccessful API requests and empty responses.
- Performs network requests on a background thread to keep the user interface responsive.

## Technologies Used

- Kotlin
- Android Studio
- Android SDK
- Gradle
- REST API
- JSON parsing with `org.json`
- Java networking classes, including `HttpURLConnection`

## REST API Integration

The application requests South African public holiday data from:

`https://openholidaysapi.org/PublicHolidays`

The request specifies:
- Country: South Africa (`ZA`)
- Language: English (`EN`)
- Date range: 1 January to 31 December of the current year

The application reads the JSON response, extracts holiday dates and English names, and displays the results to the user.

If the request fails, the application displays an error message. If the API returns no usable holiday entries, the application displays an appropriate empty-state message.

## Project Structure

- `app/src/main/java/` — Kotlin source code.
- `app/src/main/res/layout/` — Android XML layouts.
- `app/src/main/res/drawable/` — Images and drawable resources.
- `app/src/main/res/values/` — Application strings, colours and themes.
- `app/src/test/` — Local unit tests.
- `app/src/androidTest/` — Android instrumented tests.
- `gradle/` — Gradle wrapper and configuration files.

## Requirements

- Android Studio
- A compatible Java Development Kit
- Android SDK configured in Android Studio
- Internet access to retrieve public holiday data

## Build Instructions

1. Clone or download this repository.
2. Open the project root in Android Studio.
3. Allow Gradle to sync.
4. Connect an Android device or start an emulator.
5. Build the application using Build > Make Project.

Alternatively, run the following command from the project root on Windows:

```powershell
.\gradlew.bat assembleDebug

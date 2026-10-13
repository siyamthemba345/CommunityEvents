package com.example.communityevents

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.Blob
import android.widget.Toast
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private fun drawableToBlob(drawableResId: Int): Blob {
        val byteArray = resources.openRawResource(drawableResId).readBytes()
        return ByteArrayBlob(byteArray)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnViewEvents = findViewById<Button>(R.id.btn_view_events)
        val btnCommunityEvents = findViewById<Button>(R.id.btn_community_events)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val eventList = findViewById<RecyclerView>(R.id.Events)
        val btnSaHolidays = findViewById<Button>(R.id.btn_sa_holidays)

        val events = mutableListOf(
            Events_Data(1, "Plet Rage", "Pletenburg Bay", 12, drawableToBlob(R.drawable.plet_rage), "Local Events"),
            Events_Data(2, "Downhill Sokkie Jol", "Jeffrey's Bay", 14, drawableToBlob(R.drawable.downhill_sokkie_jol), "Local Events"),
            Events_Data(3, "GOTG Charity Drive", "Port Elizabeth", 11, drawableToBlob(R.drawable.gotg), "Community Events"),
            Events_Data(4, "Iron Man Marathon", "Port Elizabeth, EC", 8, drawableToBlob(R.drawable.im_triathelon), "Community Events"),
        )

        eventList.layoutManager = LinearLayoutManager(this)
        eventList.adapter = eventListAdapter(events)

        btnViewEvents.setOnClickListener {
            fun filterEvents(category: String) {
                val filteredEvents = when (category) {
                    "All Events" -> events
                    else -> events.filter { it.category == category }
                }
                eventList.adapter = eventListAdapter(filteredEvents)
            }

            val filter = arrayOf(
                "All Events",
                "Local Events",
                "Community Events"
            )

            AlertDialog.Builder(this)
                .setTitle("Filter Events")
                .setItems(filter) { _, which ->
                    when (which) {
                        0 -> filterEvents("All Events")
                        1 -> filterEvents("Local Events")
                        2 -> filterEvents("Community Events")
                    }
                }
                .show()
        }

        btnCommunityEvents.setOnClickListener {
            val communityEvents = events.filter { it.category == "Community Events" }
            eventList.adapter = eventListAdapter(communityEvents)
        }

        btnSave.setOnClickListener {


        }
        btnSaHolidays.setOnClickListener {
            loadSouthAfricanHolidays()
        }
    }
// this is for the South African holidays function/ the api
    private fun loadSouthAfricanHolidays() {
        Toast.makeText(this, "Loading public holidays...", Toast.LENGTH_SHORT).show()

        Thread {
            var connection: HttpURLConnection? = null

            try {
                val year = Calendar.getInstance().get(Calendar.YEAR)

                val apiUrl =
                    "https://openholidaysapi.org/PublicHolidays" +
                            "?countryIsoCode=ZA" +
                            "&languageIsoCode=EN" +
                            "&validFrom=$year-01-01" +
                            "&validTo=$year-12-31"

                connection = URL(apiUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.setRequestProperty("Accept", "application/json")

                val responseCode = connection.responseCode

                if (responseCode !in 200..299) {
                    throw IOException("The API returned HTTP $responseCode")
                }

                val responseText = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                val holidays = JSONArray(responseText)
                val holidayLines = mutableListOf<String>()

                for (i in 0 until holidays.length()) {
                    val holiday = holidays.getJSONObject(i)
                    val date = holiday.optString("startDate", "").trim()
                    val names = holiday.optJSONArray("name")

                    if (date.isBlank()) {
                        continue
                    }

                    var holidayName = "Unnamed holiday"

                    if (names != null) {
                        for (j in 0 until names.length()) {
                            val nameEntry = names.optJSONObject(j)

                            if (nameEntry?.optString("language") == "EN") {
                                val returnedName = nameEntry.optString("text", "").trim()

                                if (returnedName.isNotBlank()) {
                                    holidayName = returnedName
                                    break
                                }
                                break
                            }
                        }
                    }

                    holidayLines.add("$date — $holidayName")
                }

                val resultMessage = if (holidayLines.isEmpty()) {
                    "No public holidays were returned for $year."
                } else {
                    holidayLines.joinToString("\n\n")
                }

                runOnUiThread {
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("South African Public Holidays ($year)")
                        .setMessage(resultMessage)
                        .setPositiveButton("Close", null)
                        .show()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Unable to Load Holidays")
                        .setMessage(
                            "Could not retrieve public holidays. " +
                                    "Check your internet connection and try again.\n\n" +
                                    "Details: ${e.localizedMessage ?: "Unknown error"}"
                        )
                        .setPositiveButton("OK", null)
                        .show()
                }
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

}
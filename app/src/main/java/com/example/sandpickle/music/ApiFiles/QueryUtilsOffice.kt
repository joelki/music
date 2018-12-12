package com.example.sandpickle.music.ApiFiles

import android.text.TextUtils
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.util.*

class QueryUtilsOffice {
    companion object {

        private val LogTag = this::class.java.simpleName
        //okay started an intent, need to retrieve it. called "text"

        fun fetchAirQualityData(requestUrl: String): ArrayList<Measurement>? {

            val url: URL? = createUrl("https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCa90xqK2odw1KV5wHU9WRhg&maxResults=45&key=AIzaSyA4x6FRqW7xyM6aVhWVK2KNcyibJbJoviA")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(LogTag, "Problem making the HTTP request.", e)
            }

            return extractDataFromJson(jsonResponse)
        }

        private fun createUrl(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            }
            catch (e: MalformedURLException) {
                Log.e(LogTag, "Problem building the URL.", e)
            }

            return url
        }

        private fun makeHttpRequest(url: URL?): String {
            var jsonResponse = ""

            if (url == null) {
                return jsonResponse
            }

            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 1000 // 10 seconds
                urlConnection.connectTimeout = 1500 // 15 seconds
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                }
                else {
                    Log.e(LogTag, "Error response code: ${urlConnection.responseCode}")
                }
            }
            catch (e: IOException) {
                Log.e(LogTag, "Problem retrieving the air quality data results.", e)
            }
            finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }

            return jsonResponse
        }

        private fun readFromStream(inputStream: InputStream?): String {
            val output = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    output.append(line)
                    line = reader.readLine()
                }
            }

            return output.toString()
        }

        private fun extractDataFromJson(airQualityJson: String?): ArrayList<Measurement>? {
            if (TextUtils.isEmpty(airQualityJson)) {
                return null
            }

            val videoIdString = ArrayList<Measurement>()
            try {
                val baseJsonResponse = JSONObject(airQualityJson)

                val airQualityArray = baseJsonResponse.getJSONArray("items")
                for (i in 0 until airQualityArray.length()) {
                    val airQualityResult = airQualityArray.getJSONObject(i)
                    val measurements = airQualityResult.getJSONObject("id")
                    val snippet = airQualityResult.getJSONObject("snippet")
                    val thumbnails = snippet.getJSONObject("thumbnails")
                    val default = thumbnails.getJSONObject("high")

                    if(measurements.has("videoId")) {

                        var checkIfVideo = measurements.getString("videoId")
                        var checkIfTitle = snippet.getString("title")
                        var checkIfURL = default.getString("url")
                        val videoIdStringObject = Measurement(
                                checkIfVideo,
                                checkIfTitle,
                                checkIfURL
                        )
                        videoIdString.add(videoIdStringObject)
                    }
                }
            }
            catch (e: JSONException) {
                Log.e(LogTag, "Problem parsing the air quality JSON results", e)
            }

            return videoIdString
        }
    }
}
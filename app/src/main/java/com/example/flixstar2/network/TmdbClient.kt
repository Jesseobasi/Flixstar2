package com.example.flixsterplus.network

import com.example.flixsterplus.models.Person
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

object TmdbClient {
    private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
    private const val BASE = "https://api.themoviedb.org/3"
    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchPopularPeople(): List<Person> {
        val url = "$BASE/person/popular?api_key=$API_KEY"
        val req = Request.Builder().url(url).build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}")
            val body = resp.body?.string() ?: throw Exception("Empty body")
            val root = gson.fromJson(body, JsonObject::class.java)
            val results = root.getAsJsonArray("results")
            val type = object : TypeToken<List<Person>>() {}.type
            return gson.fromJson(results, type)
        }
    }

    fun fetchPersonDetails(personId: Int): JsonObject {
        val url = "$BASE/person/$personId?api_key=$API_KEY"
        val req = Request.Builder().url(url).build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}")
            val body = resp.body?.string() ?: throw Exception("Empty body")
            return gson.fromJson(body, JsonObject::class.java)
        }
    }

    // Helper to build TMDB image full URL
    fun imageUrl(path: String?, size: String = "w185"): String? {
        return if (path == null) null else "https://image.tmdb.org/t/p/$size$path"
    }
}

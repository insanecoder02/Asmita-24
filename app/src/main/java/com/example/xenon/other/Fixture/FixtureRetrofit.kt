package com.example.xenon.other.Fixture

import com.example.xenon.other.FlickrService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FixtureRetrofit {
    private const val BASE_URL = "https://app-admin-api.asmitaiiita.org/api/fixtures/"

    fun create(): FixtureService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FlickrService::class.java)
    }
}
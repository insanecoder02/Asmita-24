package com.interiiit.xenon.other

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitClient {
    private const val BASE_URL = "https://www.flickr.com/services/"

    fun create(): FlickrService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FlickrService::class.java)
    }
}

object FixtureRetrofit {
    private const val BASE_URL = "https://app-admin-api.asmitaiiita.org/api/fixtures/"

    fun create(): FixtureService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FixtureService::class.java)
    }
}
interface FixtureService {
    @GET("sport")
    fun getSport(): Call<String>

    @GET("htmlstring")
    fun getHtmlString(): Call<String>

    @GET("date")
    fun getDate(): Call<String>

    @GET("day")
    fun getDay(): Call<String>
}
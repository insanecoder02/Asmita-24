package com.example.xenon.other

import com.example.xenon.DataClass.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {
    @GET("rest/")
    fun getPhotos(
        @Query("method") method: String = "flickr.people.getPhotos",
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("user_id") userId: String="ams_iiita",
        @Query("per_page") perPage: Int = 16,
        @Query("photoset_id") photosetId: String ="72177720312642545"
    ): Call<FlickrResponse>
}
package com.example.xenon.DataClass.GalleryDataClass

data class Gallery(
    val img: String,
    val name: String
)

data class FlickrPhoto(
    val id: String,
    val farm: Int,
    val server: String,
    val secret: String
)

data class FlickrResponse(val photos: FlickrPhotos)

data class FlickrPhotos(val photo: List<FlickrPhoto>)


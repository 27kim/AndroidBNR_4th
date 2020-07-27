package io.lab27.photogallery.api

import com.google.gson.annotations.SerializedName
import io.lab27.photogallery.GalleryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}
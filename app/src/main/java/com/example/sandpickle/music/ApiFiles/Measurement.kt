package com.example.sandpickle.music.ApiFiles

import java.util.*

class Measurement(var videoId: String, var titleId: String, var imageURL: String) {

    // Parameter
    fun getParameter(): String {
        return this.videoId
    }

    fun setParameter(value: String) {
        this.videoId = value
    }

    fun getTitle(): String {
        return this.titleId
    }

    fun setTitle(value: String) {
        this.titleId = value
    }

    fun getImageUrl(): String {
        return this.imageURL
    }

    fun setImageUrl(value: String) {
        this.imageURL = value
    }
}
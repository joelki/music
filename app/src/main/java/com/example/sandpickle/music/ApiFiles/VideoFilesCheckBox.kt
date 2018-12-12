package com.example.sandpickle.music.ApiFiles


class VideoFilesCheckBox(var videoId: String, var titleId: String, var imageURL: String, var important: Int) {

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
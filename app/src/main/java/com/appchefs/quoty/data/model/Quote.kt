package com.appchefs.quoty.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Quote(
    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("content")
    val quoteContent: String,

    @Expose
    @SerializedName("author")
    val author: String,
)
package com.appchefs.quoty.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuoteResponse(

    @Expose
    @SerializedName("id")
    var id: String,

    @Expose
    @SerializedName("content")
    var content: String,

    @Expose
    @SerializedName("author")
    var author: String,

    @Expose
    @SerializedName("authorSlu")
    var authorSlu: String,

    @Expose
    @SerializedName("length")
    var length: Int,

    @Expose
    @SerializedName("tags")
    var tags: ArrayList<String>,
)
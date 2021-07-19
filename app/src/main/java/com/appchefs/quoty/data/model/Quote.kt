package com.appchefs.quoty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appchefs.quoty.data.model.Quote.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME)
data class Quote(

    @PrimaryKey
    @SerializedName("_id")
    var id: String,

    @SerializedName("author")
    var author: String? = null,

    @SerializedName("content")
    var quoteContent: String? = null,

    @SerializedName("authorSlug")
    var authorSlug: String? = null,

) {
    companion object {
        const val TABLE_NAME = "table_quote"
    }
}

//
//@SerializedName("tags")
//var list: List<String>,
//
//var isFavorite: Boolean? = false
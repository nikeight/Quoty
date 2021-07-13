package com.appchefs.quoty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appchefs.quoty.data.model.Quote.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Quote(

    @PrimaryKey
    var id: Int? = 0,
    var author: String? = null,
    var quoteContent: String? = null,
    var isFavorite: Boolean? = false
){
    companion object {
        const val TABLE_NAME = "table_quote"
    }
}
package com.appchefs.quoty.data.local.dao

import androidx.room.*
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.utils.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuote(quote: Quote)

    @Query("SELECT * FROM ${Quote.TABLE_NAME} WHERE ID = :quoteId")
    fun getQuote(quoteId: String): Flow<Quote>

    @Query("SELECT * FROM ${Quote.TABLE_NAME} ORDER BY isFavorite DESC")
    fun getAllQuotes() : Flow<List<Quote>>

    @Delete
    suspend fun delete(quote: Quote)

    @Update
    suspend fun update(quote: Quote)
}
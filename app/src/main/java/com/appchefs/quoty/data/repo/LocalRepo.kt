package com.appchefs.quoty.data.repo

import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalRepo {
    fun getAllDataFromLocal(): Flow<Resource<List<Quote>>>
    suspend fun deleteQuoteFromDb(quote: Quote)
    suspend fun updateQuote(quote: Quote)
}

@ExperimentalCoroutinesApi
class DatabaseRepo @Inject constructor(
    private val quoteDao: QuoteDao
) : LocalRepo {

    override fun getAllDataFromLocal(): Flow<Resource<List<Quote>>> {
        return object : DatabaseBoundRepository<List<Quote>>() {

            override fun fetchAllDataFromLocal(): Flow<List<Quote>> {
               return quoteDao.getAllQuotes()
            }

        }.asFlow()
    }

    override suspend fun deleteQuoteFromDb(quote: Quote) {
        quoteDao.delete(quote)
    }

    override suspend fun updateQuote(quote: Quote) {
        quoteDao.update(quote)
    }
}
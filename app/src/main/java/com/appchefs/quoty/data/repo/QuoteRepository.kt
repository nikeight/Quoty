package com.appchefs.quoty.data.repo

import com.appchefs.quoty.utils.Resource
import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.data.remote.NetworkService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface QuoteRepository {
    fun getRandomQuote(): Flow<Resource<Quote>>
    fun getQuote(tag: String): Flow<Resource<Quote>>
}

@ExperimentalCoroutinesApi
class DefaultQuoteRepository @Inject constructor(
    private val quoteDao: QuoteDao,
    private val networkService: NetworkService
) : QuoteRepository {

    private var postId: Int? = null

    override fun getRandomQuote(): Flow<Resource<Quote>> {
       return object : NetworkBoundRepository<Quote, Quote>(){

           override suspend fun fetchFromRemote(): Response<Quote> {
               return networkService.getRandomQuotes()
           }

           override suspend fun saveRemoteData(response: Quote) {
               postId = response.id
               quoteDao.addQuote(response)
           }

           override fun fetchFromLocal(): Flow<Quote> {
               return quoteDao.getQuote(postId!!)
           }

       }.asFlow()
    }

    override fun getQuote(tag: String): Flow<Resource<Quote>> {
        return object : NetworkBoundRepository<Quote, Quote>(){

            override suspend fun fetchFromRemote(): Response<Quote> {
                return networkService.getQuote(tag)
            }

            override suspend fun saveRemoteData(response: Quote) {
                // Todo: Changes here for the post Id
                postId = response.id
                quoteDao.addQuote(response)
            }

            override fun fetchFromLocal(): Flow<Quote> {
                // Showing at least any default quote.
                return quoteDao.getQuote(postId ?: 1)
            }

        }.asFlow()
    }
}
package com.appchefs.quoty.data.repo

import Resource
import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    /**
     * Fetches Response from the Remote end points.
     */
    @WorkerThread
    protected abstract suspend fun fetchFromRemote() : Response<REQUEST>

    /**
     * Save the response data to Room Database
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Retrieved the saved data from the Local DB
     */
    @WorkerThread
    protected abstract fun fetchFromLocal(): Flow<RESULT>

    fun asFlow() = flow<Resource<RESULT>> {

        // Emit the saved local data first
        emit(Resource.Success(fetchFromLocal().first()))

        // fetch another quote
        val apiResponse = fetchFromRemote()

        // parse the response body
        val remoteQuote = apiResponse.body()

        //checking the response body (validation)
        if (apiResponse.isSuccessful && remoteQuote != null){
            saveRemoteData(remoteQuote)
        }else{
            emit(Resource.Failed(apiResponse.message()))
        }

        emitAll(
            fetchFromLocal().map {
                Resource.Success(it)
            }
        )
    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Network error! can't get latest quote"))
    }
}
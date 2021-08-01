package com.appchefs.quoty.data.repo

import androidx.annotation.WorkerThread
import com.appchefs.quoty.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
abstract class DatabaseBoundRepository<RESULT> {

    @WorkerThread
    protected abstract fun fetchAllDataFromLocal() : Flow<RESULT>

    fun asFlow() = flow<Resource<RESULT>>{
        emitAll(
            fetchAllDataFromLocal().map {
                Resource.Success(it)
            }
        )
    }
}
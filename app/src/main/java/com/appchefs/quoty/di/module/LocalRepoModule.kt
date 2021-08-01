package com.appchefs.quoty.di.module

import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.repo.DatabaseRepo
import com.appchefs.quoty.data.repo.DefaultQuoteRepository
import com.appchefs.quoty.data.repo.LocalRepo
import com.appchefs.quoty.data.repo.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class DatabaseRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindDatabaseRepository(repository: DatabaseRepo) : LocalRepo
}
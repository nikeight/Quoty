package com.appchefs.quoty.di.module

import com.appchefs.quoty.data.repo.DefaultQuoteRepository
import com.appchefs.quoty.data.repo.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class QuoteRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindQuoteRepository(repository: DefaultQuoteRepository) : QuoteRepository
}
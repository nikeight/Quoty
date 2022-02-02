package com.appchefs.quoty.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.data.repo.LocalRepo
import com.appchefs.quoty.data.repo.QuoteRepository
import com.appchefs.quoty.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val localRepo: LocalRepo
) : ViewModel() {

    private val _randomQuote = MutableStateFlow<Status<Quote>>(Status.empty())
    val randomQuote: StateFlow<Status<Quote>> = _randomQuote.asStateFlow()

    private val _quote = MutableStateFlow<Status<Quote>>(Status.empty())
    val quote: StateFlow<Status<Quote>> = _quote.asStateFlow()

    private val _allQuotes = MutableStateFlow<Status<List<Quote>>>(Status.empty())
    val allQuote: StateFlow<Status<List<Quote>>> = _allQuotes.asStateFlow()


    fun getRandomQuote() {
        viewModelScope.launch {
            quoteRepository.getRandomQuote().onStart {
                _randomQuote.value = Status.loading()
            }.map { resource -> Status.fromResource(resource) }
                .collect { state -> _randomQuote.value = state }
        }
    }

    fun getQuote(tag: String) {
        viewModelScope.launch {
            quoteRepository.getQuote(tag).onStart {
                _quote.value = Status.loading()
            }.map { resource -> Status.fromResource(resource) }
                .collect { state -> _quote.value = state }
        }
    }

    fun getAllQuotes() {
        viewModelScope.launch {
            localRepo.getAllDataFromLocal().onStart {
                _allQuotes.value = Status.loading()
            }.map { resources ->
                Status.fromResource(resources)
            }.collect {
                _allQuotes.value = it
            }
        }
    }

    fun deleteQuote(quote: Quote){
        viewModelScope.launch {
            localRepo.deleteQuoteFromDb(quote)
        }
    }

    fun updateQuote(quote: Quote){
        viewModelScope.launch {
            localRepo.updateQuote(quote)
        }
    }
}
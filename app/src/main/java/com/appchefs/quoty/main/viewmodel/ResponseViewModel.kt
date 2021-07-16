package com.appchefs.quoty.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appchefs.quoty.data.model.QuoteAPI
import com.appchefs.quoty.data.repo.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ResponseViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _randomQuote = MutableLiveData<QuoteAPI>()
    val randomQuote: LiveData<QuoteAPI> = _randomQuote

    private val _quote = MutableLiveData<QuoteAPI>()
    val quote: LiveData<QuoteAPI> = _quote

    fun getRandomQuote(){
        viewModelScope.launch {
            remoteRepository.getRandomApiResponse().let {response ->
                if (response.isSuccessful){
                    _randomQuote.postValue(response.body())
                }
            }
        }
    }

    fun getQuoteByTag(tag: String){
        viewModelScope.launch {
            remoteRepository.getQuoteByTagResponse(tag).let { response ->
                if (response.isSuccessful){
                    _quote.postValue(response.body())
                }
            }
        }
    }
}
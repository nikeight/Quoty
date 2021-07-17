package com.appchefs.quoty.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appchefs.quoty.data.model.QuoteAPI
import com.appchefs.quoty.data.repo.RemoteRepository
import com.appchefs.quoty.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ResponseViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _randomQuote = MutableLiveData<Status<QuoteAPI>>()
    val randomQuote: LiveData<Status<QuoteAPI>> = _randomQuote

    private val _quote = MutableLiveData<Status<QuoteAPI>>()
    val quote: LiveData<Status<QuoteAPI>> = _quote

    fun getRandomQuote() {
        viewModelScope.launch {
            remoteRepository.getRandomApiResponse().let { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        _randomQuote.postValue(Status.success(it))
                    }
                }else{
                    Log.d("Response","${response.code()}")
                    _randomQuote.postValue(Status.error("Waiting for network, try again"))
                }
            }
        }
    }

    fun getQuoteByTag(tag: String) {
        viewModelScope.launch {
            remoteRepository.getQuoteByTagResponse(tag).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        _quote.postValue(Status.success(it))
                    }
                }else{
                    _quote.postValue(Status.error("Waiting for network, try again"))
                }
            }
        }
    }
}
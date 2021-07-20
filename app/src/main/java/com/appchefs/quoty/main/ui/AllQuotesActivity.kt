package com.appchefs.quoty.main.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appchefs.quoty.R
import com.appchefs.quoty.databinding.ActivityAllQuotesBinding
import com.appchefs.quoty.main.base.BaseActivity
import com.appchefs.quoty.main.ui.adapter.QuoteAdapter
import com.appchefs.quoty.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AllQuotesActivity : BaseActivity<MainViewModel, ActivityAllQuotesBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityAllQuotesBinding = ActivityAllQuotesBinding.inflate(layoutInflater)

    private val mAdapter = QuoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        initView()
        setUpObserver()
    }

    private fun initView() {
        mViewBinding.run {
            rvAllQuotes.layoutManager = LinearLayoutManager(this@AllQuotesActivity)
            rvAllQuotes.adapter = mAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.getAllQuotes()
    }

    private fun setUpObserver(){
        mViewModel.allQuote.observe(this, Observer {
           if (it.isNotEmpty()){
               Log.d("QuoteList",it.toString())
               mAdapter.submitList(it.toMutableList())
           }
        })
    }
}
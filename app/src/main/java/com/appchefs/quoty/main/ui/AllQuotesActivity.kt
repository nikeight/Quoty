package com.appchefs.quoty.main.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.appchefs.quoty.R
import com.appchefs.quoty.databinding.ActivityAllQuotesBinding
import com.appchefs.quoty.main.base.BaseActivity
import com.appchefs.quoty.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AllQuotesActivity : BaseActivity<MainViewModel, ActivityAllQuotesBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityAllQuotesBinding = ActivityAllQuotesBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
    }


}
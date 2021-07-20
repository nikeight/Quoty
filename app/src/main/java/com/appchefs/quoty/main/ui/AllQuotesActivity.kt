package com.appchefs.quoty.main.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                    // We don't need this method.
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            val quote = mAdapter.currentList[viewHolder.adapterPosition]
                            mViewModel.deleteQuote(quote)
                            Toast.makeText(this@AllQuotesActivity,"Quote deleted from db",Toast.LENGTH_SHORT).show()
                        }

                        ItemTouchHelper.RIGHT -> {
                            var quote = mAdapter.currentList[viewHolder.adapterPosition]
                            quote.isFavorite = true
                            mViewModel.updateQuote(quote)
                            Toast.makeText(this@AllQuotesActivity,"Quote updated successfully",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }).attachToRecyclerView(rvAllQuotes)
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
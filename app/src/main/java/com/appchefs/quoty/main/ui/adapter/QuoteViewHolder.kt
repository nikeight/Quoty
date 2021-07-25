package com.appchefs.quoty.main.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.databinding.QuoteItemViewBinding

class QuoteViewHolder(private val itemViewBinding: QuoteItemViewBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {

    fun onBind(quote: Quote){
        itemViewBinding.tvQuoteAuthor.text = quote.author
        itemViewBinding.tvQuoteContent.text = quote.quoteContent
    }
}
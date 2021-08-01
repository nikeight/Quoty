package com.appchefs.quoty.main.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.appchefs.quoty.R
import com.appchefs.quoty.data.model.Quote
import com.appchefs.quoty.databinding.QuoteItemViewBinding

class QuoteViewHolder(private val itemViewBinding: QuoteItemViewBinding, private val context : Context) : RecyclerView.ViewHolder(itemViewBinding.root) {

    fun onBind(quote: Quote){
        itemViewBinding.tvQuoteAuthor.text = quote.author
        itemViewBinding.tvQuoteContent.text = quote.quoteContent

        // TODO: Change the way of setting the images. (i.e passing the context indirectly)

        if (quote.isFavorite){
            itemViewBinding.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_selected))
        }else{
            itemViewBinding.ivFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_not_selected))
        }
    }
}
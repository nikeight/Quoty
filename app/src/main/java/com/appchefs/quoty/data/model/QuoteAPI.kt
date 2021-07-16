package com.appchefs.quoty.data.model

data class QuoteAPI(
    val id: String,
    val content: String,
    val author: String,
    val authorSlug: String,
    val tags: List<String>
)
package com.example.myapplication

import java.io.Serializable

data class NewsResponse(
    val totalArticles: Int,
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String,
    val content: String,
    val url: String,
    val image: String,
    val publishedAt: String,
    val source: Source
) : Serializable

data class Source(
    val name: String,
    val url: String
) : Serializable

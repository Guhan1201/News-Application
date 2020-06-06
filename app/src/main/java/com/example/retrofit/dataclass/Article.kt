package com.example.retrofit.dataclass

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("source")
    var source: SourceModel?,
    @SerializedName("author")
    var author: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("urlToImage")
    var urlToImage: String?,
    @SerializedName("publishedAt")
    var publishedAt: String?
)
package com.example.retrofit.dataclass

import com.example.retrofit.dataclass.Article
import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("status")
    var status: String,
    @SerializedName("totalResults")
    var totalResults: Int = 0,
    @SerializedName("articles")
    var articles: List<Article>
)

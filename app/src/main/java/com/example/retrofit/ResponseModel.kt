package com.example.retrofit

import com.example.retrofit.dataclass.Article
import com.google.gson.annotations.SerializedName

class ResponseModel {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("totalResults")
    var totalResults = 0
    @SerializedName("articles")
    var articles: List<Article>? = null

}
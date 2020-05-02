package com.example.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("top-headlines")
    fun getLatestNews(@Query("country") source: String?, @Query("apiKey") apiKey: String?): Call<ResponseModel>
}
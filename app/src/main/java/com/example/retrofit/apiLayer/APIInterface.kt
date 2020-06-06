package com.example.retrofit.apiLayer

import com.example.retrofit.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("top-headlines")
    fun getLatestNews(@Query("country") source: String?, @Query("apiKey") apiKey: String?): Call<ResponseModel>

    @GET("everything")
    fun getNewsSearch(
        @Query("q") keyword: String?, @Query("language") language: String?,
        @Query("sortBy") sortBy: String?, @Query("apiKey") apiKey: String?): Call<ResponseModel>

}
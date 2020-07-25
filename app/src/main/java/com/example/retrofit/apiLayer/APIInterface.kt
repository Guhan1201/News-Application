package com.example.retrofit.apiLayer

import com.example.retrofit.dataclass.ResponseModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("everything?q=sports")
    fun getLatestNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey")  apiKey: String?
    ): Single<ResponseModel>


//    @GET("everything")
//    fun getLatestNews(
//        @Query("page") page: Int,
//        @Query("pageSize") pageSize: Int,
//        @Query("apiKey") apiKey: String?): Single<ResponseModel>

    @GET("everything")
    fun getNewsSearch(
        @Query("q") keyword: String?, @Query("language") language: String?,
        @Query("sortBy") sortBy: String?, @Query("apiKey") apiKey: String?): Single<ResponseModel>

}
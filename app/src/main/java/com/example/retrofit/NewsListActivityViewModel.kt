package com.example.retrofit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsListActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean> = _spinner

    private val _repos = MutableLiveData<List<Article>>()
    val repos: LiveData<List<Article>> = _repos

    private val _snackbar = MutableLiveData<String>()
    val snackbar: LiveData<String> = _snackbar


    init {
        loadData()
    }


    fun loadData() {
        _spinner.postValue(true)
        ApiClient.buildService(APIInterface::class.java)
            .getLatestNews(Utils.country, "6656eef2af3b40c490f3d6e3db2e049e")
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    _snackbar.postValue(t.message)
                    _spinner.postValue(false)
                }

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {

                    if (response.body()?.status.equals("ok")) {
                        val articleList = response.body()?.articles
                        _repos.postValue(articleList)
                        Log.e("TEST", articleList.toString())
                        _spinner.postValue(false)

                    }
                }

            })
    }

    fun loadDataViasearch(query: String, language: String) {
        _spinner.postValue(true)
        ApiClient.buildService(APIInterface::class.java)
            .getNewsSearch(query,language,"publishedAt","6656eef2af3b40c490f3d6e3db2e049e")
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    _snackbar.postValue(t.message)
                    _spinner.postValue(false)
                }

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.body()?.status.equals("ok")) {
                        val articleList = response.body()?.articles
                        _repos.postValue(articleList)
                        Log.e("TEST", articleList.toString())
                        _spinner.postValue(false)

                    }
                }
            })
    }


}

sealed class Result<out T : Any?> {

    class Success<out T : Any?>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()

}
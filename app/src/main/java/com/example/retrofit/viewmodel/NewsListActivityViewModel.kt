package com.example.retrofit.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.retrofit.dataclass.ResponseModel
import com.example.retrofit.utils.Utils
import com.example.retrofit.apiLayer.APIInterface
import com.example.retrofit.apiLayer.ApiClient
import com.example.retrofit.dataclass.Article
import com.example.retrofit.dataclass.NewsDataSourceFactory
import com.example.retrofit.dataclass.State
import io.reactivex.disposables.CompositeDisposable
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

    val apiInterface = ApiClient.buildService(APIInterface::class.java)

    var newsList: LiveData<PagedList<Article>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val newsDataSourceFactory: NewsDataSourceFactory



    init {
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable, apiInterface)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        newsList = LivePagedListBuilder(newsDataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap(
        newsDataSourceFactory.newsDataSourceLiveData
    ) { it.state }

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


//    fun loadData() {
//        _spinner.postValue(true)
//        apiInterface
//            .getLatestNews(Utils.country, "6656eef2af3b40c490f3d6e3db2e049e")
//            .enqueue(object : Callback<ResponseModel> {
//                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                    _snackbar.postValue(t.message)
//                    _spinner.postValue(false)
//                }
//
//                override fun onResponse(
//                    call: Call<ResponseModel>,
//                    response: Response<ResponseModel>
//                ) {
//
//                    if (response.body()?.status.equals("ok")) {
//                        val articleList = response.body()?.articles
//                        _repos.postValue(articleList)
//                        Log.e("TEST", articleList.toString())
//                        _spinner.postValue(false)
//
//                    }
//                }
//
//            })
//    }

//    fun loadDataViasearch(query: String, language: String) {
//        _spinner.postValue(true)
//        apiInterface
//            .getNewsSearch(query, language, "publishedAt", "6656eef2af3b40c490f3d6e3db2e049e")
//            .enqueue(object : Callback<ResponseModel> {
//                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                    _snackbar.postValue(t.message)
//                    _spinner.postValue(false)
//                }
//
//                override fun onResponse(
//                    call: Call<ResponseModel>,
//                    response: Response<ResponseModel>
//                ) {
//                    if (response.body()?.status.equals("ok")) {
//                        val articleList = response.body()?.articles
//                        _repos.postValue(articleList)
//                        Log.e("TEST", articleList.toString())
//                        _spinner.postValue(false)
//
//                    }
//                }
//            })
//    }


}

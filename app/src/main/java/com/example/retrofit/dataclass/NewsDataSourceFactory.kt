package com.example.retrofit.dataclass

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.retrofit.apiLayer.APIInterface
import io.reactivex.disposables.CompositeDisposable

class NewsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: APIInterface)
    : DataSource.Factory<Int, Article>() {

    val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, Article> {
        val newsDataSource = NewsDataSource(networkService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}
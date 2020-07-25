package com.example.retrofit.dataclass

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.retrofit.apiLayer.APIInterface
import com.example.retrofit.utils.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val networkService: APIInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Article>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getLatestNews(
                1,
                params.requestedLoadSize,
                "6656eef2af3b40c490f3d6e3db2e049e"
            )
                .subscribe(
                    { response ->
                        Log.d("Test in loadInitial", response.toString())

                        updateState(State.DONE)
                        callback.onResult(
                            response.articles,
                            null,
                            2
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getLatestNews(
                params.key,
                params.requestedLoadSize,
                "6656eef2af3b40c490f3d6e3db2e049e"
            )
                .subscribe(
                    { response ->
                        Log.d("Test in loadAfter", response.toString())
                        updateState(State.DONE)
                        callback.onResult(
                            response.articles,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}
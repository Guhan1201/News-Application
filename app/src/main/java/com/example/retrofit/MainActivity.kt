package com.example.retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.activity_main_rv)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        apiCall()


    }

    private fun apiCall() {

        ApiClient.buildService(APIInterface::class.java)
            .getLatestNews("in", "6656eef2af3b40c490f3d6e3db2e049e")
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Log.e("TEST", t.message)

                }

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {

                    if (response.body()?.status.equals("ok")) {
                        val articleList = response.body()?.articles
                        Log.e("TEST", articleList.toString())

                        val mainArticleAdapter = articleList?.let { MainArticleAdapter(it) }
                        recyclerView.adapter = mainArticleAdapter
                    }
                }

            })

    }
}



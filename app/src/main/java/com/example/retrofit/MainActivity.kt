package com.example.retrofit

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MainArticleAdapter
    lateinit var viewModel: NewsListActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var parent : ConstraintLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.activity_main_rv)
        progressBar = findViewById(R.id.progressBar)
        parent = findViewById(R.id.parent)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        viewModel = ViewModelProvider(this).get(NewsListActivityViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        observeViewModel()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.apiCall()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun observeViewModel() {

        with(viewModel) {
            repos.observe(this@MainActivity, Observer {
                it?.let {
                    adapter = MainArticleAdapter(it,baseContext)
                    recyclerView.adapter = adapter
                }
            })
            spinner.observe(this@MainActivity, Observer {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
            })
            snackbar.observe(this@MainActivity, Observer {
                val s = Snackbar.make(parent, it, Snackbar.LENGTH_LONG).config(this@MainActivity)

            })

        }

    }
}



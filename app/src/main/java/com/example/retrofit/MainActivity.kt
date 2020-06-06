package com.example.retrofit

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
        adapter = MainArticleAdapter(this@MainActivity)
        observeViewModel()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search Latest News..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 2) {
                viewModel.loadDataViasearch(query,Utils.language)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Type more than two letters!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun observeViewModel() {

        with(viewModel) {
            repos.observe(this@MainActivity, Observer {
                it?.let {
                    adapter.setList(it)
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



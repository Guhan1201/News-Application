package com.example.retrofit

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar

class NewsListActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MainArticleAdapter
    lateinit var viewModel: NewsListActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var parent: ConstraintLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var articleList: List<Article>

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
        adapter = MainArticleAdapter(this@NewsListActivity)
        observeViewModel()
        initClickListener()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initClickListener() {
        adapter.setOnItemClickListener(this)
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
                    viewModel.loadDataViasearch(query, Utils.language)
                } else {
                    Toast.makeText(
                        this@NewsListActivity,
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
            repos.observe(this@NewsListActivity, Observer {
                it?.let {
                    articleList = it
                    adapter.setList(it)
                    recyclerView.adapter = adapter
                }
            })
            spinner.observe(this@NewsListActivity, Observer {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
            })
            snackbar.observe(this@NewsListActivity, Observer {
                val s =
                    Snackbar.make(parent, it, Snackbar.LENGTH_LONG).config(this@NewsListActivity)

            })

        }

    }

    override fun onItemClick(view: View?, position: Int) {


        val intent = Intent(this@NewsListActivity, NewsDetailActivity::class.java)
        val article: Article = articleList[position]
        intent.putExtra("url", article.url)
        intent.putExtra("title", article.title)
        intent.putExtra("img", article.urlToImage)
        intent.putExtra("date", article.publishedAt)
        intent.putExtra("source", article.source?.name)
        intent.putExtra("author", article.author)

        startActivity(intent)
    }
}



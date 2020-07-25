package com.example.retrofit.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
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
import com.example.retrofit.R
import com.example.retrofit.adapter.ArticleAdapter
import com.example.retrofit.adapter.MainArticleAdapter
import com.example.retrofit.adapter.OnItemClickListener
import com.example.retrofit.dataclass.Article
import com.example.retrofit.dataclass.State
import com.example.retrofit.utils.config
import com.example.retrofit.viewmodel.NewsListActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class NewsListActivity : AppCompatActivity(),
    OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MainArticleAdapter
    lateinit var viewModel: NewsListActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var parent: ConstraintLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var articleList: List<Article>
    private lateinit var newsListAdapter: ArticleAdapter


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
        adapter =
            MainArticleAdapter(this@NewsListActivity)
        initAdapter()
        initState()
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retry()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initAdapter() {
        newsListAdapter = ArticleAdapter { viewModel.retry() }
        recyclerView.adapter = newsListAdapter
        viewModel.newsList.observe(this,
            Observer {
                newsListAdapter.submitList(it)
            })
    }

    private fun initState() {
        viewModel.getState().observe(this, Observer { state ->
            progressBar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }



    private fun initClickListener() {
        adapter.setOnItemClickListener(this)
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



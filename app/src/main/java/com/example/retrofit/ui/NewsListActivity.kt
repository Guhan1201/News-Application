package com.example.retrofit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.retrofit.R
import com.example.retrofit.adapter.ArticleAdapter
import com.example.retrofit.adapter.OnItemClickListener
import com.example.retrofit.dataclass.Article
import com.example.retrofit.dataclass.State
import com.example.retrofit.viewmodel.NewsListActivityViewModel

class NewsListActivity : AppCompatActivity(),
    OnItemClickListener {

    lateinit var recyclerView: RecyclerView
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

        initAdapter()
        initState()
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retry()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initAdapter() {
        newsListAdapter = ArticleAdapter(this) { viewModel.retry() }
        recyclerView.adapter = newsListAdapter
        viewModel.newsList.observe(this,
            Observer {
                newsListAdapter.submitList(it)
                articleList = it
            })
    }

    private fun initState() {
        viewModel.getState().observe(this, Observer { state ->
            progressBar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
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



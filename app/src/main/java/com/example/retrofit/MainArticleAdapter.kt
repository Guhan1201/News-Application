package com.example.retrofit

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.retrofit.MainArticleAdapter.ViewHolder

class MainArticleAdapter(private val articleArrayList: List<Article>) :
    RecyclerView.Adapter<ViewHolder>() {
    private val context: Context? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_main_article_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val articleModel = articleArrayList[position]
        if (!TextUtils.isEmpty(articleModel.title)) {
            viewHolder.titleText.text = articleModel.title
        }
        if (!TextUtils.isEmpty(articleModel.description)) {
            viewHolder.descriptionText.text = articleModel.description
        }
        viewHolder.artilceAdapterParentLinear.tag = articleModel
    }

    override fun getItemCount(): Int {
        return articleArrayList.size
    }

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        internal val titleText: TextView = view.findViewById(R.id.article_adapter_tv_title)
        internal val descriptionText: TextView =
            view.findViewById(R.id.article_adapter_tv_description)
        internal val artilceAdapterParentLinear: LinearLayout =
            view.findViewById(R.id.article_adapter_ll_parent)

    }

}
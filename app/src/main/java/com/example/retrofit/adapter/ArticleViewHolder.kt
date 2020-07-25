package com.example.retrofit.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.retrofit.R
import com.example.retrofit.dataclass.Article
import com.example.retrofit.utils.Utils
import kotlinx.android.synthetic.main.row_main_article_adapter.view.*

class NewsViewHolder(private val view: View, private val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {


    override fun onClick(v: View?) {
        onItemClickListener.onItemClick(view, adapterPosition)
    }

    fun bind(model: Article) {

        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.error(Utils.randomDrawbleColor)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()

        if (model.urlToImage != null) {
            Glide.with(itemView)
                .load(model.urlToImage)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?, model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.prograss_load_photo.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.prograss_load_photo.visibility = View.GONE
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.img)
        }

        itemView.title.text = (model.title)
        itemView.desc.text = model.description
        itemView.source.text = model.source?.name
        itemView.time.text = " \u2022 " + Utils.dateToTimeFormat(
            model.publishedAt
        )
        itemView.publishedAt.setText(Utils.dateFormat(model.publishedAt))
        itemView.author.text = model.author
    }

    init {
        view.setOnClickListener(this)
    }


    companion object {
        fun create(parent: ViewGroup, onItemClickListener: OnItemClickListener): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_main_article_adapter, parent, false)
            return NewsViewHolder(view, onItemClickListener)
        }
    }


}
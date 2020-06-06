package com.example.retrofit

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.retrofit.MainArticleAdapter.ViewHolder

class MainArticleAdapter(private val articleArrayList: List<Article>,private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_main_article_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: Article = articleArrayList.get(position)

        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.error(Utils.randomDrawbleColor)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()

        Glide.with(context)
            .load(model.urlToImage)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?, model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)



        holder.title?.text = (model.title)
        holder.desc?.text = model.description
        holder.source?.text = model.source?.name
        holder.time!!.text = " \u2022 " + Utils.DateToTimeFormat(model.publishedAt)
        holder.published_ad?.setText(Utils.DateFormat(model.publishedAt))
        holder.author?.text = model.author
    }

    override fun getItemCount(): Int {
        return articleArrayList.size
    }

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {


        internal val title = view.findViewById<TextView>(R.id.title)
        internal val desc = view.findViewById<TextView>(R.id.desc)
        internal val author = view.findViewById<TextView>(R.id.author)
        internal val published_ad = view.findViewById<TextView>(R.id.publishedAt)
        internal val source = view.findViewById<TextView>(R.id.source)
        internal val time = view.findViewById<TextView>(R.id.time)
        internal val imageView = view.findViewById<ImageView>(R.id.img)
        internal val progressBar = view.findViewById<ProgressBar>(R.id.prograss_load_photo)


    }

}
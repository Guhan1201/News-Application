package com.example.retrofit.adapter

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
import com.example.retrofit.R
import com.example.retrofit.utils.Utils
import com.example.retrofit.adapter.MainArticleAdapter.ViewHolder
import com.example.retrofit.dataclass.Article
import com.example.retrofit.dataclass.State
import com.example.retrofit.dataclass.State.*
import kotlinx.android.synthetic.main.item_list_footer.view.*

class MainArticleAdapter(private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    private lateinit var articleArrayList: List<Article>
    private lateinit var onItemClickListener: OnItemClickListener

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_main_article_adapter, viewGroup, false)
        return ViewHolder(view, onItemClickListener)
    }

    fun setList(list: List<Article>) {
        articleArrayList = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (getItemViewType(position) == DATA_VIEW_TYPE)
            holder.bind(articleArrayList[position])
        else (holder as ListFooterViewHolder).bind(state)

    }


    override fun getItemCount(): Int {
        return articleArrayList.size
    }

    class ViewHolder(view: View, onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {


        private val title = view.findViewById<TextView>(R.id.title)
        private val desc = view.findViewById<TextView>(R.id.desc)
        private val author = view.findViewById<TextView>(R.id.author)
        private val published_ad = view.findViewById<TextView>(R.id.publishedAt)
        private val source = view.findViewById<TextView>(R.id.source)
        private val time = view.findViewById<TextView>(R.id.time)
        private val imageView = view.findViewById<ImageView>(R.id.img)
        private val progressBar = view.findViewById<ProgressBar>(R.id.prograss_load_photo)
        private var onItemClickListener = onItemClickListener

        init {
            view.setOnClickListener(this)
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
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: com.bumptech.glide.load.DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }

            title.text = (model.title)
            desc.text = model.description
            source.text = model.source?.name
            time.text = " \u2022 " + Utils.dateToTimeFormat(
                model.publishedAt
            )
            published_ad.setText(Utils.dateFormat(model.publishedAt))
            author.text = model.author
        }

        override fun onClick(view: View?) {
            onItemClickListener.onItemClick(view, adapterPosition)
        }

    }


    class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(status: State?) {
            itemView.progress_bar.visibility = if (status == LOADING) View.VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility = if (status == ERROR) View.VISIBLE else View.INVISIBLE
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
                view.txt_error.setOnClickListener { retry() }
                return ListFooterViewHolder(view)
            }
        }
    }

}

interface OnItemClickListener {
    fun onItemClick(view: View?, position: Int)
}

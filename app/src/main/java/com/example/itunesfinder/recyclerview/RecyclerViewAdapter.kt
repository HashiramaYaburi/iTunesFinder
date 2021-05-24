package com.example.itunesfinder.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itunesfinder.model.Item
import com.example.itunesfinder.R


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    var mItemList: List<Item?>? = null
    var mItemCallback: ItemCallback? = null



    constructor (itemList: List<Item?>?, itemCallback: ItemCallback) {
        mItemList = itemList
        mItemCallback = itemCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType === VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item,
                    parent,
                    false
            )
            ItemViewHolder(view, mItemCallback)
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
            )
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            populateItemRows(holder, position)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (mItemList == null) 0 else mItemList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mItemList!!.size-1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }


    class ItemViewHolder(itemView: View, item : ItemCallback? = null) : RecyclerView.ViewHolder(itemView) {
        var artItem : TextView
        var trackItem : TextView
        var imgItem : ImageView
        init {
            artItem = itemView.findViewById(R.id.artist_name)
            trackItem = itemView.findViewById(R.id.track_name)
            imgItem = itemView.findViewById(R.id.iv_image)

            //launch new Activity with values and animation
            itemView.setOnClickListener{
                item!!.onItemClick(adapterPosition, imgItem, artItem, trackItem)

            }
        }

    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar
        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val item = mItemList!![position]
        viewHolder.artItem.text = item!!.artistName
        viewHolder.trackItem.text = item.trackName
        Glide.with(viewHolder.itemView.context)
            .load(item.artworkUrl100)
            .into(viewHolder.imgItem)
    }

}
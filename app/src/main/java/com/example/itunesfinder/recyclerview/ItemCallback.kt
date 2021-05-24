package com.example.itunesfinder.recyclerview

import android.widget.ImageView

import android.widget.TextView




interface ItemCallback {
    fun onItemClick(pos: Int,
                    imgItem: ImageView?,
                    artist: TextView?,
                    track: TextView?)
}